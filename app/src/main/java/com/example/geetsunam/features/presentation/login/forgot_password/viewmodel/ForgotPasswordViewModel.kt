package com.example.geetsunam.features.presentation.login.forgot_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.ForgotPasswordUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUsecase: ForgotPasswordUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(ForgotPasswordState.idle)
    val forgotPasswordState: LiveData<ForgotPasswordState> = _liveState

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.SendResetLink -> {
                sendResetLink()
            }

            is ForgotPasswordEvent.EmailChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ForgotPasswordState.ForgotPasswordStatus.FieldChanging,
                        email = event.fieldValue,
                        isEmailValid = event.validationResult.isValid,
                    )
                )
            }

            is ForgotPasswordEvent.CheckValidation -> {
                val result = isFormValid()
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = if (result) ForgotPasswordState.ForgotPasswordStatus.FormValid
                        else ForgotPasswordState.ForgotPasswordStatus.FormInvalid,
                        message = if (result) "Validated" else "Please validate all data",
                    )
                )
            }

            is ForgotPasswordEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ForgotPasswordState.ForgotPasswordStatus.IDLE,
                        message = null,
                    )
                )
            }
        }
    }

    private fun isFormValid(): Boolean {
        return _liveState.value?.isEmailValid == true
    }

    private fun sendResetLink() {
        forgotPasswordUsecase.call(
            CommonRequestModel(
                email = _liveState.value?.email.toString(),
            )
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ForgotPasswordState.ForgotPasswordStatus.LOADING,
                            message = "Sending reset link"
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ForgotPasswordState.ForgotPasswordStatus.SUCCESS,
                            message = "Link sent to your email",
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ForgotPasswordState.ForgotPasswordStatus.FAILED,
                            message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
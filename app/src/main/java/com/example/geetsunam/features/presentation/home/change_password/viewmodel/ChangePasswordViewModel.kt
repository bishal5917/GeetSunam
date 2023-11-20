package com.example.geetsunam.features.presentation.home.change_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.ChangePasswordUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUsecase: ChangePasswordUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(ChangePasswordState.idle)
    val changePasswordState: LiveData<ChangePasswordState> = _liveState

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.PasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ChangePasswordState.ChangePasswordStatus.FieldChanging,
                        password = event.fieldValue,
                        isPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is ChangePasswordEvent.NewPasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ChangePasswordState.ChangePasswordStatus.FieldChanging,
                        newPassword = event.fieldValue,
                        isNewPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is ChangePasswordEvent.ConfirmNewPasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ChangePasswordState.ChangePasswordStatus.FieldChanging,
                        confirmNewPassword = event.fieldValue,
                        isConfirmPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is ChangePasswordEvent.CheckValidation -> {
                val result = isFormValid()
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = if (result) ChangePasswordState.ChangePasswordStatus.FormValid
                        else ChangePasswordState.ChangePasswordStatus.FormInvalid,
                        message = if (result) "Validated" else "Please validate all data",
                    )
                )
            }

            is ChangePasswordEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = ChangePasswordState.ChangePasswordStatus.IDLE,
                        message = null,
                    )
                )
            }

            is ChangePasswordEvent.ChangePassword -> {
                changePassword(event.token)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return _liveState.value?.isPasswordValid == true && _liveState.value?.isNewPasswordValid == true && _liveState.value?.isConfirmPasswordValid == true
    }

    private fun changePassword(token: String) {
        changePasswordUsecase.call(
            CommonRequestModel(
                token = token,
                currentPassword = _liveState.value?.password,
                newPassword = _liveState.value?.newPassword,
                confirmNewPassword = _liveState.value?.confirmNewPassword,
            )
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ChangePasswordState.ChangePasswordStatus.LOADING,
                            message = "Changing your password"
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ChangePasswordState.ChangePasswordStatus.SUCCESS,
                            message = "Password changed successfully,Please Login again!",
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ChangePasswordState.ChangePasswordStatus.FAILED,
                            message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
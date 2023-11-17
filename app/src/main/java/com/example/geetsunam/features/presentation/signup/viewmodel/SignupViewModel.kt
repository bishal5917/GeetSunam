package com.example.geetsunam.features.presentation.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.data.models.signup.SignupRequestModel
import com.example.geetsunam.features.domain.usecases.SignupUsecase
import com.example.geetsunam.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUsecase: SignupUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(SignupState.idle)
    val signupState: LiveData<SignupState> = _liveState

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.FullNameChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = SignupState.SignupStatus.FieldChanging,
                        name = event.fieldValue,
                        isNameValid = event.validationResult.isValid,
                    )
                )
            }

            is SignupEvent.EmailChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = SignupState.SignupStatus.FieldChanging,
                        email = event.fieldValue,
                        isEmailValid = event.validationResult.isValid,
                    )
                )
            }

            is SignupEvent.PasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = SignupState.SignupStatus.FieldChanging,
                        password = event.fieldValue,
                        isPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is SignupEvent.ConfirmPasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = SignupState.SignupStatus.FieldChanging,
                        confirmPassword = event.fieldValue,
                        isConfirmPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is SignupEvent.Signup -> {
                signUp()
            }

            is SignupEvent.CheckValidation -> {
                val result = isFormValid()
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = if (result) SignupState.SignupStatus.FormValid else SignupState
                            .SignupStatus.FormInvalid,
                        message = if (result) "Validated" else "Please validate all data",
                    )
                )
            }

            is SignupEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = SignupState.SignupStatus.IDLE,
                        message = null,
                    )
                )
            }

            else -> {}
        }
    }

    private fun isFormValid(): Boolean {
        return _liveState.value?.isNameValid == true && _liveState.value?.isEmailValid == true && _liveState.value?.isPasswordValid == true && _liveState.value?.isConfirmPasswordValid == true
    }

    private fun signUp() {
        signupUsecase.call(
            SignupRequestModel(
                fullname = _liveState.value?.name.toString(),
                email = _liveState.value?.email.toString(),
                password = _liveState.value?.password.toString(),
                confirmPassword = _liveState.value?.confirmPassword.toString()
            )
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SignupState.SignupStatus.LOADING,
                            message = "Signing you up,Please wait..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SignupState.SignupStatus.SUCCESS,
                            message = "Signed up successfully,You can log in now!",
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SignupState.SignupStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
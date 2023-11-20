package com.example.geetsunam.features.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.domain.entities.UserEntity
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase, private val localDatastore: LocalDatastore
) : ViewModel() {
    private val _liveState = MutableLiveData(LoginState.idle)
    val loginState: LiveData<LoginState> = _liveState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = LoginState.LoginStatus.FieldChanging,
                        email = event.fieldValue,
                        isEmailValid = event.validationResult.isValid,
                    )
                )
            }

            is LoginEvent.PasswordChanged -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = LoginState.LoginStatus.FieldChanging,
                        password = event.fieldValue,
                        isPasswordValid = event.validationResult.isValid,
                    )
                )
            }

            is LoginEvent.CheckValidation -> {
                val result = isFormValid()
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = if (result) LoginState.LoginStatus.FormValid else LoginState.LoginStatus.FormInvalid,
                        message = if (result) "Validated" else "Please validate all data",
                    )
                )
            }

            is LoginEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = LoginState.LoginStatus.IDLE,
                        message = null,
                    )
                )
            }

            is LoginEvent.LoginUser -> {
                login()
            }

            is LoginEvent.LogoutUser -> {
                logout()
            }
        }
    }

    private fun isFormValid(): Boolean {
        return _liveState.value?.isEmailValid == true && _liveState.value?.isPasswordValid == true
    }

    private fun login() {
        loginUsecase.call(
            LoginRequestModel(
                _liveState.value?.email.toString(), _liveState.value
                    ?.password.toString()
            )
        )
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _liveState.postValue(
                            _liveState.value?.copy(
                                status = LoginState.LoginStatus.LOADING, message = "Logging in ..."
                            )
                        )
                    }

                    is Resource.Success -> {
                        val user = UserEntity(
                            token = result.data?.token,
                            name = result.data?.data?.user?.fullname,
                            email = result.data?.data?.user?.email,
                            image = result.data?.data?.user?.profileImage
                        )
                        _liveState.postValue(
                            _liveState.value?.copy(
                                status = LoginState.LoginStatus.SUCCESS,
                                message = "Logged in successfully",
                                user = user,
                            )
                        )
                        localDatastore.saveUser(user)
                    }

                    is Resource.Error -> {
                        _liveState.postValue(
                            _liveState.value?.copy(
                                status = LoginState.LoginStatus.FAILED, message = result.message
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun logout() {
        viewModelScope.launch {
            _liveState.postValue(
                _liveState.value?.copy(
                    status = LoginState.LoginStatus.LogoutLoading, message = "Logging out"
                )
            )
            localDatastore.removeUser()
            _liveState.postValue(
                _liveState.value?.copy(
                    status = LoginState.LoginStatus.LogoutSuccess,
                    message = "Logged out",
                    user = null
                )
            )
        }
    }
}
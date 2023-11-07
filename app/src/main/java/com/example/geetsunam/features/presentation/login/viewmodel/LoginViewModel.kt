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
    private val _loginState = MutableLiveData(LoginState.idle)
    val loginState: LiveData<LoginState> = _loginState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginUser -> {
                login(event.email, event.password)
            }

            is LoginEvent.LogoutUser -> {
                logout()
            }
        }
    }

    private fun login(email: String, pass: String) {
        loginUsecase.call(LoginRequestModel(email, pass)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = LoginState.LoginStatus.LOADING, message = "Logging in ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = LoginState.LoginStatus.SUCCESS,
                            message = "Logged in successfully",
                            user = result.data?.data?.user,
                        )
                    )
                    localDatastore.saveUser(
                        UserEntity(
                            token = result.data?.token,
                            name = result
                                .data?.data?.user?.fullname,
                            email = result
                                .data?.data?.user?.email,
                            image = result
                                .data?.data?.user?.profileImage
                        )
                    )
                }

                is Resource.Error -> {
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = LoginState.LoginStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun logout() {
        viewModelScope.launch {
            _loginState.postValue(
                _loginState.value?.copy(
                    status = LoginState.LoginStatus.LogoutLoading, message = "Logging out"
                )
            )
            localDatastore.removeUser()
            _loginState.postValue(
                _loginState.value?.copy(
                    status = LoginState.LoginStatus.LogoutSuccess,
                    message = "Logged out",
                    user = null
                )
            )
        }
    }
}
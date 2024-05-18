package com.example.geetsunam.features.presentation.login.google_login_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.entities.UserEntity
import com.example.geetsunam.features.domain.usecases.GoogleLoginUsecase
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GoogleLoginViewModel @Inject constructor(
    private val googleLoginUsecase: GoogleLoginUsecase, private val localDatastore: LocalDatastore
) : ViewModel() {
    private val _loginState = MutableLiveData(GoogleLoginState.idle)
    val googleLoginState: LiveData<GoogleLoginState> = _loginState

    fun onEvent(event: GoogleLoginEvent) {
        when (event) {
            is GoogleLoginEvent.LoginWithGoogle -> {
                loginWithGoogle(event.commonRequestModel)
            }

            is GoogleLoginEvent.HandleError -> {
                _loginState.postValue(
                    _loginState.value?.copy(
                        status = GoogleLoginState.GoogleLoginStatus.FAILED, message = event.message
                    )
                )
            }

            else -> {}
        }
    }

    private fun loginWithGoogle(commonRequestModel: CommonRequestModel) {
        googleLoginUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = GoogleLoginState.GoogleLoginStatus.LOADING,
                            message = "Logging in ..."
                        )
                    )
                }

                is Resource.Success -> {
                    val user = UserEntity(
                        token = result.data?.token,
                        name = result.data?.data?.user?.fullname,
                        email = result.data?.data?.user?.email,
                        image = result.data?.data?.user?.profileImage,
                        isGoogleLogin = true,
                        loggedInTimestamp = System.currentTimeMillis()
                    )
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = GoogleLoginState.GoogleLoginStatus.SUCCESS,
                            message = "Logged in successfully",
                            user = user,
                        )
                    )
                    localDatastore.saveUser(user)
                }

                is Resource.Error -> {
                    _loginState.postValue(
                        _loginState.value?.copy(
                            status = GoogleLoginState.GoogleLoginStatus.FAILED,
                            message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
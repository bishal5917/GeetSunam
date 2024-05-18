package com.example.geetsunam.features.presentation.splash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.utils.DateUtil
import com.example.geetsunam.utils.LogTag
import com.example.heartconnect.features.presentation.screens.splash.viewmodel.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val localDatastore: LocalDatastore) :
    ViewModel() {

    private val _splashState = MutableLiveData(SplashState.IDLE)
    val splashState: LiveData<SplashState> = _splashState

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckStatus -> {
                checkStatus()
            }

            is SplashEvent.SetUser -> {
                _splashState.postValue(
                    _splashState.value?.copy(
                        status = SplashState.SplashStatus.LoggedIn,
                        message = "Logged In",
                        userEntity = event.user,
                    )
                )
            }
        }
    }

    private fun checkStatus() {
        _splashState.postValue(
            _splashState.value?.copy(
                status = SplashState.SplashStatus.LOADING,
                message = "Loading",
            )
        )
        try {
            viewModelScope.launch {
                localDatastore.getUser().collect() {
                    if (it?.token.isNullOrEmpty()) {
                        _splashState.postValue(
                            _splashState.value?.copy(
                                status = SplashState.SplashStatus.LoggedOut,
                                message = "Logged Out",
                                userEntity = it
                            )
                        )
                    } else {
                        if (DateUtil().hasBeenOver5Days(it?.loggedInTimestamp!!)) {
                            _splashState.postValue(
                                _splashState.value?.copy(
                                    status = SplashState.SplashStatus.SessionExpired,
                                    message = "Your session has expired.",
                                    userEntity = it
                                )
                            )
                        } else {
                            _splashState.postValue(
                                _splashState.value?.copy(
                                    status = SplashState.SplashStatus.LoggedIn,
                                    message = "Logged In",
                                    userEntity = it
                                )
                            )
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            _splashState.postValue(
                _splashState.value?.copy(
                    status = SplashState.SplashStatus.LoggedOut,
                    message = "Some error occured",
                )
            )
            Log.d(LogTag.SPLASH, "Exception : ${ex.message}")
        }
    }
}
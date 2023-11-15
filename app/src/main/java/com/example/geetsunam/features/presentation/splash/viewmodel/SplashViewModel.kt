package com.example.geetsunam.features.presentation.splash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.entities.UserEntity
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.utils.LogTag
import com.example.heartconnect.features.presentation.screens.splash.viewmodel.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val localDatastore: LocalDatastore) :
    ViewModel() {

    private val _splashState = MutableLiveData(SplashState.IDLE)
    val splashState: LiveData<SplashState> = _splashState

    val userFlow = localDatastore.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserEntity()
    );

    init {
        viewModelScope.launch {
            userFlow.collect()
        }
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckStatus -> {
                checkStatus()
            }
        }
    }

    private fun checkStatus() = viewModelScope.launch {
        try {
            if (userFlow.value?.token?.isEmpty() == true) {
                _splashState.postValue(
                    _splashState.value?.copy(
                        status = SplashState.SplashStatus.LoggedOut,
                        message = "Logged Out",
                    )
                )
                Log.d(LogTag.SPLASH, "ID: ${userFlow.value}")
            } else {
                _splashState.postValue(
                    _splashState.value?.copy(
                        status = SplashState.SplashStatus.LoggedIn,
                        message = "Logged In",

                        )
                )
                Log.d(LogTag.SPLASH, "ID: ${userFlow.value}")
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
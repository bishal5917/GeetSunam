package com.example.geetsunam.utils

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Idle : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    data class Success<T>(val data: T, val message: String?) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
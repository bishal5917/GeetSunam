package com.example.geetsunam.utils

object Validation {
    private val regexPattern = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")

    fun validateName(value: String): ValidationResult {
        return if (value.isEmpty()) {
            ValidationResult(isValid = false, message = "Cannot be empty")
        } else if (value.length !in 3..20) {
            ValidationResult(
                isValid = false, message = "Name must be 3 to 16 characters long"
            )
        } else ValidationResult(isValid = true, message = "")
    }

    fun validateEmail(value: String): ValidationResult {
        return if (value.isEmpty()) {
            ValidationResult(isValid = false, message = "Cannot be empty")
        } else if (!regexPattern.matches(value)) {
            ValidationResult(isValid = false, message = "Please enter valid email")
        } else ValidationResult(isValid = true, message = "")
    }

    fun validatePassword(value: String): ValidationResult {
        return if (value.isEmpty()) {
            ValidationResult(isValid = false, message = "Cannot be empty")
        } else if (value.length !in 8..20) {
            ValidationResult(
                isValid = false, message = "Password must be 8 to 20 characters long"
            )
        } else ValidationResult(isValid = true, message = "")
    }

    fun confirmPassword(previous: String, current: String): ValidationResult {
        return if (previous != current) {
            ValidationResult(isValid = false, message = "Password not matched")
        } else ValidationResult(isValid = true, message = "")
    }
}

data class ValidationResult(
    val isValid: Boolean = false, val message: String? = null
)
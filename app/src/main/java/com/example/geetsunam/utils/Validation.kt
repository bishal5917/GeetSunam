package com.example.geetsunam.utils

object Validation {
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
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
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
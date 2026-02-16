package com.example.demo.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = isEmailValid && isPasswordValid
}

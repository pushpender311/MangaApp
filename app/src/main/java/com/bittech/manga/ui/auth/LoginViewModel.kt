package com.bittech.manga.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSignedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        _uiState.value = _uiState.value.copy(isSignedIn = repository.isSignedIn())
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun loginOrRegister(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.loginOrSignUp(uiState.value.email, uiState.value.password)
            _uiState.value = _uiState.value.copy(isSignedIn = success)
            onResult(success)
        }
    }

    fun logout(onLogOut: () -> Unit) {
        viewModelScope.launch {
            repository.logOut()
            _uiState.value = _uiState.value.copy(isSignedIn = false)
            onLogOut()
        }
    }
}

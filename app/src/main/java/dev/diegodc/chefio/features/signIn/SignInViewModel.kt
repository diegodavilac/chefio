package dev.diegodc.chefio.features.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.data.models.AuthParam
import dev.diegodc.chefio.data.repositories.IAuthRepository
import dev.diegodc.chefio.data.succeeded
import dev.diegodc.chefio.features.signIn.model.SignInViewState
import dev.diegodc.chefio.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {
    // Init state
    private val _uiState = MutableStateFlow(SignInViewState())
    val uiState: StateFlow<SignInViewState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword(pass: String) {
        _uiState.update {
            it.copy(
                password = pass
            )
        }
    }

    fun updateShowPassword(showPassword: Boolean) {
        _uiState.update {
            it.copy(
                showPass = showPassword
            )
        }
    }


    fun login() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = authRepository.login(
                AuthParam(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
            )

            if (result.succeeded) {
                _uiState.update {
                    it.copy(isLoading = false, isAuthenticated = (result as Result.Success).data)
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, isAuthenticated = false,)
                }
            }
        }
    }
}
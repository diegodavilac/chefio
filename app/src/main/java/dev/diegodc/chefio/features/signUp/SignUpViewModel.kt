package dev.diegodc.chefio.features.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.data.models.AuthParam
import dev.diegodc.chefio.data.repositories.IAuthRepository
import dev.diegodc.chefio.data.succeeded
import dev.diegodc.chefio.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpViewState())
    val uiState: StateFlow<SignUpViewState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateShowPassword(show: Boolean) {
        _uiState.update {
            it.copy(
                showPassword = show
            )
        }
    }

    fun signUp() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = authRepository.signup(
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
                //TODO: handle unhappy paths
                _uiState.update {
                    it.copy(isLoading = false, error = "Sign up failed :(")
                }
            }
        }
    }
}
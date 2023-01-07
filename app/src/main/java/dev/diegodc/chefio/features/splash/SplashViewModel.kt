package dev.diegodc.chefio.features.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.common.base.BaseViewModel
import dev.diegodc.chefio.data.repositories.IAuthRepository
import dev.diegodc.chefio.data.succeeded
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: IAuthRepository
)  : BaseViewModel<SplashViewState>(){

    override val initialState: SplashViewState
        get() = SplashViewState(isLoading = true)

    init {
        checkUserIsAuthenticated()
    }

    private fun checkUserIsAuthenticated(){
        viewModelScope.launch {
            val result = authRepository.getProfile()

            _uiState.update {
                it.copy(isAuthenticated = result.succeeded)
            }
        }
    }

    fun updateLoading( loading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = loading
            )
        }
    }

}
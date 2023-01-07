package dev.diegodc.chefio.features.splash

import dev.diegodc.chefio.common.base.BaseViewState

data class SplashViewState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false
) : BaseViewState

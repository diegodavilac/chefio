package dev.diegodc.chefio.features.signIn.model

data class SignInViewState(
    val isLoading : Boolean = false,
    val email :String = "",
    val password : String = "",
    val showPass : Boolean = false,
    val isAuthenticated : Boolean = false,
    val error: String? = null
) {
    val credentialsCompleted: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty()
}

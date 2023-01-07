package dev.diegodc.chefio.features.signUp

data class SignUpViewState(
    val isLoading : Boolean = false,
    val email : String = "",
    val password : String = "",
    val showPassword : Boolean = false,
    val error: String? = null,
    val isAuthenticated : Boolean = false
) {
    val atLeast6Chars : Boolean
        get() =  password.length >= 6

    val containsNumber : Boolean
        get() = password.contains("[0-9]".toRegex())
}
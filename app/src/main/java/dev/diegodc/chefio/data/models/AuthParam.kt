package dev.diegodc.chefio.data.models

data class AuthParam(
    val email : String,
    val password: String,
    val name: String = "",
    val lastName: String = ""
)

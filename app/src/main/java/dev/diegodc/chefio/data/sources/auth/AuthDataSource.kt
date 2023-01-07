package dev.diegodc.chefio.data.sources.auth

import dev.diegodc.chefio.data.models.Profile

interface AuthDataSource {
    suspend fun login(email: String, password: String) : Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun getProfile(): Profile?
    suspend fun logout() : Boolean
}
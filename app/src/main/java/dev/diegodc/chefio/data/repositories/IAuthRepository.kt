package dev.diegodc.chefio.data.repositories

import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.models.AuthParam
import dev.diegodc.chefio.data.models.Profile

interface IAuthRepository {
    suspend fun signup(param: AuthParam) : Result<Boolean>
    suspend fun login(param: AuthParam) : Result<Boolean>
    suspend fun getProfile() : Result<Profile>
}
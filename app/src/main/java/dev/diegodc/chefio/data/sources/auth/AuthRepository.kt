package dev.diegodc.chefio.data.sources.auth

import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.models.AuthParam
import dev.diegodc.chefio.data.models.Profile
import dev.diegodc.chefio.data.repositories.IAuthRepository

class AuthRepository constructor(
    private val remoteDataSource : AuthDataSource
) : IAuthRepository {
    override suspend fun signup(param: AuthParam): Result<Boolean> {
        val result = remoteDataSource.signUp(email = param.email, password = param.password)
        return Result.Success(result)
    }

    override suspend fun login(param: AuthParam): Result<Boolean> {
        val result = remoteDataSource.login(email = param.email, password = param.password)
        return Result.Success(result)
    }

    override suspend fun getProfile(): Result<Profile> {
        val result = remoteDataSource.getProfile()
        return if (result != null) {
            Result.Success(result)
        } else {
            Result.Error(exception = IllegalStateException("Profile not found"))
        }
    }
}
package dev.diegodc.chefio.data.sources.auth.firebase


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dev.diegodc.chefio.data.models.Profile
import dev.diegodc.chefio.data.sources.auth.AuthDataSource
import kotlinx.coroutines.tasks.await

class AuthFirebaseDataSource constructor(
    private val auth: FirebaseAuth
) : AuthDataSource {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val request = auth.signInWithEmailAndPassword(email, password).await()
            request.user != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            val request = auth.createUserWithEmailAndPassword(email, password).await()
            request.user != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getProfile(): Profile? {
        val user = auth.currentUser
        return if (user != null) {
            Profile(
                email = user.email?:"",
                name = user.displayName?:"",
                photo = user.photoUrl.toString(),
                lastName = ""
            )
        } else {
            null
        }
    }

    override suspend fun logout(): Boolean {
        auth.signOut()
        return true
    }
}
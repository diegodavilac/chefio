package dev.diegodc.chefio.models

data class UserProfile(
    val name: String,
    val lastName: String,
    val username:String,
    val photo: String,
) {
    fun toFirebaseMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "lastName" to lastName,
            "username" to username,
            "photo" to photo
        )
    }

    companion object {
        fun fromFirebaseMap(data: Map<String, Any?>) : UserProfile {
            return UserProfile(
                name = data["name"] as String,
                lastName = data["lastName"] as String,
                username = data["username"] as String,
                photo = data["photo"] as String
            )
        }
    }
}

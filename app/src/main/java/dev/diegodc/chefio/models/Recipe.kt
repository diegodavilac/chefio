package dev.diegodc.chefio.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val timeToPrepare: Int = 0,
    val creator: UserProfile,
    val ingredients: List<String> = listOf(),
    val preparationSteps: List<PreparationStep> = listOf(),
    val createdAt: Date,
    val address: String,
    val location: LatLng? = null
) {
    companion object {
        fun fromFirebaseMap(id: String, data: Map<String, Any>?): Recipe {
           return Recipe(
                id = id,
                title = data?.get("title") as String,
                description = data["description"] as String,
                image = data["image"] as String,
                timeToPrepare = (data["timeToPrepare"] as Long).toInt(),
                ingredients = data["ingredients"] as List<String>,
                createdAt = (data["createdAt"] as Timestamp).toDate(),
                creator = UserProfile.fromFirebaseMap(data["creator"] as Map<String, Any?>),
                preparationSteps = (data["preparationSteps"] as List<Map<String,Any?>>).map {
                    PreparationStep(it["description"] as String)
                },
                address = (data["address"] as String?) ?: "",
                location = (data["location"] as GeoPoint?)?.let {
                    LatLng(it.latitude,it.longitude)
                }
            )
        }
    }

    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "image" to image,
            "timeToPrepare" to timeToPrepare,
            "creator" to creator.toFirebaseMap(),
            "ingredients" to ingredients,
            "preparationSteps" to preparationSteps.map { it.toFirebaseMap() },
            "createdAt" to Timestamp(createdAt),
            "address" to address,
            "location" to location?.let { GeoPoint(it.latitude, it.longitude) }
        )
    }
}

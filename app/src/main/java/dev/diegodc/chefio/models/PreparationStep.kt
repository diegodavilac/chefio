package dev.diegodc.chefio.models

data class PreparationStep(
    val description: String
) {
    fun toFirebaseMap() : Map<String, Any>{
        return mapOf(
            "description" to description
        )
    }
}

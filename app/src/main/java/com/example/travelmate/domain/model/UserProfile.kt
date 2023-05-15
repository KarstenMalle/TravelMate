package com.example.travelmate.domain.model

data class UserProfile(
    val uid: String = "",
    var fullName: String = "",
    val email: String = "",
    var photoUrl: String? = null,
    var age: Int = 0,
    var homeCountry: String = "",
    var interests: List<String> = emptyList(),
    var bio: String = ""
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "uid" to uid,
            "fullName" to fullName,
            "email" to email,
            "photoUrl" to photoUrl,
            "age" to age,
            "homeCountry" to homeCountry,
            "interests" to interests,
            "bio" to bio
        )
    }
}
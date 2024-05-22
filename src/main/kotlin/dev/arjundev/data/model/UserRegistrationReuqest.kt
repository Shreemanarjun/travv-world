package dev.arjundev.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationRequest(val userName: String, val email: String, val password: String)
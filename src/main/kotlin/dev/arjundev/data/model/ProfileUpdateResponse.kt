package dev.arjundev.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateResponse(val profile: Profile, val updateMessage: String)

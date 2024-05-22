package dev.arjundev.data.model

import kotlinx.serialization.Serializable

@Serializable

data class MyToken(val accessToken:String,val refreshToken:String)

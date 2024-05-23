package dev.arjundev.data.model

import dev.arjundev.data.table.User
import kotlinx.serialization.Serializable

@Serializable
data class Profile(val userID: String, val userName: String, val email: String)

fun User.toProfile() = Profile(userID = this.id, userName = this.username, email = this.email)
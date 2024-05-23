package dev.arjundev.data.table

import org.jetbrains.exposed.dao.id.UUIDTable

data class User(val id: String, val username: String, val password: String, val email: String)


object UserTable : UUIDTable(name = "User", columnName = "userID") {

    val username = varchar("username", 128)
    val email = varchar("email", length = 1024)
    val password = varchar("password", 1024)


}
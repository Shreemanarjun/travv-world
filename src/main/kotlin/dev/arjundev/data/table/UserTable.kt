package dev.arjundev.data.table

import org.jetbrains.exposed.dao.id.UUIDTable

data class Users(val users: List<User>)
data class User(val id: String, val username: String, val password: String, val email: String)


object UserTable : UUIDTable(name = "User", columnName = "userID") {

    val username = varchar("username", 128).uniqueIndex()
    val email = varchar("email", length = 1024).uniqueIndex()
    val password = varchar("password", 1024)


}
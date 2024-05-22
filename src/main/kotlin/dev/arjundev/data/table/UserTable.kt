package dev.arjundev.data.table

import org.jetbrains.exposed.dao.id.UUIDTable

data class Users(val users:List<User>)
data class User(val id: Int?, val username: String, val password: String)


object UserTable : UUIDTable(name = "User", columnName = "userID") {

    val username = varchar("username", 128).uniqueIndex()
    val password = varchar("password", 1024)



}
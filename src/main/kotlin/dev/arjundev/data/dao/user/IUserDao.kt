package dev.arjundev.data.dao.user

import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.table.User

interface IUserDao {
    suspend fun getAllUser(): List<User>
    suspend fun isUserAvailable(id: String): User?

    suspend fun isUserAvailable(user: UserLoginRequest): Boolean

    suspend fun getUser(user: UserLoginRequest): User?

    suspend fun isEmailExist(email: String): User?

    suspend fun isUserNameExist(username: String): User?
    suspend fun addNewUser(email: String, username: String, password: String): User?
    suspend fun deleteUser(id: String): Boolean
}
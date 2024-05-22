package dev.arjundev.data.dao.user

import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.table.User
import dev.arjundev.data.table.UserTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserDao : IUserDao {
    private fun ResultRow.toUser() = User(
        id = this[UserTable.id].value.toString(),
        username = this[UserTable.username],
        password = this[UserTable.password],
        email = this[UserTable.email]
    )

    override suspend fun getAllUser(): List<User> = DatabaseFactory.dbQuery {
        UserTable.selectAll().map { it.toUser() }
    }

    override suspend fun isUserAvailable(user: UserLoginRequest): Boolean = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.email eq user.email) and (UserTable.password eq user.password) }
            .map { it.toUser() }
            .isNotEmpty()
    }

    override suspend fun getUser(user: UserLoginRequest): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.email eq user.email) and (UserTable.password eq user.password) }
            .map { it.toUser() }
            .singleOrNull()
    }


    override suspend fun isUserNameExist(username: String): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.username eq username) }
            .map { it.toUser() }.firstOrNull()

    }
    override suspend fun isEmailExist(email: String): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.email eq email) }
            .map { it.toUser() }.firstOrNull()

    }

    override suspend fun isUserAvailable(id: Int): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.id eq UUID.fromString(id.toString())) }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun addNewUser(email: String,username:String, password: String): User? = DatabaseFactory.dbQuery {
        val insertStatement = UserTable.insert {
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.username]=username
        }
        insertStatement.resultedValues?.singleOrNull()?.let { it.toUser() }
    }

    override suspend fun deleteUser(id: Int): Boolean = DatabaseFactory.dbQuery {
        UserTable.deleteWhere { UserTable.id eq UUID.fromString(id.toString()) } > 0
    }
}

val userDao: IUserDao = UserDao().apply {
    runBlocking {
//        if(getAllUser().isEmpty()) {
//            addNewUser(email = "Arjun@mail.in", password = "password")
//
//        }
    }
}
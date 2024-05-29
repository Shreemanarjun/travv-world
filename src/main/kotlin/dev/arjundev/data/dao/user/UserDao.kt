package dev.arjundev.data.dao.user

import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.table.User
import dev.arjundev.data.table.UserTable
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

    override suspend fun isUserAvailable(id: String): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { (UserTable.id eq UUID.fromString(id)) }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun addNewUser(email: String, username: String, password: String): User? =
        DatabaseFactory.dbQuery {
            val insertStatement = UserTable.insert {
                it[UserTable.email] = email
                it[UserTable.password] = password
                it[UserTable.username] = username
            }
            insertStatement.resultedValues?.singleOrNull()?.toUser()
        }

    override suspend fun deleteUser(id: String): Boolean = DatabaseFactory.dbQuery {
        UserTable.deleteWhere { UserTable.id eq UUID.fromString(id) } > 0
    }

    override suspend fun updateUsernameAndEmail(id: String, email: String, username: String): Boolean = DatabaseFactory.dbQuery {
        UserTable.update({ UserTable.id eq UUID.fromString(id) }) {
            it[UserTable.username] = username
            it[UserTable.email] = email
        } > 0
    }

    override suspend fun updateUsername(id: String, username: String): Boolean =DatabaseFactory.dbQuery {
        UserTable.update({ UserTable.id eq UUID.fromString(id) }) {
            it[UserTable.username] = username

        } > 0
    }

    override suspend fun updateEmail(id: String, email: String): Boolean =DatabaseFactory.dbQuery {
        UserTable.update({ UserTable.id eq UUID.fromString(id) }) {

            it[UserTable.email] = email
        } > 0
    }

    override suspend fun isUsernameAvailable(id: String, username: String): Boolean = DatabaseFactory.dbQuery {
        UserTable.select { (UserTable.username eq username) and (UserTable.id neq UUID.fromString(id)) }
            .map { it.toUser() }
            .isEmpty()
    }

    override suspend fun isEmailAvailable(id: String, email: String): Boolean = DatabaseFactory.dbQuery {
        UserTable.select { (UserTable.email eq email) and (UserTable.id neq UUID.fromString(id)) }
            .map { it.toUser() }
            .isEmpty()
    }
}


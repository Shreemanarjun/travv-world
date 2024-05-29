package dev.arjundev.data.dao

import dev.arjundev.data.table.TokenTable
import dev.arjundev.data.table.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(
        createTablesIfExist: Boolean = true
    ): Result<Database> {

        try {
            val database = Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                user = "root",
                driver = "org.h2.Driver",
                password = ""

            )
            if (createTablesIfExist) {
                createTables()
            } else {
                dropAllTables()

            }


            return Result.success(database)
        } catch (e: Exception) {
            return Result.failure(e)

        }
    }

    fun createTables(): Unit {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UserTable)
            SchemaUtils.createMissingTablesAndColumns(TokenTable)

        }
    }

    fun dropAllTables(): Unit {

        transaction {
            SchemaUtils.drop(TokenTable)
            SchemaUtils.drop(UserTable)

        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
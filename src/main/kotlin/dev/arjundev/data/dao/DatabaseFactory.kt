package dev.arjundev.data.dao

import dev.arjundev.data.table.TokenTable
import dev.arjundev.data.table.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(): Result<Boolean> {

        try {
            val database = Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                user = "root",
                driver = "org.h2.Driver",
                password = ""
            )
//            val driverClassName = "org.postgresql.Driver"
//            val jdbcURL = "jdbc:postgresql://localhost:5432/TravvWorld"
//            val database = Database.connect(jdbcURL, driverClassName, user = "shreemanarjunsahu")
//
//            val database = Database.connect(
//                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//                user = "root",
//                driver = "org.h2.Driver",
//                password = ""
//            )
            transaction(database) {
                SchemaUtils.createMissingTablesAndColumns(UserTable)
                SchemaUtils.createMissingTablesAndColumns(TokenTable)
                //  SchemaUtils.create(BlogTable)
            }
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)

        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
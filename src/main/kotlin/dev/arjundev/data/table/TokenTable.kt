package dev.arjundev.data.table

import org.jetbrains.exposed.sql.Table

data class Tokens(val tokens: List<Token>)

data class Token(val id: String, val accessToken:String?, val refreshToken:String)

object TokenTable: Table() {
    val userId =reference("userID",UserTable.id)
    val accessToken=varchar("accessToken", length = 1024)
    val refreshToken=varchar("refreshToken", length = 1024)

    override val primaryKey=PrimaryKey(userId)

}
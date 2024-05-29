package dev.arjundev.data.dao.token

import com.example.data.dao.token.TokenDaoFacade
import com.example.data.dao.token.TokenType
import dev.arjundev.data.dao.DatabaseFactory.dbQuery
import dev.arjundev.data.table.Token
import dev.arjundev.data.table.TokenTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class TokenDaoFacadeImpl : TokenDaoFacade {
    private fun resultRowToArticle(row: ResultRow) = Token(
        id = row[TokenTable.userId].value.toString(),
        accessToken = row[TokenTable.accessToken],
        refreshToken = row[TokenTable.refreshToken],
    )

    override suspend fun isRefreshTokenAvailable(userId: String, refreshToken: String): Boolean = dbQuery {
        TokenTable.select { (TokenTable.userId eq UUID.fromString(userId)) and (TokenTable.refreshToken eq refreshToken) }
            .map(::resultRowToArticle).isNotEmpty()
    }

    override suspend fun getAllToken(): List<Token> = dbQuery {
        TokenTable.selectAll().map(::resultRowToArticle)
    }

    override suspend fun getTokens(userId: String): Token? = dbQuery {
        TokenTable.select(where = TokenTable.userId eq UUID.fromString(userId)).map(::resultRowToArticle)
            .firstOrNull()
    }

    override suspend fun addToken(token: Token): Boolean = dbQuery {
        val insertStatement = TokenTable.insert {
            it[userId] = UUID.fromString(token.id)
            it[accessToken] = token.accessToken ?: ""
            it[refreshToken] = token.refreshToken
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle) != null
    }

    override suspend fun replaceAccessToken(userId: String, accessToken: String) = dbQuery {
        val updateStatement = TokenTable.update(where = { TokenTable.userId eq UUID.fromString(userId) }) {
            it[TokenTable.accessToken] = accessToken
        }
        updateStatement > 0
    }


    override suspend fun isTokenAvailable(userId: String): Boolean = dbQuery {
        TokenTable.select { TokenTable.userId eq UUID.fromString(userId) }.map(::resultRowToArticle)
            .isNotEmpty()
    }

    override suspend fun deleteToken(tokenType: TokenType, userId: String) = dbQuery {
        when (tokenType) {
            TokenType.accessToken -> {
                val updateStatement =
                    TokenTable.update(where = { TokenTable.userId eq UUID.fromString(userId) }) {
                        it[accessToken] = ""

                    }
                updateStatement > 0
            }

            TokenType.refreshToken -> {
                val updateStatement =
                    TokenTable.update(where = { TokenTable.userId eq UUID.fromString(userId) }) {
                        it[refreshToken] = ""

                    }
                updateStatement > 0
            }

            TokenType.allToken -> {
                TokenTable.deleteWhere { TokenTable.userId eq UUID.fromString(userId) } > 0
            }
        }
    }
}

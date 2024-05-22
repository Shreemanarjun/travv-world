package com.example.data.dao.token

import dev.arjundev.data.table.Token

enum class TokenType{
    accessToken,refreshToken,allToken
}

interface TokenDaoFacade {
    suspend fun isRefreshTokenAvailable(userId: String,refreshToken:String):Boolean
    suspend fun getAllToken():List<Token>

    suspend fun getTokens(userId: String):Token?

    suspend fun addToken(token: Token):Boolean

    suspend fun replaceAccessToken(userId: String,accessToken:String):Boolean

    suspend fun isTokenAvailable(userId: String):Boolean

    suspend fun deleteToken(tokenType:TokenType,userId: String):Boolean
}
package dev.arjundev

import com.example.data.dao.token.TokenDaoFacade
import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.dao.token.TokenDaoFacadeImpl
import dev.arjundev.data.dao.user.IUserDao
import dev.arjundev.data.dao.user.UserDao
import dev.arjundev.data.model.MyToken
import dev.arjundev.data.model.Response
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.model.UserRegistrationRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileTest : KoinTest {
    val testModule = module {

        single<IUserDao> { mockk<UserDao>(relaxed = true) }
        single<TokenDaoFacade> { mockk<TokenDaoFacadeImpl>(relaxed = true) }
    }

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)

        }



    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testProfileRequest() = testApplication {



        DatabaseFactory.init(createTablesIfExist = false)


        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(UserRegistrationRequest(userName = "examples", email = "test@mail.com", password = "password"))
        }.apply {
            println(bodyAsText())
            assertEquals(HttpStatusCode.Created, status, message = "User signed up status is oK")
            assertEquals(
                Json.encodeToString(Response.Success(data = "User successfully created")),
                bodyAsText(),
                message = "User sign up successfull body received"
            )
        }

        val loginResponse = client.post("api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLoginRequest(email = "test@mail.com", password = "password"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(true, bodyAsText().contains("data"), message = "login request should have data block ")
            assertEquals(true, bodyAsText().contains("accessToken"), message = "login request should have accessToken ")
            assertEquals(
                true,
                bodyAsText().contains("refreshToken"),
                message = "login request should have refreshToken "
            )

        }

        val token = Json.decodeFromString<Response.Success<MyToken>>(loginResponse.bodyAsText())

        client.get("api/v1/user/profile") {
            headers {
                appendAll(StringValues.build {
                    set("Authorization", "Bearer ${token.data.accessToken}")
                })
            }
        }.apply {

            assertEquals(HttpStatusCode.OK, status)
            assertEquals(true, bodyAsText().contains("data"), message = "profile response should have data block ")
            assertEquals(true, bodyAsText().contains("userID"), message = "profile response should have userID key ")
            assertEquals(true, bodyAsText().contains("email"), message = "profile response should have email key ")
            assertEquals(
                true,
                bodyAsText().contains("userName"),
                message = "profile response should have userName key "
            )
        }
    }
}
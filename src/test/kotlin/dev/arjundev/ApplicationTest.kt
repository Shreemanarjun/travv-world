import com.example.data.dao.token.TokenDaoFacade
import dev.arjundev.data.dao.token.TokenDaoFacadeImpl
import dev.arjundev.data.dao.user.IUserDao
import dev.arjundev.data.dao.user.UserDao
import dev.arjundev.data.model.Response
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.model.UserRegistrationRequest
import dev.arjundev.data.table.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals





// Assuming User, UserLoginRequest, IUserDao, and TokenDaoFacade classes/interfaces are defined elsewhere

class ApplicationTest : KoinTest {
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
    fun testInvalidEmailSignupRequest() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(UserRegistrationRequest(userName = "e", email = "testmail.com", password = "passwords"))
        }.apply {
            assertEquals(
                HttpStatusCode.UnprocessableEntity,
                status,
                message = "User signed up status is UnprocessableEntity"
            )
            assertEquals(
                Json.encodeToString(
                    Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = listOf("email: Must be a valid email")
                    )
                ),
                bodyAsText(),
                message = "User sign up successfull body received"
            )
        }
    }

    @Test
    fun testInvalidPasswordSignupRequest() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(UserRegistrationRequest(userName = "e", email = "tes@tmail.com", password = ""))
        }.apply {
            assertEquals(
                HttpStatusCode.UnprocessableEntity,
                status,
                message = "User signed up status is UnprocessableEntity"
            )
            assertEquals(
                Json.encodeToString(
                    Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = listOf(
                            "password: Must not be blank",
                            "password: Length must be greater than or equal to 8"
                        )
                    )
                ),
                bodyAsText(),
                message = "Password must not be blank and should have length grater equal to 8"
            )
        }
    }

    @Test
    fun testInvalidUsernameSignupRequest() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(UserRegistrationRequest(userName = "", email = "tes@tmail.com", password = "abcd1234"))
        }.apply {
            assertEquals(
                HttpStatusCode.UnprocessableEntity,
                status,
                message = "User signed up status is UnprocessableEntity"
            )
            assertEquals(
                Json.encodeToString(
                    Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = listOf(

                            "userName: Must not be blank"
                        )
                    )
                ),
                bodyAsText(),
                message = "Username should be required"
            )
        }
    }

    @Test
    fun testInvalidEmailSigninrequest() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLoginRequest(email = "testmail.com", password = "password"))
        }.apply {
            assertEquals(
                HttpStatusCode.UnprocessableEntity,
                status,
                message = "User signed in status is UnprocessableEntity"
            )
            assertEquals(
                Json.encodeToString(
                    Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = listOf(

                            "email: Must be a valid email"
                        )
                    )
                ),
                bodyAsText(),
                message = "Email should be valid"
            )
        }
    }

    @Test
    fun testInvalidPasswordSigninrequest() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLoginRequest(email = "test@mail.com", password = ""))
        }.apply {
            assertEquals(
                HttpStatusCode.UnprocessableEntity,
                status,
                message = "User signed in status is UnprocessableEntity"
            )
            assertEquals(
                Json.encodeToString(
                    Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = listOf(
                            "email: This email is not registered yet","password: Must not be blank","password: Length must be greater than or equal to 8"
                        )
                    )
                ),
                bodyAsText(),
                message = "LoginRequest should be valid"
            )
        }
    }

    @Test
    fun testSignupSignInRequest() = testApplication {
        val userDao: IUserDao by inject()

        coEvery { userDao.isEmailExist(email = any()) } returns User(
            id = "1",
            email = "test@mail.com",
            username = "username",
            password = "password"
        )
        coEvery { userDao.isUserAvailable(id = any()) } returns User(
            id = "1",
            email = "test@mail.com",
            username = "username",
            password = "password"
        )
        coEvery {
            userDao.getUser(
                user = UserLoginRequest(
                    email = "test@mail.com",
                    password = "password"
                )
            )
        } returns User(
            id = "1",
            email = "test@mail.com",
            username = "username",
            password = "password"
        )


        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        client.post("api/v1/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(UserRegistrationRequest(userName = "example", email = "test@mail.com", password = "password"))
        }.apply {
            assertEquals(HttpStatusCode.Created, status, message = "User signed up status is oK")
            assertEquals(
                Json.encodeToString(Response.Success(data = "User successfully created")),
                bodyAsText(),
                message = "User sign up successfull body received"
            )
        }

        client.post("api/v1/user/login") {
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
    }
}
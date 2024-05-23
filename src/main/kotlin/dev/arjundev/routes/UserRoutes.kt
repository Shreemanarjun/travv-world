package dev.arjundev.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.dao.token.TokenType
import dev.arjundev.data.dao.token.tokenDao
import dev.arjundev.data.dao.user.userDao
import dev.arjundev.data.model.MyToken
import dev.arjundev.data.model.Response
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.model.UserRegistrationRequest
import dev.arjundev.data.table.Token
import dev.arjundev.data.table.User
import io.github.smiley4.ktorswaggerui.data.ValueExampleDescriptor
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Routing.userRoutes() {
    val audience = this@userRoutes.environment?.config?.property("jwt.audience")?.getString()
    val issuer = this@userRoutes.environment?.config?.property("jwt.domain")?.getString()
    val secret = this@userRoutes.environment?.config?.property("jwt.secret")?.getString()
    val accessTokenExpiryTime = 60000 * 30
    val refreshTokenExpiryTime = 60000 * 60 * 24

    suspend fun createAccessAndRefreshRefreshToken(user: User): Pair<String, String> {
        val accessToken = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userid", user.id)
            .withClaim("username", user.username)

            .withClaim("tokenType", "accessToken")
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenExpiryTime))
            .sign(Algorithm.HMAC256(secret))
        val refreshToken = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userid", user.id)
            .withClaim("username", user.username)

            .withClaim("tokenType", "refreshToken")
            .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenExpiryTime))
            .sign(Algorithm.HMAC256(secret))
        tokenDao.addToken(Token(id = user.id, accessToken = accessToken, refreshToken = refreshToken))
        return Pair(accessToken, refreshToken)
    }
    route("api/v1/user") {
        ///user registration,with a username and email,password
        post("signup", {
            routeApiV1UserSignup()
        }) {
            val userregistartionrequest = call.receive<UserRegistrationRequest>()


            val user = userDao.addNewUser(
                email = userregistartionrequest.email,
                username = userregistartionrequest.userName,
                userregistartionrequest.password
            )
            if (user != null) {
                call.respond(status = HttpStatusCode.Created, Response.Success(data = "User successfully created"))
            } else {
                call.respond(status = HttpStatusCode.BadRequest, Response.Error(message = "Unable to register user"))
            }


        }
        //user login witth email and password

        post("login", {
            routeApiV1UserLogin()
        }) {
            val userloginRequest = call.receive<UserLoginRequest>()


            val user = userDao.getUser(userloginRequest)
            when {
                user != null -> {
                    val isTokenAvailable = user.id.let { it1 -> tokenDao.isTokenAvailable(userId = it1) }
                    when {
                        isTokenAvailable -> {
                            tokenDao.deleteToken(tokenType = TokenType.allToken, userId = user.id)
                            val (accessToken, refreshToken) = createAccessAndRefreshRefreshToken(user = user)
                            call.respond(
                                Response.Success(
                                    data = MyToken(
                                        accessToken = accessToken,
                                        refreshToken = refreshToken
                                    )
                                )
                            )
                        }

                        else -> {
                            val (accessToken, refreshToken) = createAccessAndRefreshRefreshToken(user = user)
                            call.respond(
                                Response.Success(
                                    data = MyToken(
                                        accessToken = accessToken,
                                        refreshToken = refreshToken
                                    )
                                )
                            )
                        }
                    }

                }

                else -> call.respond(
                    HttpStatusCode.BadRequest,
                    Response.Error(message = "Invalid username or password")
                )
            }
        }


    }
}

private fun OpenApiRoute.routeApiV1UserLogin() {
    run {
        tags = listOf("User")
        description = "Performs user login with email and password"
        request {

            body<UserLoginRequest> {
                // specify two example values
                example(
                    ValueExampleDescriptor(
                        name = "Success example",
                        description = "A longer description of the example",
                        summary = "Success example",
                        value = UserLoginRequest(
                            email = "example@email.com", password = "password"
                        )
                    )

                )
                example(
                    ValueExampleDescriptor(
                        name = "Empty email",
                        description = "A longer description of the example",
                        summary = "Empty Email",
                        value = UserLoginRequest(
                            email = "", password = "password"
                        )
                    )

                )
                example(
                    ValueExampleDescriptor(
                        name = "Bad email",
                        description = "A longer description of the example",
                        summary = "Bad email",
                        value = UserLoginRequest(
                            email = "example", password = "password"
                        )
                    )

                )

                example(
                    ValueExampleDescriptor(
                        name = "Bad password with length",
                        description = "A longer description of the example",
                        summary = "Bad password with length",
                        value = UserLoginRequest(
                            email = "example", password = ""
                        )
                    )

                )

            }

        }

        response {


            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<Response.Success<MyToken>> {
                    example(
                        ValueExampleDescriptor(
                            name = "Login Successfully",
                            description = "You will get access token and refresh token",
                            summary = "Logged in ",

                            value = Response.Success(data = MyToken(accessToken = "a", refreshToken = "a"))
                        )
                    )
                }


            }
            HttpStatusCode.BadRequest to {
                description = "Something went wrong"

                body<Response.Error>() {
                    example(
                        ValueExampleDescriptor(
                            name = "Bad request",
                            value = Response.Error(
                                message = "Please check information related to login",
                                exception = null
                            )
                        )
                    )
                }


            }
            HttpStatusCode.InternalServerError to {
                description = "Internal Server Error"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "Internal Server Error",
                            value = Response.Error(message = "Internal Server Error", exception = null)
                        )
                    )
                }

            }

            HttpStatusCode.UnprocessableEntity to {
                description = "Unprocessable Entity"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "Unprocessable Entity Error",
                            value = Response.Error(
                                message = "Unprocessable  gEntity Error.Check input data",
                                exception = null
                            )
                        )
                    )
                }

            }
        }
    }
}

private fun OpenApiRoute.routeApiV1UserSignup() {
    run {
        tags = listOf("User")
        description = "Performs user registration with unique username,email,and password"
        request {

            body<UserRegistrationRequest> {
                // specify two example values
                example(
                    ValueExampleDescriptor(
                        name = "First",
                        description = "A longer description of the example",
                        summary = "User registration request",
                        value = UserRegistrationRequest(
                            userName = "Arjun", email = "example@email.com", password = "password"
                        )
                    )
                )

            }

        }

        response {


            HttpStatusCode.Created to {
                description = "The operation was successful"
                body<Response.Success<String>> {
                    example(
                        ValueExampleDescriptor(
                            name = "SuccessUser successfully created",

                            value = Response.Success(data = "User successfully created")
                        )
                    )
                }


            }
            HttpStatusCode.BadRequest to {
                description = "Something went wrong"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "Bad request",
                            value = Response.Error(message = "Please check information", exception = null)
                        )
                    )
                }


            }
            HttpStatusCode.InternalServerError to {
                description = "Internal Server Error"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "Internal Server Error",
                            value = Response.Error(message = "Internal Server Error", exception = null)
                        )
                    )
                }

            }

            HttpStatusCode.UnprocessableEntity to {
                description = "Unprocessable Entity"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "Unprocessable Entity Error",
                            value = Response.Error(
                                message = "UnprocessableEntity Error.Check input data",
                                exception = null
                            )
                        )
                    )
                }

            }
        }
    }
}
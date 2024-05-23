package dev.arjundev.routes

import dev.arjundev.data.dao.user.userDao
import dev.arjundev.data.model.*
import io.github.smiley4.ktorswaggerui.data.ValueExampleDescriptor
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Routing.profileRoutes() {
    authenticate("authJWT") {
        route("api/v1/user/profile") {
            get("", {
                routeApiV1UserProfile()
            }) {
                val principal = call.principal<JWTPrincipal>()
                if (principal != null) {

                    val userID = principal["userid"]
                    /*  val username = principal?.get("username")
                      val tokentype = principal?.get("tokenType")*/
                    val user = userDao.isUserAvailable(userID!!)
                    call.respond(Response.Success(user!!.toProfile()))

                } else {
                    call.respond("")
                }

            }
            put("", {
                routeApiV1UserProfilePatch()
            }) {
                val principal = call.principal<JWTPrincipal>()
                if (principal != null) {

                    val userID = principal["userid"]


                    val user = userDao.isUserAvailable(userID!!)
                    if (user != null) {
                        val profileUpdateRequest = call.receive<ProfileUpdateRequest>()

                        val isUsernameAvailable =
                            userDao.isUsernameAvailable(userID, username = profileUpdateRequest.username)
                        val isEmailAvailable = userDao.isEmailAvailable(userID, email = profileUpdateRequest.email)
                        when {
                            isUsernameAvailable && isEmailAvailable -> {
                                userDao.updateUsernameAndEmail(
                                    userID,
                                    username = profileUpdateRequest.username,
                                    email = profileUpdateRequest.email
                                )
                                val user = userDao.isUserAvailable(userID)
                                if (user != null) {
                                    call.respond(
                                        Response.Success(
                                            data = ProfileUpdateResponse(
                                                profile = user.toProfile(),
                                                updateMessage = "email and username updated"
                                            )
                                        )
                                    )
                                }
                            }

                            isUsernameAvailable -> {
                                userDao.updateUsername(id = userID, username = profileUpdateRequest.username)
                                val user = userDao.isUserAvailable(userID)
                                if (user != null) {
                                    call.respond(
                                        Response.Success(
                                            data = ProfileUpdateResponse(
                                                profile = user.toProfile(),
                                                updateMessage = "username updated"
                                            )
                                        )
                                    )
                                }
                            }

                            isEmailAvailable -> {
                                userDao.updateEmail(id = userID, email = profileUpdateRequest.email)
                                val user = userDao.isUserAvailable(userID)
                                if (user != null) {
                                    call.respond(
                                        Response.Success(
                                            data = ProfileUpdateResponse(
                                                profile = user.toProfile(),
                                                updateMessage = "email updated"
                                            )
                                        )
                                    )
                                }
                            }

                            else -> {
                                call.respond(
                                    Response.Error(
                                        message = "Unable to update user",
                                        reasons = listOf(
                                            "username: Username already taken",
                                            "email: Email already taken"
                                        )
                                    )
                                )
                            }
                        }


                    } else {
                        call.respond(Response.Error(message = "User not available"))
                    }
                }
            }
        }
    }
}

private fun OpenApiRoute.routeApiV1UserProfilePatch() {
    run {
        tags = listOf("Profile")

        request {

            body<ProfileUpdateRequest> {
                // specify two example values
                example(
                    ValueExampleDescriptor(
                        name = "First",
                        description = "A longer description of the example",
                        summary = "User Profile Updation request",
                        value = ProfileUpdateRequest(
                            username = "Arjun", email = "example@email.com"
                        )
                    )
                )

            }

        }

        response {


            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<Response.Success<ProfileUpdateResponse>> {
                    example(
                        ValueExampleDescriptor(
                            name = "Profile",
                            description = "You will get profile data and updateMessage",
                            summary = "User Profile",
                            value = Response.Success(

                                ProfileUpdateResponse(
                                    profile = Profile(
                                        userID = "",
                                        userName = "",
                                        email = ""
                                    ), updateMessage = ""
                                )

                            )
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
                                message = "Unprocessable Entity Error.Check input data",
                                exception = null
                            )
                        )
                    )
                }

            }
            HttpStatusCode.Unauthorized to {
                description = "Unauthorized"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "UnauthorizedError",
                            summary =
                            "Username or password is invalid",
                            value = Response.Error(
                                message = "Username or password is invalid",
                                exception = null
                            )
                        )
                    )
                }

            }
        }
    }
}

private fun OpenApiRoute.routeApiV1UserProfile() {
    run {
        tags = listOf("Profile")

        request { }

        response {


            HttpStatusCode.OK to {
                description = "The operation was successful"
                body<Response.Success<Profile>> {
                    example(
                        ValueExampleDescriptor(
                            name = "Profile",
                            description = "You will get user profile",
                            summary = "User Profile",
                            value = Response.Success(Profile(userID = "", userName = "", email = ""))
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
                                message = "Unprocessable Entity Error.Check input data",
                                exception = null
                            )
                        )
                    )
                }

            }
            HttpStatusCode.Unauthorized to {
                description = "Unauthorized"

                body<Response.Error> {
                    example(
                        ValueExampleDescriptor(
                            name = "UnauthorizedError",
                            summary =
                            "Username or password is invalid",
                            value = Response.Error(
                                message = "Username or password is invalid",
                                exception = null
                            )
                        )
                    )
                }

            }
        }
    }
}
package dev.arjundev.routes

import dev.arjundev.data.dao.user.userDao
import dev.arjundev.data.model.Profile
import dev.arjundev.data.model.Response
import dev.arjundev.data.model.toProfile
import io.github.smiley4.ktorswaggerui.data.ValueExampleDescriptor
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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

                    val userID = principal?.get("userid")
                    /*  val username = principal?.get("username")
                      val tokentype = principal?.get("tokenType")*/
                    val user = userDao.isUserAvailable(userID!!)
                    call.respond(Response.Success(user!!.toProfile()))

                } else {
                    call.respond("")
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
                            description = "You will get access token and refresh token",
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
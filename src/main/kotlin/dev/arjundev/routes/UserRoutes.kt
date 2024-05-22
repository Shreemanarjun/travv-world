package dev.arjundev.routes

import dev.arjundev.data.model.Response
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.model.UserRegistrationRequest
import io.github.smiley4.ktorswaggerui.data.KTypeDescriptor
import io.github.smiley4.ktorswaggerui.data.ValueExampleDescriptor
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.typeOf

fun Routing.userRoutes() {

    route("api/v1/user") {
        ///user registration,with a username and email,password
        post("signUp", {
            run {
                tags = listOf("User")
                description = "Performs user registration with unique username,email,and password"
                request {

                    body(KTypeDescriptor(typeOf<UserRegistrationRequest>())) {
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
                        body(KTypeDescriptor(typeOf<Response.Success<String>>())) {
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

                        body(KTypeDescriptor(typeOf<Response.Error>())) {
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

                        body(KTypeDescriptor(typeOf<Response.Error>())) {
                            example(
                                ValueExampleDescriptor(
                                    name = "Internal Server Error",
                                    value = Response.Error(message = "Internal Server Error", exception = null)
                                )
                            )
                        }

                    }
                }
            }
        }) {

            ///TODO code for registration user
            call.respond(status = HttpStatusCode.Created, Response.Success(data = "User successfully created"))
        }
        //user login witth email and password
        post("login", {
            run {
                tags = listOf("User")
                description = "Performs user login with email and password"
                request {

                    body(KTypeDescriptor(typeOf<UserLoginRequest>())) {
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
                        body(KTypeDescriptor(typeOf<Response.Success<String>>())) {
                            example(
                                ValueExampleDescriptor(
                                    name = "Login Successful",

                                    value = Response.Success(data = "User logged in successfully")
                                )
                            )
                        }


                    }
                    HttpStatusCode.BadRequest to {
                        description = "Something went wrong"

                        body(KTypeDescriptor(typeOf<Response.Error>())) {
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

                        body(KTypeDescriptor(typeOf<Response.Error>())) {
                            example(
                                ValueExampleDescriptor(
                                    name = "Internal Server Error",
                                    value = Response.Error(message = "Internal Server Error", exception = null)
                                )
                            )
                        }

                    }

                    HttpStatusCode.UnprocessableEntity to {
                        description = "UnprocessableEntity"

                        body(KTypeDescriptor(typeOf<Response.Error>())) {
                            example(
                                ValueExampleDescriptor(
                                    name = "UnprocessableEntityError",
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
        }) {
            call.receive<UserLoginRequest>()
            call.respondText {
                "Success"
            }
            ///TODO Login Code checker
        }
    }
}
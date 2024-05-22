package dev.arjundev

import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.model.Response
import dev.arjundev.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureSecurity()
    ///Handle database connection gracefully
    var isDBInitialized = DatabaseFactory.init()
    when {
        isDBInitialized.isSuccess -> {
            configureRequestValidation()
            configureTemplating()
            configureHTTP()

            configureSwagger()
            configureRouting()
        }

        else -> {
            /// If any DB exception is there redirect useres to a common path and provide a rety url for retry the connection
            routing {

                get("{...}") {
                    call.respond(
                        Response.Error(
                            exception = "DB Connection Refused. } /retry ",
                            message = isDBInitialized.exceptionOrNull()?.localizedMessage
                        )
                    )
                }

            }
        }
    }


}

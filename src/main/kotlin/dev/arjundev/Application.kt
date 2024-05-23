package dev.arjundev

import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.model.Response
import dev.arjundev.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)




@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureSecurity()
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 }
    }
    ///Handle database connection gracefully
    val isDBInitialized = DatabaseFactory.init()
    when {
        isDBInitialized.isSuccess -> {
            configureRequestValidation()
            configureTemplating()
            configureHTTP()
            configureSwagger()
            configureRouting()
        }

        else -> {


            /// If any DB exception is there redirect users to a common path and provide a retry url for retry the connection
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

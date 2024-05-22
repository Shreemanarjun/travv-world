package dev.arjundev

import dev.arjundev.data.dao.DatabaseFactory
import dev.arjundev.data.model.Response
import dev.arjundev.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module, watchPaths = listOf("classes"))
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    ///Handle database connection gracefully
    var isDBInitialized = DatabaseFactory.init()
    when {
        isDBInitialized.isSuccess -> {
            configureRequestValidation()
            configureTemplating()
            configureHTTP()
            configureSecurity()
            configureSwagger()
            configureRouting()
        }

        else -> {
            /// If any DB exception is there redirect useres to a common path and provide a rety url for retry the connection
            routing {
                var retried=0

                get("/retry") {
                    isDBInitialized = DatabaseFactory.init()
                    ++retried
                    if (isDBInitialized.isFailure) {

                        call.respondRedirect("/")
                    } else {
                        call.respond(
                            Response.Success(
                                data = "Database connected"
                            )
                        )
                    }
                }
                get("/{...}") {
                    call.respond(
                        Response.Error(
                            exception = "DB Connection Refused. ${if (retried!=0 ) "Retried $retried times" else ""} /retry ",
                            message = isDBInitialized.exceptionOrNull()?.localizedMessage
                        )
                    )
                }

            }
        }
    }


}

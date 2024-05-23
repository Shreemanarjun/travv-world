package dev.arjundev.plugins

import dev.arjundev.data.model.Response
import dev.arjundev.routes.profileRoutes
import dev.arjundev.routes.userRoutes
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    install(StatusPages) {

        exception<Throwable> { call, cause ->

            when (cause) {
                is RequestValidationException -> call.respond(
                    message = Response.Error(
                        message = "Data validation failed",
                        exception = "RequestValidationException",
                        reasons = cause.reasons,
                    ), status = HttpStatusCode.UnprocessableEntity,
                )

                is BadRequestException -> call.respond(
                    message = Response.Error(
                        message = "400: Bad Request ${cause.localizedMessage} ",
                        exception = cause.toString()
                    ),
                    status = HttpStatusCode.BadRequest,
                )

                is UnsupportedMediaTypeException -> call.respond(
                    message = Response.Error(
                        message = "400: Bad Request ${cause.localizedMessage} ",
                        exception = cause.toString()
                    ),
                    status = HttpStatusCode.UnsupportedMediaType,
                )

                else -> call.respond(
                    message = Response.Error(message = "500: ${cause.localizedMessage}", exception = cause.toString()),
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
    install(AutoHeadResponse)
    routing {
        route("swagger") {
            swaggerUI("/api.json")
            route("/"){

            }
        }
        route("api.json") {
            openApiSpec()
        }

        get("/") {
            call.respondRedirect("/swagger")
        }
        userRoutes()
        profileRoutes()


        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static") {

        }
    }
}

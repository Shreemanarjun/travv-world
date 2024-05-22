package dev.arjundev.plugins




import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSort
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSyntaxHighlight
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.util.*
import kotlinx.serialization.json.Json


fun Application.configureSwagger() {
    val engineenv = (environment as ApplicationEngineEnvironment)
    val envHost = System.getenv("RAILWAY_STATIC_URL")
    val envPort = engineenv.config.port
    val engineconnectors = engineenv.connectors

    install(SwaggerUI) {

        swagger {
            url {

            }
            onlineSpecValidator()

            displayOperationId = true
            showTagFilterInput = true
            sort = SwaggerUiSort.HTTP_METHOD
            syntaxHighlight = SwaggerUiSyntaxHighlight.MONOKAI
        }
        externalDocs {
            url = "example.com"
            description = "Project documentation"
        }
        info {
            title = "Api"
            version = "v1.2"
            description = "My Api"
            termsOfService = "http://example.com/terms"
            contact {
                name = "API Support"
                url = "http://www.example.com/support"
                email = "support@example.com"
            }
            license {
                name = "Apache 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.html"
            }
        }
        if (envHost != null) {
            server {
                url = "https://${envHost}"
                description = "Development server"
            }
        }
        engineconnectors.forEach { e ->
            server {
                url = "http://${e.host}:${e.port}"
                description = "${e.type}"
            }
        }
        server {
            url = "https://${engineenv.config.host}:$envPort"
            description = "Development server"

        }




        /*  securityScheme("authJWT") {
              type = AuthType.API_KEY
              scheme = AuthScheme.BEARER
              bearerFormat = "jwt"
              location = AuthKeyLocation.HEADER

          }
          defaultSecuritySchemeName = "authJWT"

          defaultUnauthorizedResponse {
              description = "Unauthorized Access"
              body<ErrorResponse> {
                  example("Unauthorized Access", ErrorResponse(error = "Unauthorized Access"))
              }
          }*/
        val json = Json {
            prettyPrint = true
            encodeDefaults = true



        }
        /*   encoding {
               exampleEncoder { type, value ->
                   json.encodeToString(serializer(type!!), value)
               }


           }*/

    }
}
package dev.arjundev.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {

    val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
    val issuer = this@configureSecurity.environment.config.property("jwt.domain").getString()
    val secret = this@configureSecurity.environment.config.property("jwt.secret").getString()
    val myRealm = this@configureSecurity.environment.config.property("jwt.realm").getString()
    authentication {
        jwt("authJWT") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(jwtAudience)
                    .withIssuer(issuer)
                    .build()
            )
            validate {
                return@validate if ((it.payload.getClaim("username")
                        .asString() != "")&& (it.payload.getClaim("userid")
                        .asString() != "") && (it.payload.getClaim("tokenType").asString() == "accessToken")
                ) {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }

    }

}

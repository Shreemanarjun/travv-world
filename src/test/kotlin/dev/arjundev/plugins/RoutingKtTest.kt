package dev.arjundev.plugins

import io.ktor.server.testing.*
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun testGetApiHome() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

    }
}
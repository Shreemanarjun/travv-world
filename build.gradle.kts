val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val swagger_ui_version: String by project
plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    id("org.jetbrains.kotlinx.kover") version "0.8.0"
}

group = "dev.arjundev"
version = "0.0.1"

application {
    mainClass.set("dev.arjundev.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")

    //Databases
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.7.3")

    implementation("io.ktor:ktor-server-html-builder-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.10.1")
    implementation("io.ktor:ktor-server-conditional-headers-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-auto-head-response-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Swagger
    implementation("io.github.smiley4:ktor-swagger-ui:$swagger_ui_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.11")

    ///akkurate
    implementation("dev.nesk.akkurate:akkurate-core:0.7.0")
    implementation("dev.nesk.akkurate:akkurate-ksp-plugin:0.7.0")
    ksp("dev.nesk.akkurate:akkurate-ksp-plugin:0.7.0")

    //koin
    val koin_version = "3.5.6"
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")
//MOcKK
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:${mockkVersion}")

    //KTOr client
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}
ktor {
    fatJar {
        archiveFileName.set("travvworld.jar")
    }
}


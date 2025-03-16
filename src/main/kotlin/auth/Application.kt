package auth

import auth.config.DatabaseConnection
import auth.plugins.configureRouting
import auth.plugins.configureSecurity
import auth.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

    DatabaseConnection.init(environment.config)

    configureSecurity()

    configureSerialization()

    configureRouting()

}

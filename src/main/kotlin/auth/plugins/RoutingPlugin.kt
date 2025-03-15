package auth.plugins

import auth.routes.authRoutes
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing


fun Application.configureRouting() {
    routing {
        authRoutes()

        authenticate("auth-jwt") {
            get("/health") {
                call.respondText("OK")
            }
        }
    }
}

package auth.routes

import auth.dto.AuthRequest
import auth.dto.AuthResponse
import auth.service.TokenService
import auth.service.UserService
import auth.util.RsaKeyReader
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import java.util.Date


fun Route.authRoutes() {

    val userService = UserService()
    val tokenService = TokenService(application.environment.config)


    post("/register") {
        val req = call.receive<AuthRequest>()
        if (req.login.isBlank() || req.password.isBlank()) {
            call.respondText("Invalid login or password", status = HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.register(req.login, req.password)
        if (user == null) {
            call.respondText("Login already exists", status = HttpStatusCode.Conflict)
        } else {
            call.respondText("User created", status = HttpStatusCode.Created)
        }
    }

    post("/login") {
        val req = call.receive<AuthRequest>()
        val user = userService.findByLoginAndPassword(req.login, req.password)
        if (user == null) {
            call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
            return@post
        }

        val token = tokenService.generateJwtForUser(user)
        call.respond(AuthResponse(token))
    }
}

package auth.plugins

import auth.util.RsaKeyReader
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val config = environment.config
    val jwtDomain = config.property("jwt.domain").getString()
    val jwtAudience = config.property("jwt.audience").getString()
    val jwtRealm = config.property("jwt.audience").getString()

    val publicKeyPath = config.property("jwt.rsa.public-key-path").getString()

    val publicKey = RsaKeyReader.getPublicKeyFromPemFile(publicKeyPath)

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.RSA256(publicKey, null))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

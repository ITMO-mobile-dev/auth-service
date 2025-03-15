package auth.service

import auth.model.User
import auth.util.RsaKeyReader
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.Date

class TokenService(
    private val appConfig: ApplicationConfig
) {
    fun generateJwtForUser(user: User): String {
        val jwtDomain = appConfig.property("jwt.domain").getString()
        val jwtAudience = appConfig.property("jwt.audience").getString()

        val publicKeyPath = appConfig.property("jwt.rsa.public-key-path").getString()
        val privateKeyPath = appConfig.property("jwt.rsa.private-key-path").getString()

        val publicKey = RsaKeyReader.getPublicKeyFromPemFile(publicKeyPath)
        val privateKey = RsaKeyReader.getPrivateKeyFromPemFile(privateKeyPath)

        val algorithm = Algorithm.RSA256(publicKey, privateKey)

        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("userId", user.id)
            .withClaim("login", user.login)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600_000))
            .sign(algorithm)
    }
}
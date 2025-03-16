package auth.util

import java.io.FileNotFoundException
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object RsaKeyReader {
    fun getPublicKeyFromPemFile(path: String): RSAPublicKey {
        val publicKeyStream = this::class.java.classLoader.getResourceAsStream("publicKey.pem")
            ?: throw FileNotFoundException("Файл publicKey.pem не найден в ресурсах")
        val content = publicKeyStream.bufferedReader().use { it.readText() }
        return parsePublicKey(content)
    }

    fun getPrivateKeyFromPemFile(path: String): RSAPrivateKey {
        val privateKeyStream = this::class.java.classLoader.getResourceAsStream("privateKey.pem")
            ?: throw FileNotFoundException("Файл privateKey.pem не найден в ресурсах")
        val content = privateKeyStream.bufferedReader().use { it.readText() }
        return parsePrivateKey(content)
    }

    private fun parsePublicKey(pem: String): RSAPublicKey {
        val publicKeyPEM = pem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")

        val decoded = Base64.getDecoder().decode(publicKeyPEM)
        val keySpec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    private fun parsePrivateKey(pem: String): RSAPrivateKey {
        val privateKeyPEM = pem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")

        val decoded = Base64.getDecoder().decode(privateKeyPEM)
        val keySpec = PKCS8EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}
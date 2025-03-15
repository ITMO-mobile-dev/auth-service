package auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import auth.config.DatabaseConnection
import auth.model.User
import java.sql.Connection

class UserService {

    fun register(login: String, passwordPlain: String): User? {
        DatabaseConnection.getConnection().use { conn ->
            if (userExists(conn, login)) {
                return null
            }

            val bcryptHash = BCrypt.withDefaults().hashToString(12, passwordPlain.toCharArray())

            val sql = """
                INSERT INTO users (login, password_hash)
                VALUES (?, ?) RETURNING id
            """.trimIndent()

            conn.prepareStatement(sql).use { ps ->
                ps.setString(1, login)
                ps.setString(2, bcryptHash)
                val rs = ps.executeQuery()
                if (rs.next()) {
                    val newId = rs.getInt("id")
                    return User(newId, login, bcryptHash)
                }
            }
            return null
        }
    }

    fun findByLoginAndPassword(login: String, passwordPlain: String): User? {
        DatabaseConnection.getConnection().use { conn ->
            val sql = "SELECT id, login, password_hash FROM users WHERE login = ?"
            conn.prepareStatement(sql).use { ps ->
                ps.setString(1, login)
                val rs = ps.executeQuery()
                if (rs.next()) {
                    val userId = rs.getInt("id")
                    val dbLogin = rs.getString("login")
                    val dbHash = rs.getString("password_hash")

                    val verify = BCrypt.verifyer().verify(passwordPlain.toCharArray(), dbHash.toCharArray())
                    if (verify.verified) {
                        return User(userId, dbLogin, dbHash)
                    }
                }
                return null
            }
        }
    }

    private fun userExists(conn: Connection, login: String): Boolean {
        val sql = "SELECT COUNT(*) as cnt FROM users WHERE login = ?"
        conn.prepareStatement(sql).use { ps ->
            ps.setString(1, login)
            val rs = ps.executeQuery()
            if (rs.next()) {
                return rs.getLong("cnt") > 0
            }
            return false
        }
    }
}

package auth.config

import io.ktor.server.config.ApplicationConfig
import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnection {
    private lateinit var dbUrl: String
    private lateinit var dbUser: String
    private lateinit var dbPassword: String

    fun init(config: ApplicationConfig) {
        dbUrl = config.property("db.url").getString()
        dbUser = config.property("db.user").getString()
        dbPassword = config.property("db.password").getString()

        Class.forName("org.postgresql.Driver")

        createUsersTable()
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword)
    }

    private fun createUsersTable() {
        getConnection().use { conn ->
            conn.createStatement().use { st ->
                st.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        login VARCHAR(50) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}


package auth.model

data class User(
    val id: Int,
    val login: String,
    val passwordHash: String
)
package utils

import data.User

/**
 * Reads and constructs User data.
 */
class UserReaderCreator {
    private val hashirator = HashingPassword()

    /**
     * Reads a username from the console.
     *
     * @return The username as a string.
     */
    private fun readUsername(): String {
        print("Введите имя пользователя (ты кто?): ")
        return readlnOrNull() ?: ""
    }

    /**
     * Reads a password from the console and hashes it.
     *
     * @return The hashed password as a string.
     */
    private fun readPassword(): String {
        print("Введите пароль (без него тебя не пустят ваще никак): ")
        val password = readlnOrNull() ?: ""
        return hashirator.hashPassword(password)
    }

    /**
     * Reads a username and password from the console and constructs a User object.
     *
     * @return The constructed User object.
     */
    fun readUser(): User {
        val username = readUsername()
        val password = readPassword()
        val user = User(username,password)
        UserValidator().validate(user)  // Validate the user
        return user
    }
}

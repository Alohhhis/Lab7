package utils

import data.User

/**
 * Validates User data.
 */
class UserValidator {

    /**
     * Validates the given username and password.
     *
     * @param user The user whose data is to be validated.
     * @throws IllegalArgumentException If the username or password is empty.
     */
    fun validate(user: User) {
        if (user.username.isBlank()) {
            throw IllegalArgumentException("Ты без имени чтоли, серьёзно??")
        }
        if (user.password.isBlank()) {
            throw IllegalArgumentException("Без пароля ну вот ваще никак, он не может быть пустым.")
        }

    }
}

package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Messages
import data.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The LoginCommand class handles the process of user login.
 *
 * @property usersStorage The collection of users where the user's credentials will be verified.
 */
class Login: StorageCommand() {
    override val commandType = CommandType.USER_LOGIN
    override val commandArgs = listOf(CommandArgument("login", "User"))


    /**
     * Attempts to log in a user with the provided credentials.
     *
     * @param args A list of arguments where the first element should be the JSON representation of a User.
     * @param token An optional token for current logged in user.
     * @return A String message indicating the result of the login attempt.
     */
    override fun execute(args: List<Any>, token: String?): String {
        if (args.size != 1) {
            throw IllegalArgumentException("Команда входа в систему ожидает 1 аргумент, но получила ${args.size}.")
        }

        val user = args[0] as? String
            ?: throw IllegalArgumentException("Аргумент команды входа не имеет типа String.")

        val deserializedUser: User = Json.decodeFromString(user)

        val loginResult = usersStorage.login(deserializedUser.username, deserializedUser.password)
        return loginResult ?: Messages.LOGIN_FAIL
    }
}


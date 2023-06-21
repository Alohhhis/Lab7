package client

import commandArguments.CommandArgument
import commandArguments.CommandData
import commandArguments.CommandType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.CollectionReader
import utils.UserReaderCreator

/**
 * Interprets user input into commands that can be executed by the ClientApp.
 */
class CommandInterpreter : KoinComponent {
    private val collectionReader: CollectionReader by inject()
    private val clientApp: ClientApp by inject()
    private val userReader: UserReaderCreator by inject()
    private var loggedInUser: String? = null


    /**
     * Interpret user input into a command.
     *
     * @param input User input.
     * @return A pair consisting of the CommandData for the command, and a list of CommandArguments.
     * @throws IllegalArgumentException If command type is not supported or a required parameter is missing.
     * @throws IllegalStateException If a user is not logged in/out when required.
     */
    fun interpret(input: String): Pair<CommandData, List<CommandArgument>> {
        val commandParts = input.split(" ")
        val commandName = commandParts[0]
        val parameters = commandParts.drop(1)
        val commandType = findCommandType(commandName) ?: throw IllegalArgumentException("StorageCommand не найден :( ")

        val arguments = when (commandType) {
            CommandType.NO_ARG -> {
                requireLoggedIn()
                emptyList()
            }

            CommandType.SINGLE_ARG -> {
                requireLoggedIn()
                requireParameter(parameters)
                listOf(CommandArgument("arg", "String", parameters[0]))
            }

            CommandType.LABWORK_ARG -> {
                requireLoggedIn()
                val serializedLabWork = getSerializedLabWork()
                listOf(CommandArgument("vehicle", "Vehicle", serializedLabWork))
            }

            CommandType.ARG_AND_LABWORK -> {
                requireLoggedIn()
                requireParameter(parameters)
                val serializedLabWork = getSerializedLabWork()
                listOf(
                    CommandArgument("arg", "String", parameters[0]),
                    CommandArgument("vehicle", "Vehicle", serializedLabWork)
                )
            }

            CommandType.USER_REGISTRATION -> {
                requireLoggedOut()
                val serializedUser = getSerializedUser()
                listOf(CommandArgument("reg", "User", serializedUser))
            }

            CommandType.USER_LOGIN -> {
                requireLoggedOut()
                val serializedUser = getSerializedUser()
                listOf(CommandArgument("login", "User", serializedUser))
            }
            CommandType.USER_LOGOUT -> {
                requireLoggedIn()
                // Clear logged in user
                loggedInUser = null
                // Clear client token
                clientApp.token = null
                emptyList()
            }

            else -> throw IllegalArgumentException("Тип StorageCommand не поддерживается :/ ")
        }

        return Pair(CommandData(commandName, arguments, clientApp.token), arguments)
    }

    /**
     * Handles a failed login or registration attempt.
     */
    fun failedLoginOrRegistration() {
        loggedInUser = null
    }

    /**
     * Finds the CommandType for a given command name.
     *
     * @param commandName The name of the command.
     * @return The CommandType, or null if not found.
     */
    private fun findCommandType(commandName: String): CommandType? {
        return clientApp.commandList[commandName]
    }

    /**
     * Serializes a Vehicle object into a JSON string.
     *
     * @return The serialized Vehicle.
     * @throws IllegalStateException If no user is logged in.
     */
    private fun getSerializedLabWork(): String {
        // Check if a user is logged in before attempting to read lab work
        val editor = loggedInUser ?: throw IllegalStateException("Не вижу пользователя, вошедшего в систему -_-")
        val vehicle = collectionReader.readLabWork(editor)
        return Json.encodeToString(vehicle)
    }

    /**
     * Reads a user profile and serializes it into a JSON string.
     *
     * @return The serialized User.
     */
    private fun getSerializedUser(): String {
        val user = userReader.readUser()
        loggedInUser = user.username  // Store the username after reading user data
        return Json.encodeToString(user)
    }

    /**
     * Checks if a user is logged in, and throws an exception if not.
     *
     * @throws IllegalStateException If no user is logged in.
     */
    private fun requireLoggedIn() {
        if (loggedInUser == null) {
            throw IllegalStateException("Вы должны войти в систему, чтобы выполнить это действие")
        }
    }

    /**
     * Checks if a user is not logged in, and throws an exception if they are.
     *
     * @throws IllegalStateException If a user is already logged in.
     */
    private fun requireLoggedOut() {
        if (loggedInUser != null) {
            throw IllegalStateException("Вы уже вошли в систему. Пожалуйста, выйдите из системы перед повторным входом. (Выйди и зайди нормально)")
        }
    }

    /**
     * Checks if a list of parameters is not empty, and throws an exception if it is.
     *
     * @param parameters The list of parameters.
     * @throws IllegalArgumentException If the list of parameters is empty.
     */
    private fun requireParameter(parameters: List<String>) {
        if (parameters.isEmpty()) {
            throw IllegalArgumentException("Для этой команды требуется параметр, напиши чё-нибудь соответствующее..")
        }
    }
}
package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The AddCommand class adds a new lab work to the collection.
 *
 * @property collectionObjectManager The lab work collection to add the lab work to.
 */
class Add : StorageCommand() {

    override val commandType = CommandType.LABWORK_ARG
    override val commandArgs = listOf(CommandArgument("vehicle", "Vehicle"))

    override fun execute(args: List<Any>, token: String?): String {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Команда добавления ожидает 1 аргумент, но не получила ни одного, чё за дела.")
        }

        if (token == null) {
            throw IllegalArgumentException("Токен не предоставлен, так что мимо")
        }

        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Это не твой токен")

        val collectionJson = args[0] as? String
            ?: throw IllegalArgumentException("Аргумент для команды добавления не имеет типа String.")

        val vehicle = Json.decodeFromString<Vehicle>(collectionJson)
        vehicle.editor = editor
        collectionObjectManager.add(vehicle)
        return Messages.LAB_WORK_SUCCESS_ADD
    }
}

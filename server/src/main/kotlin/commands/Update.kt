package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Vehicle
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The UpdateCommand class is responsible for updating a specific lab work in the collection
 * by providing a valid ID.
 *
 * @property collectionObjectManager The lab work collection to be updated.
 */


class Update : StorageCommand() {
    override val commandType = CommandType.ARG_AND_LABWORK
    override val commandArgs = listOf(CommandArgument("id", "String"), CommandArgument("labwork", "Vehicle"))

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null || args.size < 2 || args[0] !is String) {
            return "Id, объект транспортного средства и/или токен не предоставлены или имеют неверный формат."
        }

        val id: Int = try {
            args[0].toString().toInt()
        } catch (e: NumberFormatException) {
            return "Неверный формат id. Пжлста, перевведите по-нормальному."
        }

        val collectionJson = args[1] as String
        val updatedCollection = Json.decodeFromString<Vehicle>(collectionJson)

        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Неверный токен")

        val labWorkToUpdate = collectionObjectManager.show().find { it.id == id && it.editor == editor }

        return if (labWorkToUpdate != null) {
            collectionObjectManager.update(id, updatedCollection)
            "Транспорт с ID: $id обновлен."
        } else {
            "Транспорт с id: $id, принадлежащий текущему пользователю, не найден"
        }
    }
}
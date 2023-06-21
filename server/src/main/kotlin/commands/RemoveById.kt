package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveByIdCommand class removes a lab work from the collection by providing a valid ID.
 *
 * @property collectionObjectManager The lab work collection to remove the lab work from.
 */
class RemoveById : StorageCommand() {
    override val commandType = CommandType.SINGLE_ARG
    override val commandArgs = listOf(CommandArgument("id", "String"))

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null || args.isEmpty() || args[0] !is String) {
            return "id и/или токен не предоставлены или имеют неверный формат."
        }

        val id: Int = try {
            args[0].toString().toInt()
        } catch (e: NumberFormatException) {
            return "Недопустимый формат id. Пожалуйста введите правильное число, чтобы подошло"
        }
        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Неправильный токен, импостер")

        val vehicle = collectionObjectManager.show().find { it.id == id && it.editor == editor }

        return if (vehicle != null) {
            collectionObjectManager.removeById(id)
            "Транспорт успешно удалён из коллекции."
        } else {
            "Транспорт с указанным идентификатором, принадлежащим текущему пользователю, не найден"
        }
    }
}
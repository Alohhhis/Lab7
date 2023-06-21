package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveFirstCommand class removes the first element in the lab work collection.
 *
 * @property collectionObjectManager The lab work collection to remove the first element from.
 */
class RemoveFirst : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null) {
            throw IllegalArgumentException("Токен не предоставлен.")
        }

        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Неправильный токен, ты импостер")

        collectionObjectManager.removeFirst(editor)
        return "Первый элемент успешно удален."
    }

}

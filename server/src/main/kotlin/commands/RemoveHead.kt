package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveHeadCommand class removes and returns the first element in the lab work collection.
 *
 * @property collectionObjectManager The lab work collection to remove the first element from.
 */
class RemoveHead : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null) {
            throw IllegalArgumentException("Токен не предоставлен.")
        }

        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Неправильный токен, ты импостер")

        return collectionObjectManager.removeHead(editor).toString()
    }

}

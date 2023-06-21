package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Messages

/**
 * The ClearCommand class clears the lab work collection.
 *
 * @property collectionObjectManager The lab work collection to be cleared.
 */
class Clear : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null) {
            throw IllegalArgumentException("Токен не предоставлен")
        }

        val editor = usersStorage.validateToken(token)
            ?: throw IllegalArgumentException("Неправильный какой-то у тебя токен")

        collectionObjectManager.clear(editor)
        return Messages.LAB_WORK_SUCCESS_CLEAR
    }

}

package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The ShowCommand class displays all lab works in the collection.
 *
 * @property collectionObjectManager The lab work collection to be displayed.
 */
class Show : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return collectionObjectManager.show().joinToString(separator = "\n")
    }
}

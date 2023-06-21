package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The InfoCommand class displays information about the lab work collection.
 *
 * @property collectionObjectManager The lab work collection to get information about.
 */
class Info : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return collectionObjectManager.getInfo()
    }
}

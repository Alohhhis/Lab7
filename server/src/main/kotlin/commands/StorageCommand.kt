package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.CollectionObjectManager
import utils.UsersStorage

abstract class StorageCommand: KoinComponent {
    val collectionObjectManager: CollectionObjectManager by inject()
    val usersStorage: UsersStorage by inject()

    abstract val commandType: CommandType
    abstract val commandArgs: List<CommandArgument>

    abstract fun execute(args: List<Any>, token: String? = null): String
}

package utils

import commands.*
import commandsForRegAndLogIn.Logout
import java.util.*

/**
 * Class responsible for managing and executing commands.
 *
 */
class CommandExecutor {
    private val commandMap: MutableMap<String, StorageCommand> by lazy {
        mutableMapOf<String, StorageCommand>().apply {
            this["help"] = Help(this@CommandExecutor)
            this["info"] = Info()
            this["show"] = Show()
            this["add"] = Add()
            this["update"] = Update()
            this["remove_by_id"] = RemoveById()
            this["clear"] = Clear()
            this["remove_first"] = RemoveFirst()
            this["remove_head"] = RemoveHead()
            this["add_if_max"] = AddIfMax()
            this["register"] = Register()
            this["login"] = Login()
            this["logout"] = Logout()
        }
    }


    /**
     * Retrieves a command instance by its name.
     *
     * @param name The name of the command to retrieve.
     * @return The command instance if found, null otherwise.
     */
    fun getCommand(name: String): StorageCommand? {
        return commandMap[name.lowercase(Locale.getDefault())]
    }

    /**
     * Retrieves the available commands.
     *
     * @return A map of command names to their corresponding [StorageCommand] instances.
     */
    fun getAvailableCommands(): Map<String, StorageCommand> {
        return commandMap
    }
}

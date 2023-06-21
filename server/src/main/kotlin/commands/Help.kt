package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import utils.CommandExecutor

/**
 * The HelpCommand class displays help information for all available commands.
 *
 * @property commandExecutor The command executor used to get the list of available commands.
 */
class Help(private val commandExecutor: CommandExecutor) : StorageCommand() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    private val commandDescriptions = mapOf(
        "help" to "help : выдаёт краткую справку по доступным командам",
        "info" to "info : выдаёт какую-то базочку насчёт коллекицю, когда как и кем",
        "show" to "show : показывает коллекцию (её содержимое)",
        "add" to "add : добавляет новый вид транспорта в коллекцию",
        "update" to "update : обновить значение элемента в коллекции, id которого равен заданному",
        "remove_by_id" to "remove_by_id id : удаляет элемент коллекции по заданному id",
        "clear" to "clear : подчищает всю колекцию, чтобы сверкала, как новенькая",
        "save" to "save : сохраняет коллекцию в файл (ну не надо уже ведь такого)",
        "execute_script" to "execute_script file_name : прочитать и выполнить скрипт из указанного файла. Скрипт содержит команды в том виде, в каком их вводит пользователь в интерактивном режиме. Но тебе такую команде нельзя выполнять.",
        "exit" to "exit : выходишь, ничё не сохраняя",
        "remove_first" to "remove_first : удалить первый элемент из коллекции",
        "remove_head" to "remove_head : отображает первый элемент коллекции и удаляет его",
        "add_if_max" to "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение самого большого элемента в этой коллекции, новый босс",
        "login" to "login : вход в систему (авторизацией ещё называют)",
        "register" to "register : создание нового пользователя, его регистрация",
        "logout" to "logout : выход из системы, после этого вы не пользователь, а просто прохожий"
    )

    override fun execute(args: List<Any>, token: String?): String {
        val availableCommands = commandExecutor.getAvailableCommands()
        val helpText = StringBuilder()

        availableCommands.forEach { (commandName, _) ->
            helpText.append(commandDescriptions[commandName] ?: "Это что?..: $commandName я такой команды не знаю")
            helpText.append("\n")
        }

        return helpText.toString()
    }
}

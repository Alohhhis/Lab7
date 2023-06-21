package client

import data.Messages
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommandProcessor : KoinComponent {

    private val clientApp: ClientApp by inject()
    private val commandInterpreter: CommandInterpreter by inject()

    /**
     * Start processing user input commands
     */
    fun start() {
        clientApp.setCommandInterpreter(commandInterpreter)
        clientApp.connect()

        println(Messages.WELCOME)
        println(Messages.ENTER_HELP)

        while (true) {
            print("> ")
            val input = readlnOrNull() ?: continue
            if (input.lowercase() == "exit") {
                clientApp.disconnect()
                break
            }

            try {
                val (commandData, _) = commandInterpreter.interpret(input)

                if (commandData.commandName != "register" && commandData.commandName != "login" && clientApp.token == null) {
                    println("Прежде чем, ты тут шаманить начнёшь, войди в систему >:( ")
                    continue
                }

                val task = Running(commandData)

                commandData.arguments.forEach { argument ->
                    if (argument.value.isNullOrEmpty()) {
                        print("Enter ${argument.name} (${argument.type}): ")
                        argument.value = readlnOrNull()
                    }
                }

                val response = task.execute(clientApp)
                println(response.message)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            } catch (e: IllegalStateException) {
                println(e.message)
            }
        }
        clientApp.disconnect()
    }
}

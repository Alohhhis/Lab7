package client

import commandArguments.CommandData
import commandArguments.Response

/**
 * Represents a task that can be executed by sending a command and receiving a response.
 *
 * @property commandData The data for the command to be executed.
 */
class Running(private val commandData: CommandData) {

    /**
     * Executes the command and returns the response.
     *
     * @param ClientApp The manager for the client that will execute the command.
     * @return The response from the execution of the command.
     */
    fun execute(clientApp: ClientApp): Response {
        clientApp.sendCommand(commandData)
        return clientApp.receiveResponse()
    }
}


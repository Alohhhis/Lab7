package client

import commandArguments.CommandData
import commandArguments.CommandType
import commandArguments.Response
import data.Messages
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import java.net.Socket

class ClientApp(private val host: String, private val port: Int) {
    private val socket: Socket by lazy { Socket(host, port) }
    private val reader: BufferedReader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
    private val writer: PrintWriter by lazy { PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream()))) }
    var commandList: Map<String, CommandType> = emptyMap()
    var token: String? = null
    private var commandInterpreter: CommandInterpreter? = null

    /**
     * Connects to the server
     * Tries to connect in a loop until successful
     */
    fun connect() {
        while (true) {
            try {
                val commandListJson: String = reader.readLine() ?: "{}"
                commandList = Json.decodeFromString(commandListJson)
                break
            } catch (e: IOException) {
                println("Не удается подключиться к серверу. Повторите попытку через 10 секунд...")
                Thread.sleep(10000)
            }
        }
    }

    /**
     * Disconnects from the server
     * Closes reader, writer, and socket
     */
    fun disconnect() {
        reader.close()
        writer.close()
        socket.close()
    }

    /**
     * Sends a command to the server
     * @param commandData StorageCommand data to send
     */
    fun sendCommand(commandData: CommandData) {
        val commandWithArgs = commandData.copy(arguments = commandData.arguments.map { it.copy(value = it.value) })
        val serializedCommand = Json.encodeToString(commandWithArgs)
        writer.println(serializedCommand)
        writer.flush()
    }

    /**
     * Receives a response from the server
     * @return Response from the server
     * @throws IllegalStateException if no response was received
     */
    fun receiveResponse(): Response {
        val serializedResponse = reader.readLine()
        if (serializedResponse.isNullOrBlank()) {
            throw IllegalStateException("Ответа от сервера не получено.")
        }
        val response = Json.decodeFromString<Response>(serializedResponse)

        // Handle the token for a successful login.
        if (response.message.startsWith("Успешный вход, пупупуууу, ваш токен: ")) {
            val token = response.message.substringAfter("Успешный вход, пупупуууу, ваш токен: ")
            // Notify the CommandInterpreter about the received token.
            receiveToken(token)
        } else if (response.message == Messages.LOGIN_FAIL || response.message == Messages.REGISTRATION_FAIL || response.message == Messages.REGISTRATION_SUCCESS) {
            // Notify the CommandInterpreter about the failed login or registration.
            failedLoginOrRegistration()
        }

        return response
    }

    /**
     * Sets command interpreter
     * @param commandInterpreter StorageCommand interpreter to set
     */
    fun setCommandInterpreter(commandInterpreter: CommandInterpreter) {
        this.commandInterpreter = commandInterpreter
    }

    private fun receiveToken(token: String) {
        this.token = token
    }

    private fun failedLoginOrRegistration() {
        commandInterpreter?.failedLoginOrRegistration()
        this.token = null
    }
}

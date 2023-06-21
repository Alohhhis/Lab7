package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Vehicle
import data.Messages
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The AddIfMaxCommand class adds a new lab work to the collection if its value is greater than the largest
 * element in the collection.
 *
 * @property collectionObjectManager The lab work collection to add the lab work to.
 */
class AddIfMax: StorageCommand() {
    override val commandType = CommandType.LABWORK_ARG
    override val commandArgs = listOf(CommandArgument("vehicle", "Vehicle"))

    override fun execute(args: List<Any>, token: String?): String {
        val collectionJson = args[0] as String
        val vehicle = Json.decodeFromString<Vehicle>(collectionJson)
        vehicle.editor = token!!
        val added = collectionObjectManager.addIfMax(vehicle)
        return if (added) Messages.LAB_WORK_SUCCESS_ADD else Messages.LAB_WORK_NOT_MAX
    }
}
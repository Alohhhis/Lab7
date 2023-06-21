package utils

import data.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.ConcurrentSkipListSet
import java.time.LocalDate

/**
 * This LabWorkService class represents the service for managing lab work objects.
 *
 * @property dataBaseStorage An instance of [DataBaseStorage] for managing lab works in the database.
 * @property vehicleCollection A thread-safe set of lab work objects.
 */
class CollectionObjectManager : KoinComponent {

    private val dataBaseStorage: DataBaseStorage by inject()

    private val vehicleCollection: ConcurrentSkipListSet<Vehicle> = ConcurrentSkipListSet()

    /**
     * Load lab works from the repository to the concurrent set during the service initialization.
     */
    init {
        vehicleCollection.addAll(dataBaseStorage.loadLabWorks())
    }

    /**
     * Add a Vehicle instance to the lab work set and repository.
     * @param vehicle A Vehicle instance to be added.
     * @return The message indicating the result of the operation.
     */
    fun add(vehicle: Vehicle): String {
        val message = dataBaseStorage.addLabWork(vehicle)
        vehicleCollection.add(vehicle)
        return message
    }

    /**
     * Remove a Vehicle instance from the lab work set and repository by its id.
     * @param id The id of the Vehicle to be removed.
     * @return The result of the operation.
     */
    fun removeById(id: Int): Boolean {
        val isRemoved = dataBaseStorage.removeLabWorkById(id)
        if (isRemoved) {
            vehicleCollection.removeIf { it.id == id }
        }
        return isRemoved
    }

    /**
     * Clear all Vehicle instances owned by a specific editor from the lab work set and the repository.
     * @param editor The editor of the Vehicle instances to be cleared.
     */
    fun clear(editor: String) {
        dataBaseStorage.clearLabWorksByOwner(editor)
        vehicleCollection.removeIf { it.editor == editor }
    }

    /**
     * Show all Vehicle instances in the lab work set.
     * @return A list of Vehicle instances.
     */
    fun show(): List<Vehicle> {
        return vehicleCollection.toList()
    }

    /**
     * Remove the first Vehicle instance owned by a specific editor from the lab work set and the repository.
     * @param editor The editor of the Vehicle instance to be removed.
     * @return The Vehicle instance that was removed.
     */
    fun removeFirst(editor: String): Vehicle? {
        val vehicle = vehicleCollection.firstOrNull { it.editor == editor }
        if (vehicle != null) {
            dataBaseStorage.removeLabWorkById(vehicle.id)
            vehicleCollection.remove(vehicle)
        }
        return vehicle
    }

    /**
     * An alias for the removeFirst function.
     */
    fun removeHead(editor: String): Vehicle? {
        return removeFirst(editor)
    }

    /**
     * Add a Vehicle instance to the lab work set and the repository if it has the highest id among all Vehicle instances owned by the same editor.
     * @param vehicle The Vehicle instance to be potentially added.
     * @return Whether the operation was successful.
     */
    fun addIfMax(vehicle: Vehicle): Boolean {
        if (vehicleCollection.none { it.editor == vehicle.editor }) return false

        val maxLabWork = vehicleCollection.filter { it.editor == vehicle.editor }.maxWithOrNull(compareBy { it.id })
        if (maxLabWork == null || compareBy<Vehicle> { it.id }.compare(maxLabWork, vehicle) < 0) {
            add(vehicle)
            return true
        }
        return false
    }

    /**
     * Update a Vehicle instance in the lab work set and the repository.
     * The editor of the new Vehicle instance should be the same as the editor of the existing one.
     * @param id The id of the Vehicle instance to be updated.
     * @param newCollection The new Vehicle instance.
     * @return Whether the operation was successful.
     */
    fun update(id: Int, newCollection: Vehicle): Boolean {
        val vehicle = vehicleCollection.find { it.id == id }

        if (vehicle != null && vehicle.editor == newCollection.editor) {
            val updatedCollection = Vehicle(
                id = vehicle.id,
                name = newCollection.name,
                coordinates = newCollection.coordinates,
                creationDate = vehicle.creationDate,
                enginePower = newCollection.enginePower,
                distanceTravelled = newCollection.distanceTravelled,
                vehicleType = newCollection.vehicleType,
                fuelType = newCollection.fuelType,
                editor = vehicle.editor
            )

            dataBaseStorage.updateLabWork(updatedCollection)
            vehicleCollection.remove(vehicle)
            vehicleCollection.add(updatedCollection)
            return true
        }
        return false
    }

    /**
     * Get information about the lab work set, including its type, initialization date, and the number of elements.
     * @return A string containing the information about the lab work set.
     */
    fun getInfo(): String {
        return "Ну типа, коллекция типа: ${vehicleCollection::class.simpleName}\n" +
                "Была создана: ${LocalDate.now()}\n" +
                "Количество элементов: ${vehicleCollection.size}"
    }
}

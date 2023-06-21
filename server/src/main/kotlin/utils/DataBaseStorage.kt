package utils

import data.*
import databaseManager.DataBaseManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Timestamp

/**
 * DataBaseStorage LabWorkRepository class provides a set of operations over Vehicle objects.
 * It serves as a data access layer, interacting directly with the database.
 */
class DataBaseStorage: KoinComponent {

    private val databaseManager: DataBaseManager by inject()

    /**
     * Load all Vehicle instances from the database.
     * @return The list of Vehicle instances retrieved from the database.
     */
    fun loadLabWorks(): List<Vehicle> {
        val collections = mutableListOf<Vehicle>()
        val connection = databaseManager.connection
        val statement = connection?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM vehicles")

        while (resultSet?.next() == true) {
            val vehicle = Vehicle(
                id = resultSet.getInt("id"),
                name = resultSet.getString("name"),
                coordinates = Coordinates(
                    x = resultSet.getFloat("coordinate_x"),
                    y = resultSet.getDouble("coordinate_y")
                ),
                creationDate = resultSet.getTimestamp("creation_date").toLocalDateTime(),
                enginePower = resultSet.getInt("engine_power"),
                distanceTravelled = resultSet.getInt("distance_travelled"),
                vehicleType = resultSet.getString("vehicle_type")?.let { VehicleType.valueOf(it) },
                fuelType = resultSet.getString("fuel_type")?.let { FuelType.valueOf(it) }, 
                editor = resultSet.getString("editor")
            )

            collections.add(vehicle)
        }

        return collections
    }

    /**
     * Add a Vehicle instance to the database.
     * @param vehicle A Vehicle instance to be added.
     * @return The message indicating the result of the operation.
     */
    fun addLabWork(vehicle: Vehicle): String {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("""
    INSERT INTO vehicles(
        id, 
        name, 
        coordinate_x, 
        coordinate_y, 
        creation_date, 
        engine_power, 
        distance_travelled, 
        vehicle_type, 
        fuel_type,
        editor  
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
""")

        preparedStatement?.setInt(1, vehicle.id)
        preparedStatement?.setString(2, vehicle.name)
        preparedStatement?.setFloat(3, vehicle.coordinates.x)
        preparedStatement?.setDouble(4, vehicle.coordinates.y)
        preparedStatement?.setTimestamp(5, Timestamp.valueOf(vehicle.creationDate))
        preparedStatement?.setInt(6, vehicle.enginePower)
        preparedStatement?.setInt(7, vehicle.distanceTravelled)
        preparedStatement?.setString(8, vehicle.vehicleType?.toString())
        preparedStatement?.setString(9, vehicle.fuelType?.toString())
        preparedStatement?.setString(10, vehicle.editor)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        if (rowsAffected > 0) {
            return Messages.LAB_WORK_SUCCESS_ADD
        } else {
            throw IllegalStateException("Не получилось добавить транспорт в БД")
        }
    }

    /**
     * Remove a Vehicle instance from the database by its id.
     * @param id The id of the Vehicle to be removed.
     * @return The result of the operation.
     */
    fun removeLabWorkById(id: Int): Boolean {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE id = ?")

        preparedStatement?.setInt(1, id)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        return rowsAffected > 0
    }

    /**
     * Clear all Vehicle instances owned by a specific editor from the database.
     * @param editor The editor of the Vehicle instances to be cleared.
     */
    fun clearLabWorksByOwner(editor: String) {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE editor = ?")

        preparedStatement?.setString(1, editor)

        preparedStatement?.executeUpdate()
    }

    /**
     * Update a Vehicle instance in the database.
     * @param updatedCollection The new Vehicle instance.
     * @return Whether the operation was successful.
     */
    fun updateLabWork(updatedCollection: Vehicle): Boolean {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("""
            UPDATE vehicles SET 
            name = ?, 
            coordinate_x = ?, 
            coordinate_y = ?, 
            engine_power = ?, 
            distance_travelled = ?, 
            vehicle_type = ?, 
            fuel_type = ?
            WHERE id = ?;
        """)

        preparedStatement?.setString(1, updatedCollection.name)
        preparedStatement?.setFloat(2, updatedCollection.coordinates.x)
        preparedStatement?.setDouble(3, updatedCollection.coordinates.y)
        preparedStatement?.setInt(4, updatedCollection.enginePower)
        preparedStatement?.setInt(5, updatedCollection.distanceTravelled)
        preparedStatement?.setString(6, updatedCollection.vehicleType?.toString())
        preparedStatement?.setString(7, updatedCollection.fuelType?.toString())
        preparedStatement?.setInt(8, updatedCollection.id)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        return rowsAffected > 0
    }
}

package utils

import data.*
import databaseManager.DataBaseManager
import java.sql.Timestamp
import java.time.LocalDate
import java.util.concurrent.ConcurrentSkipListSet

class StorageManager {
    private val vehicleSet = ConcurrentSkipListSet<Vehicle>()
    private val dataBaseManager = DataBaseManager()

    init {
        System.err.println("Коллекция создана")
        loadFromDatabase()
    }

    private fun loadFromDatabase() {
        val connection = dataBaseManager.connection
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
                vehicleType = resultSet.getString("vehicle_type").let { VehicleType.valueOf(it) },
                fuelType = resultSet.getString("fuel_type")?.let { FuelType.valueOf(it) },
                editor = resultSet.getString("editor")
            )

            vehicleSet.add(vehicle)
        }

    }

    fun add(vehicle: Vehicle): String {

        val connection = dataBaseManager.connection
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
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
""")

        // Assign editor to vehicle object

        preparedStatement?.setInt(1, vehicle.id)
        preparedStatement?.setString(2, vehicle.name)
        preparedStatement?.setFloat(3, vehicle.coordinates.x)
        preparedStatement?.setDouble(4, vehicle.coordinates.y)
        preparedStatement?.setTimestamp(5, Timestamp.valueOf(vehicle.creationDate))
        preparedStatement?.setInt(6, vehicle.enginePower)
        preparedStatement?.setInt(7, vehicle.distanceTravelled)
        preparedStatement?.setString(8, vehicle.vehicleType.toString())
        preparedStatement?.setString(9, vehicle.fuelType.toString())
        preparedStatement?.setString(10, vehicle.editor)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        if (rowsAffected > 0) {
            vehicleSet.add(vehicle)  // Vehicle object already includes editor
            return Messages.LAB_WORK_SUCCESS_ADD
        } else {
            throw IllegalStateException("Не удалось добавить транспортное средство в базу данных :(")
        }
    }


    fun removeById(id: Int): Boolean {
        val vehicle = vehicleSet.find { it.id == id }
        if (vehicle != null) {
            val connection = dataBaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE id = ?")

            preparedStatement?.setInt(1, id)

            val rowsAffected = preparedStatement?.executeUpdate() ?: 0

            if (rowsAffected > 0) {
                return vehicleSet.removeIf { it.id == id }
            }
        }
        throw IllegalArgumentException("Не надо чужое удалять")
    }

    fun clear(editor: String) {
        val connection = dataBaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE editor = ?")

        preparedStatement?.setString(1, editor)

        preparedStatement?.executeUpdate()

        vehicleSet.removeIf { it.editor == editor }
    }


    fun show(): List<Vehicle> {
        return vehicleSet.toList()
    }

    private fun size(): Int {
        return vehicleSet.size
    }

    private fun getCreationDate(): LocalDate {
        return LocalDate.now()
    }

    fun removeFirst(editor: String) {
        val vehicle = vehicleSet.firstOrNull { it.editor == editor }
        if (vehicle != null) {
            val connection = dataBaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE id = ?")

            preparedStatement?.setInt(1, vehicle.id)

            preparedStatement?.executeUpdate()

            vehicleSet.remove(vehicle)
        } else {
            throw IllegalArgumentException("Не найдено элементов, принадлежащих предоставленному пользователю.")
        }
    }

    fun addIfMax(vehicle: Vehicle): Boolean {
        if (vehicleSet.none { it.editor == vehicle.editor }) return false

        val newVehicle = vehicleSet.filter { it.editor == vehicle.editor }.maxWithOrNull(compareBy { it.id })
        if (newVehicle == null || compareBy<Vehicle> { it.id }.compare(newVehicle, vehicle) < 0) {
            add(vehicle)  // Make sure to catch potential IllegalStateException here
            return true
        }
        return false
    }

    fun removeHead(editor: String): Vehicle {
        val vehicle = vehicleSet.firstOrNull { it.editor == editor }
        if (vehicle != null) {
            val connection = dataBaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM vehicles WHERE id = ?")

            preparedStatement?.setInt(1, vehicle.id)

            preparedStatement?.executeUpdate()

            vehicleSet.remove(vehicle)
            return vehicle
        } else {
            throw IllegalArgumentException("Не найдено элементов, принадлежащих предоставленному пользователю.")
        }
    }

    fun getInfo(): String {
        return "Ну типа, коллекция типа: ${vehicleSet::class.simpleName}\n" +
                "была создана: ${getCreationDate()}\n" +
                "количество элементов: ${size()}"
    }

    fun update(id: Int, newVehicle: Vehicle): Boolean {
        val vehicle = vehicleSet.find { it.id == id }

        if (vehicle != null && vehicle.editor == newVehicle.editor) {
            val updatedVehicle = Vehicle(
                id = vehicle.id,
                name = newVehicle.name,
                coordinates = newVehicle.coordinates,
                creationDate = vehicle.creationDate,
                enginePower = newVehicle.enginePower,
                distanceTravelled = newVehicle.distanceTravelled,
                vehicleType = newVehicle.vehicleType,
                fuelType = newVehicle.fuelType,
                editor = vehicle.editor
            )

            val connection = dataBaseManager.connection
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

            preparedStatement?.setString(1, updatedVehicle.name)
            preparedStatement?.setFloat(2, updatedVehicle.coordinates.x)
            preparedStatement?.setDouble(3, updatedVehicle.coordinates.y)
            preparedStatement?.setInt(4, updatedVehicle.enginePower)
            preparedStatement?.setInt(5, updatedVehicle.distanceTravelled)
            preparedStatement?.setString(6, updatedVehicle.vehicleType.toString())
            preparedStatement?.setString(7, updatedVehicle.fuelType.toString())
            preparedStatement?.setInt(8, updatedVehicle.id)

            val rowsAffected = preparedStatement?.executeUpdate() ?: 0

            if (rowsAffected > 0) {
                vehicleSet.remove(vehicle)
                vehicleSet.add(updatedVehicle)
                return true
            }
        }
        throw IllegalArgumentException("Не могу вам позволить обновить что-то, что создали не вы")
    }

}

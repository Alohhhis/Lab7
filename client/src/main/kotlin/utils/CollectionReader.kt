package utils

import data.*
import exeptions.ValidationException
import java.time.LocalDateTime
import java.util.*
import javax.xml.validation.Validator

/**
 * A class for reading Vehicle instances from user input.
 *
 * @param readLineFn A function to read user input.
 * @param collectionValidator A Validator instance to validate user input.
 */
class CollectionReader(private val readLineFn: () -> String, private val collectionValidator: CollectionValidator) {

    /**
     * Reads and validates the name of a Vehicle object from user input.
     *
     * @return A valid Vehicle name.
     */
    private fun readName(): String {
        while (true) {
            print(Messages.ENTER_NAME)
            val name = readLineFn()
            try {
                collectionValidator.validateName(name)
                return name
            } catch (e: ValidationException) {
                println(e.message)
            }
        }
    }

    /**
     * Reads and validates the coordinates of a Vehicle object from user input.
     *
     * @return A valid Coordinates object.
     */
    private fun readCoordinates(): Coordinates {
        while (true) {
            try {
                print(Messages.ENTER_X)
                val x = readLineFn().trim().toFloat()
                print(Messages.ENTER_Y)
                val y = readLineFn().trim().toDouble()
                val coordinates = Coordinates(x, y)
                collectionValidator.validateCoordinates(coordinates)
                return coordinates
            } catch (e: NumberFormatException) {
                println(Messages.INVALID_COORDINATES)
            } catch (e: ValidationException) {
                println(e.message)
            }
        }
    }

    /**
     * Reads and validates the minimal point of a Vehicle object from user input.
     *
     * @return A valid minimal point value.
     */
    private fun readEnginePower(): Int {
        return readInt(Messages.ENTER_ENGINE_POWER, readLineFn, collectionValidator::validateEnginePower)
    }

    private fun readDistanceTravelled(): Int {
        return readInt(Messages.ENTER_DISTANCE_TRAVELLED, readLineFn, collectionValidator::validateDistanceTravelled)
    }
    /**
     * Reads and validates the vehicleType of a Vehicle object from user input.
     *
     * @return A valid Difficulty object or null if not specified.
     */
    private fun readVehicleType(): VehicleType? {
        print(Messages.ENTER_VEHICLE_TYPE)
        while (true) {
            val input = readLineFn().trim()
            if (input.isEmpty()) {
                return null
            }

            return try {
                VehicleType.valueOf(input.uppercase(Locale.getDefault()))
            } catch (e: IllegalArgumentException) {
                println(Messages.INVALID_VEHICLE_TYPE)
                continue
            }
        }
    }

    private fun readFuelType(): FuelType? {
        print(Messages.ENTER_FUEL_TYPE)
        while (true) {
            val input = readLineFn().trim()
            if (input.isEmpty()) {
                return null
            }

            return try {
                FuelType.valueOf(input.uppercase(Locale.getDefault()))
            } catch (e: IllegalArgumentException) {
                println(Messages.INVALID_FUEL_TYPE)
                continue
            }
        }
    }
    /**
     * Reads a Vehicle object from user input.
     *
     * @param id The unique identifier for the Vehicle object (default: generated by IdGenerator).
     * @param creationDate The creation date for the Vehicle object (default: current date and time).
     * @return The created Vehicle object.
     */
    fun readLabWork(
        editor: String,
        id: Int = IdGenerator.generateUniqueId(),
        creationDate: LocalDateTime = LocalDateTime.now()
    ): Vehicle {
        val name = readName()
        val coordinates = readCoordinates()
        val enginePower = readEnginePower()
        val distanceTravelled = readDistanceTravelled()
        val vehicleType = readVehicleType()
        val fuelType = readFuelType()
        return Vehicle(
            id = id,
            name = name,
            coordinates = coordinates,
            creationDate = creationDate,
            enginePower = enginePower,
            distanceTravelled = distanceTravelled,
            vehicleType = vehicleType,
            fuelType = fuelType,
            editor = editor
        )
    }

    /**
     * Reads and validates an integer from user input using the provided prompt and validation function.
     *
     * @param prompt The prompt message to display before reading input.
     * @param readLineFn A function to read user input.
     * @param validation A function to validate the input value.
     * @return A valid integer value.
     */
    private fun readInt(prompt: String, readLineFn: () -> String, validation: (Int) -> Unit): Int {
        while (true) {
            try {
                print(prompt)
                val value = readLineFn().toInt()
                validation(value)
                return value
            } catch (e: NumberFormatException) {
                println("Чё-та не то написал, перепроверь и введи нормальное значение: ")
            } catch (e: ValidationException) {
                println(e.message)
            }
        }
    }

}
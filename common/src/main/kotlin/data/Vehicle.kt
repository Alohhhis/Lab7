package data

import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import utils.LocalDateTimeSerializer

/**
 * A data class representing a lab work with various attributes.
 *
 * @property id The unique ID of the lab work.
 * @property name The name of the lab work.
 * @property coordinates The coordinates of the lab work.
 * @property creationDate The creation date of the lab work.
 * @property enginePower The minimal point of the lab work.
 * @property distanceTravelled The minimum personal qualities value.
 * @property vehicleType The vehicleType level of the lab work.
 * @property fuelType The fuelType associated with the lab work.
 */
@Serializable
data class Vehicle(
    var id: Int,
    val name: String, //Поле не может быть null, Строка не может быть пустой
    val coordinates: Coordinates,//Поле не может быть null
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationDate: LocalDateTime,//Значение поля должно быть больше 0
    val enginePower: Int,//Значение поля должно быть больше 0
    val distanceTravelled: Int,//Поле не может быть null, Значение поля должно быть больше 0
    val vehicleType: VehicleType?,//Поле может быть null
    val fuelType: FuelType?, //Поле не может быть null
    var editor: String
) : Comparable<Vehicle>, java.io.Serializable {

    /**
     * Compares this Vehicle object with another Vehicle object by their IDs.
     *
     * @param other The Vehicle object to compare with.
     * @return A negative value if this object is less than the other object, 0 if they are equal, and a positive value if this object is greater than the other object.
     */
    override fun compareTo(other: Vehicle): Int {
        return id.compareTo(other.id)
    }
}





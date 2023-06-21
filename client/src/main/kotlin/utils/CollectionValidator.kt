package utils

import data.Coordinates
import exeptions.ValidationException

/**
 * Class responsible for validating the input data for Vehicle instances.
 */
class CollectionValidator {
    /**
     * Validates the name of a Vehicle instance.
     *
     * @param name The name to validate.
     * @throws ValidationException If the name is empty or null.
     */
    fun validateName(name: String?) {
        if (name.isNullOrBlank()) {
            throw ValidationException("Название не может быть пустым, как различать то будем?!")
        }
    }
    /**
     *
     * Validates the coordinates of a Vehicle instance.
     * @param coordinates The coordinates to validate.
     * @throws ValidationException If the coordinates are null or the X value is greater than 608.
     */
    fun validateCoordinates(coordinates: Coordinates?) {
        if (coordinates == null || coordinates.y > 274) {
            throw ValidationException("значение координаты Y не может быть больше 274, мне так птичка напела, переделывай")
        }
    }

    /**

     * Validates the minimal point value of a Vehicle instance.
     * @param enginePower The minimal point value to validate.
     * @throws ValidationException If the minimal point value is null or less than or equal to 0.
     */
    fun validateEnginePower(enginePower: Int?) {
        if (enginePower == null || enginePower <= 0) {
            throw ValidationException("Значение мощности двигателя должно быть больше 0, ты как ехать то собрался? ")
        }
    }

    /**

     * Validates the personal qualities minimum value of a Vehicle instance.
     * @param distanceTravelled The personal qualities minimum value to validate.
     * @throws ValidationException If the personal qualities minimum value is null or less than or equal to 0.
     */
    fun validateDistanceTravelled(distanceTravelled: Int?) {
        if (distanceTravelled == null || distanceTravelled <= -1) {
            throw ValidationException("Значение пройденного расстояния должно быть не меньше 0, ну по факту же...")
        }
    }

}

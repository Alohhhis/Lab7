package data

import kotlinx.serialization.Serializable

/**
 *Дата класс содержащий значения координат 'x' и 'y'
 */

@Serializable
data class Coordinates(
    val x: Float,
    val y: Double
)
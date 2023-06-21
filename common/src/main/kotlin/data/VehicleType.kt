package data

import kotlinx.serialization.Serializable

/**
 * Класс, который содержит типы транспортных средств
 */
@Serializable
enum class VehicleType: java.io.Serializable{
    CAR,
    SUBMARINE,
    SHIP,
    BICYCLE,
    HOVERBOARD;
}
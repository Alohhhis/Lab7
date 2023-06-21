package data

import kotlinx.serialization.Serializable

/**
 * Класс Содержащий тип топлива
 */
@Serializable
enum class FuelType {
    ELECTRICITY,
    NUCLEAR,
    PLASMA;
}
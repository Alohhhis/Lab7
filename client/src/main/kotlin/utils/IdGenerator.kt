package utils

/**
 * A utility object for generating unique IDs.
 */
object IdGenerator {
    private val generatedIds = mutableSetOf<Int>()

    /**
     * Generates a unique ID within the range of 1 to Long.MAX_VALUE.
     *
     * @return A unique ID.
     */
    fun generateUniqueId(): Int {
        var id: Int
        do {
            id = (1L..Int.MAX_VALUE).random().toInt()
        } while (generatedIds.contains(id))
        generatedIds.add(id)
        return id
    }
}


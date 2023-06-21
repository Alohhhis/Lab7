package databaseManager

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * This class manages database connections.
 */
class DataBaseManager {
    var connection: Connection? = null
        private set

    init {
        val lines = File("db_login.txt").readLines()
        val url = lines[0]
        val user = lines[1]
        val password = lines[2]

        connection = DriverManager.getConnection(url, user, password).also {
            println("Успешно подключился к БДшечкам")
        }
    }
}

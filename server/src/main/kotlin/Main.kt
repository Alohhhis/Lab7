package server

import databaseManager.DataBaseManager
import org.koin.core.context.startKoin
import org.koin.dsl.module
import server.utils.RequestHandler
import utils.*
import java.util.*

val koinModule = module {
    single { Stack<String>() }
    single { DataBaseManager() }
    single { CollectionObjectManager() }
    single { DataBaseStorage() }
    single { UsersStorage() }
    single { CommandExecutor() }
    single { RequestHandler() }
}

fun main() {
    startKoin {
        modules(koinModule)
    }

    val serverManager = ServerApp(3322)

    run {
        serverManager.startServer()
    }
}


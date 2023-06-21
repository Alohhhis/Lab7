import client.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import utils.CollectionValidator
import utils.CollectionReader
import utils.UserReaderCreator

val appModule = module {
    single { ClientApp("localhost", 3322) }
    single { CollectionReader({ readlnOrNull() ?: throw IllegalStateException("Вход не предоставлен :| ") }, get()) }
    single { CollectionValidator() }
    single { UserReaderCreator() }
    single { CommandInterpreter() }
}

fun main() {
    startKoin {
        modules(appModule)
    }
    CommandProcessor().start()
}


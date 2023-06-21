package data

/**
 * An object that holds constant message strings for user prompts and errors.
 */
object Messages {
    const val REGISTRATION_SUCCESS = "Опааа, успешная регистрация"
    const val REGISTRATION_FAIL = "Не удалось зарегистрироваться"
    const val LOGIN_FAIL = "Авторизация не пройдена, но попытка была хорошая"
    const val WELCOME = "Привет, я приложенька консольная, гуд монинг, добро пожаловать"
    const val ENTER_HELP = "Ты можешь увидеть все свои возможности (список команд), введя 'help', кстати"
    const val ENTER_NAME = "Придумай, введи имя: "
    const val ENTER_X = "Задай и введи координату x (тип Float): "
    const val ENTER_Y = "Задай и введи координату y (тип Int): "
    const val INVALID_COORDINATES = "Неверный Ввод. Пожалуйста, введи нормальные координаты."
    const val ENTER_ENGINE_POWER = "Введи мощность двигателя (int): "
    const val INVALID_ENGINE_POWER = "Неверный Ввод. Пожалуйста, введи нормальную мощность. "
    const val ENTER_VEHICLE_TYPE = "Выбери тип транспорта и введи" +
            "    (CAR,\n" +
            "    SUBMARINE,\n" +
            "    SHIP,\n" +
            "    BICYCLE,\n" +
            "    HOVERBOARD;): "
    const val INVALID_VEHICLE_TYPE = "Неверный Ввод. Выбери из перечисленного тип транспорта" +
            "   (CAR,\n" +
            "    SUBMARINE,\n" +
            "    SHIP,\n"+
            "    BICYCLE,\n" +
            "    HOVERBOARD;):"
    const val ENTER_FUEL_TYPE = "Выбери тип топлива" +
            "   (ELECTRICITY,\n" +
            "    NUCLEAR,\n" +
            "    PLASMA;): "
    const val INVALID_FUEL_TYPE = "Неверный Ввод. Выбери из перечисленного тип топлива" +
            "   (ELECTRICITY,\n" +
            "    NUCLEAR,\n" +
            "    PLASMA;)."
    const val ENTER_DISTANCE_TRAVELLED = "Пробег этой шайтан-машины: "
        //const val INVALID_NUMBER = "Invalid input. Please enter a valid number."
    const val LAB_WORK_SUCCESS_ADD = "Коллекция успешно добавлена, круто"
    const val LAB_WORK_SUCCESS_CLEAR = "Всё подчистил за вами, не благодарите"
//    const val LAB_WORK_SUCCESS_SAVE = "Vehicle collection saved successfully."
    const val LAB_WORK_NOT_MAX = "Элемент не является максимальным, так что я его не был добавил в коллекцию"
    const val LOGOUT_SUCCESS = "Пока-пока!"
}

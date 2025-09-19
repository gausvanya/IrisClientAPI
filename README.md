# IrisApiCleint

Асинхронная библиотека для взаимодействия с Telegram Iris-API на языке Kotlin

# Возможности библиботеки:
  - Получение баланса мешка бота
  - Получение истории ирисок, голд и очков доната
  - Получение актуального списка агентов ириса
  - Получение информации о аккаунте пользоваталея (дата первого появления во вселенной ириса, активность в чатах ириса, наличие в спам-базах ириса, валюты в мешке пользователя)
  - Перевод голд, ирисок и очков доната между ботом и пользователями
  - Генерация deep-links для взаимодействия с ирисом 
  - Управление доступом пользователей к мешку вашего бота
  - Настройка блокировки/разблокировки возможности переводов валюты боту 
  
    
# Как установить?  
Скачайте заранее [подготовленный JAR файл](https://github.com/gausvanya/IrisClientAPI/releases) из репозитория и используйте его в своем проекте  

# Регистрация в API:  
 - Создайте своего Telegram бота в [BotFather](https://t.me/BotFather)
 - В личные сообщения [Iris | Black Diamond](https://t.me/iris_black_bot) отправьте команду: +ирис коннект
 - Выполните требования бота для проверки и получите свой токен
 - Сбросить токен можно по команде: коннект сбросить токен @BotId


# Способы пополнения валют бота:
 - Через сгенерированные deep-links
 - Пополнить бота [без од] {число} @бот — переводит боту число ирисок.  без од —  передать без очков доната (необязательный параметр)
 - Ппополнить бота голд [без од] {число} @бот — переводит боту число золотых ирисок.  без од —  передать без очков доната (необязательный параметр)
 - Пополнить бота од {число} @бот — переводит боту число очков доната
  
# Примеры использования:
```kotlin
suspend fun main() {
    // Укажите свои botId и IrisToken
    val api = IrisApiClient(botId = 6897200170, irisToken = "cH2i6pwEqcpDWmSaEOrEaUWjfqda52Lj")

    // Получение истории голд, ирисок и очков доната
    val historyGold = api.getGoldHistory()
    val historySweets = api.getSweetsHistory()
    val historyDonateScore = api.getDonateScoreHistory()
    println(historyGold)
    println(historySweets)
    println(historyDonateScore)

    // Получение баланса мешка бота
    val balance = api.getBalance()
    println(balance)

    // Перевод голд, ирисок и очков доната в другой мешок
    api.giveGold(count = 1, userId = 12345, comment = "Тестирование голд")
    api.giveSweets(count = 1, userId = 12345, comment = "Тестирование ирисок")
    api.giveDonateScore(count = 1, userId = 12345, comment = "Тестирование очков доната")

    // Включение/выключение возможности открывать мешок бота
    api.enableOrDisablePocket(enable = true)
    api.enableOrDisablePocket(enable = false)

    // Запретить/разрешить всем переводить валюту боту
    api.enableOrDisableAllPocket(enable = false)
    api.enableOrDisableAllPocket(enable = true)

    // Запретить/разрешить конкретному пользователю переводить валюту боту
    api.allowOrDenyUserPocket(userId = 123456789, enable = false)
    api.allowOrDenyUserPocket(userId = 123456789, enable = true)

    // Получение списка действующих агентов ириса.
    val irisAgents = api.getIrisAgents()
    println(irisAgents)

    // Получение списка обновлений
    val updates = api.getUpdates()
    if (updates != null) {
        for (update in updates) println(update)
    }

    // Генерация deep-links на примере ирисок, так же поддерживаются голд и очки доната
    val goldDeepLink = api.generateDeepLink(currency = Currencies.SWEETS, count = 1, comment = "тест_ирисок")
    println(goldDeepLink)

    // Генеарция deel-link для выдачи боту прав доступа к пользовательским данным ириса
    val botPermissionsDeelLink = api.generateBotPermissionsDeepLink(
        permissions = listOf(
            BotPermissions.REG,
            BotPermissions.ACTIVITY
        )
    )
    println(botPermissionsDeelLink)

    // Получение инфорации о пользователе: Регистрация | Активность | Спам-Базы |
    val userRegistration = api.checkUserReg(userId = 12345)
    val userActivity = api.checkUserActivity(userId = 12345)
    val userSpamBases = api.checkUserSpam(userId = 12345)
    val userStars = api.checkUserStars(userId = 12345)
    val userPocket = api.checkUserPocket(userId = 12345)
    println(
        "Информация о пользователе:\n" +
                "Дата регистрации: $userRegistration\n" +
                "Актив (д|н|м|весь): ${userActivity!!.activity.day} | ${userActivity.activity.week} |" +
                " ${userActivity.activity.month} | ${userActivity.activity.total}\n\n" +
                "Наличие в спам базах ириса:\n" +
                "Спам: ${userSpamBases!!.spam.isSpam}\n" +
                "Игнор: ${userSpamBases.spam.isIgnore}" +
                "Скам: ${userSpamBases.spam.isScam}\n\n" +
                "Количество звезд: $userStars\n\n" +
                "Мешок(ириски, голд, коины): ${userPocket!!.pocket.sweets} | ${userPocket.pocket.gold} | ${userPocket.pocket.coins}"
    )
}
```

# Пример использования API биржи:  
```kotlin
suspend fun main() {
    // Создаем объект класса API
    val api = IrisTradesApi()


    // Метод получения сделок с голд на бирже, имеется параметр id
    val deals = api.getDeals()

    if (deals != null) {
        for (deal in deals) {
            val id = deal.id
            val volume = deal.volume
            val date = deal.date
            val groupId = deal.groupId
            val type = deal.type
            println("id=$id, volume=$volume, date=$date, groupId=$groupId, type=$type")
        }
    }

    // Метод получения стакана заявок биржи
    val orderBook = api.getOrderBook()

    if (orderBook != null) {
        val buys = orderBook.buy
        val sells = orderBook.sell

        for (buy in buys) {
            val price = buy.price
            val volume = buy.volume
            println("buys: id=$price, volume=$volume")

        }

        for (sell in sells) {
            val price = sell.price
            val volume = sell.volume
            println("sells: id=$price, volume=$volume")
        }
    }
}
```
  
📞 Контакт для связи: [Telegram](https://t.me/gausvanya)  
  
🆘 Чат помощи [Iris Connect](https://t.me/+AweQAYgm5hwyNjky)

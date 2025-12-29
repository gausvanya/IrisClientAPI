# IrisApiCleint

Асинхронная библиотека для взаимодействия с Telegram Iris-API на языке Kotlin

# Возможности библиботеки:
  - Получение баланса мешка бота.
  - Получение истории ирисок, голд, очков доната и NFT.
  - Получение актуального списка агентов ириса.
  - Получение информации о аккаунте пользоваталея (дата первого появления во вселенной ириса, активность в чатах ириса, наличие в спам-базах ириса, валюты в мешке пользователя).
  - Перевод голд, ирисок, очков доната и NFT между ботом и пользователями
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
 - Пополнить бота голд [без од] {число} @бот — переводит боту число золотых ирисок.  без од —  передать без очков доната (необязательный параметр)
 - Пополнить бота од {число} @бот — переводит боту число очков доната.
 - Пополнить бота тгзв {число} @бот — переводит боту число телеграм звезд.
 - Пополнить бота нфт {ид/ссылка} @бот — переводит боту указанный NFT.
 

# Примеры использования библиотеки:
```kotlin

suspend fun main() {
    // Укажите свои botId и IrisToken.
    val api = IrisApiClient(botId = 123456789, irisToken = "abcdfj")
    
    // пример с использование прокси.
    val api = IrisApiClient(
        botId = 123456789, irisToken = "abcdfj", proxyClient = ProxyBuilder.http(Url(urlString = "http://111.222.333.44:8080"))
    )

    // Получение истории голд, ирисок, очков доната и NFT.
    val historyGold = api.getGoldHistory()
    val historySweets = api.getSweetsHistory()
    val historyDonateScore = api.getDonateScoreHistory()
    val historyNFT = api.getNFTHistory()
    
    println(historyGold)
    println(historySweets)
    println(historyDonateScore)
    println(historyNFT)

    // Получение баланса мешка бота
    val balance = api.getBalance()
    println(balance)
    
    // Получение списка NFT пользователя
    val nftList = api.getNFTList()
    println(nftList)
    
    // Получение информации об NFT
    val nftInfo = api.getNFTInfo(id = 5, nftName = "LolPop-246539")
    println(nftInfo)

    // Перевод голд, ирисок и очков доната в другой мешок
    api.giveGold(count = 1, userId = 12345, comment = "Тестирование голд")
    api.giveSweets(count = 1, userId = 12345, comment = "Тестирование ирисок")
    api.giveDonateScore(count = 1, userId = 12345, comment = "Тестирование очков доната")
    api.giveTgStars(count = 1, userId = 12345, comment = "Тестирование телеграм звезд")
    api.giveNFT(id = 5, nftName = "LolPop-246539", userId = 12345, comment = "Тестирование NFT")

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
    val sweetsDeepLink = api.generateDeepLink(currency = Currencies.SWEETS, count = 1, comment = "тест_ирисок")
    println(sweetsDeepLink)

    // Генеарция deep-link для выдачи боту прав доступа к пользовательским данным ириса
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
    
    // Подача заявки на покупку ирис-голд на бирже.
    val buyTrade = api.buyTrade(1.0, 10)
    println(buyTrade)

    // Подача заявки на продажу ирис-голд на бирже.
    val sellTrade = api.sellTrade(1.0, 10)
    println(sellTrade)

    // Получить список заявок поданных на Ирис-бирже.
    val ordersTrade = api.getOrdersTrade()
    println(ordersTrade)

    // Отменить все заявки по указанной цене.
    val cancelPriceTrade = api.cancelPriceTrade(1.0)
    println(cancelPriceTrade)

    // Отменить все заявки по указанной цене.
    val cancelAllTrade = api.cancelAllTrade()
    println(ordersTrade)

    // Отменить выбранную заявку частично.
    val cancelPartPrice = api.cancelPartTrade(1, 10)
    println(ordersTrade)
}
```
  
📞 Контакт для связи: [Telegram](https://t.me/gausvanya)  
  
🆘 Чат помощи [Iris Connect](https://t.me/+AweQAYgm5hwyNjky)

# IrisApiCleint

Асинхронная библиотека для взаимодействия с Telegram Iris-API на языке Kotlin

# Возможности библиботеки:
  - Получение баланса мешка бота
  - Получение истории Ирисок и голд
  - Перевод Голд и Ирисок между ботом и пользователями
  - Управление доступом пользователей к мешку вашего бота
  - Настройка блокировки/разблокировки возможности переводов валюты боту 
  
    
# Как установить?  
Скачайте заранее [подготовленный JAR файл](https://github.com/gausvanya/IrisClientAPI/releases) из репозитория и используйте его в своем проекте  

# Регистрация в API:  
 - Создайте своего Telegram бота в [BotFather](https://t.me/BotFather)
 - В личные сообщения [Iris | Black Diamond](https://t.me/iris_black_bot) отправьте команду: +ирис коннект
 - Выполните требования бота для проверки и получите свой токен
 - Сбросить токен можно по команде: коннект сбросить токен @BotId
  
# Примеры использования:
```kotlin
suspend fun main() {
    // Укажите свои botId и IrisToken
    val api = IrisApiClient(botId = 123456789, irisToken = "exampleToken")


    // Получение истории Голд и Ирисок
    val getHistoryGold = api.goldHistory()
    println(getHistoryGold)

    val getHistorySweets = api.sweetsHistory()
    println(getHistorySweets)


    // Получение баланса мешка
    val getBalance = api.balance()
    println(getBalance)


    // Перевод Голд и Ирисок в другой мешок
    api.giveGold(count = 1, userId = 12345, comment = "Тестирование")

    api.giveSweets(count = 1, userId = 12345, comment = "Тестирование")


    // Включение/выключение возможности открывать мешок бота
    api.enableOrDisablePocket(enable = true)

    api.enableOrDisablePocket(enable = false)


    // Запретить/разрешить всем переводить валюту боту
    api.enableOrDisableAllPocket(enable = false)

    api.enableOrDisableAllPocket(enable = true)


    // Запретить/разрешить конкретному пользователю переводить валюту боту
    api.allowOrDenyUserPocket(userId = 123456789, enable = false)

    api.allowOrDenyUserPocket(userId = 123456789, enable = true)
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

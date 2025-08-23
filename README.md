# IrisApiCleint

Асинхронная библиотека для взаимодействия с Telegram Iris-API на языке Kotlin

# Возможности библиботеки:
  - Получение баланса меша
  - Получение истории ирисок и голд
  - Управление доступом пользователей к мешку вашего бота,
  - Настройка блокировки/разблокировки возможности переводов валюты  
  
    
# Как установить?  
Скачайте заранее [подготовленный JAR файл](https://github.com/gausvanya/IrisClientAPI/releases) из репозитория и используйте его в своем проекте  
  
  
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
  
📞 Контакт для связи: [Telegram](https://t.me/gausvanya)  
  
🆘 Чат помощи [Iris Connect](https://t.me/+AweQAYgm5hwyNjky)

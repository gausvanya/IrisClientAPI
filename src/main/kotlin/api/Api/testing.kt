package IrisClientAPI.Api


suspend fun main() {
    val api = IrisApiClient(
        8498110175, "1xDMqUEZJWbgHTxjVUViT2fdW67G1Nua"
    )
    api.buyTrade(1.0, 1)
    val result = api.checkUserPocket(5858412531)
    println(result!!)
}
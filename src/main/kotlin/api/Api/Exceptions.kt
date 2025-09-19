package IrisClientAPI.Api

class IrisResponseException(message: String) : Exception(message)

class CurrencyCountZeroException(message: String) : Exception(message)

class LimitCommentLengthException(message: String) : Exception(message)

class CommentNotPatternException(message: String) : Exception(message)
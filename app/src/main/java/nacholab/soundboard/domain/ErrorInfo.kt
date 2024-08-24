package nacholab.soundboard.domain

data class ErrorInfo(
    val type: Int,
    val errorBody: String? = null,
    val statusCode: Int? = null,
    val exception: Exception? = null
){
    companion object{
        const val UNKNOWN = -1
        const val NETWORK = 1
        const val PARSE = 2
        const val EMPTY = 3
    }
}
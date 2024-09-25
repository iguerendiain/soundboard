package nacholab.android.errorhandling.domain

data class ErrorInfo(
    val type: ErrorType,
    val errorBody: String? = null,
    val statusCode: Int? = null,
    val exception: Exception? = null
)
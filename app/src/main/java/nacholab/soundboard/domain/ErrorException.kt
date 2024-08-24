package nacholab.soundboard.domain

data class ErrorException(val errorInfo: ErrorInfo): Exception()
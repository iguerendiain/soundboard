package nacholab.android.errorhandling.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nacholab.android.errorhandling.R
import nacholab.android.errorhandling.domain.ErrorInfo
import nacholab.android.errorhandling.domain.ErrorType

@Composable
fun ErrorInfoDialog(
    title: String,
    errorInfo: ErrorInfo,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)?,
){
    AlertDialog(
        icon = { Icon(
            imageVector = Icons.Default.Warning,
            modifier = Modifier.size(100.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        ) },
        title = { Text(
            textAlign = TextAlign.Center,
            text = title
        ) },
        text = {
            Column {
                when (errorInfo.type){
                    ErrorType.UNKNOWN -> {
                        Text(
                            text= stringResource(id = R.string.common_error_unknown_title),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        errorInfo.exception?.message?.let { Text(text = it) }

                        if (errorInfo.errorBody?.isNotBlank() == true || errorInfo.statusCode!=null){
                            Text(
                                text = stringResource(
                                    R.string.common_error_serverresponse,
                                    errorInfo.statusCode?:-1,
                                    errorInfo.errorBody?:""
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    ErrorType.NETWORK -> {
                        Text(
                            text= stringResource(id = R.string.common_error_network),
                            textAlign = TextAlign.Center
                        )
                    }
                    ErrorType.PARSE -> {
                        Text(
                            text= stringResource(id = R.string.common_error_parse_title),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        errorInfo.exception?.message?.let { Text(text = it) }
                    }
                    ErrorType.EMPTY -> {
                        Text(
                            text= stringResource(id = R.string.common_error_empty),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.common_gotit))
            }
        },
        dismissButton = { if (onRetry!=null)
            TextButton(onClick = {
                onRetry.invoke()
                onDismiss.invoke()
            }){
                Text(text = stringResource(R.string.common_retry))
            }
        }
    )
}
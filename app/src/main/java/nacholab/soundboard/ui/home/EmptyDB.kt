package nacholab.soundboard.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nacholab.soundboard.R
import nacholab.soundboard.domain.MainState

@Composable
fun EmptyDB(
    loadingState: Int,
    modifier: Modifier = Modifier.fillMaxSize(),
    onRefreshRequested: () -> Unit)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        if (loadingState == MainState.LOADING_STATE_LOADING) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp)
            )
        } else {
            Text(stringResource(id = R.string.home_empty))
            Button(onClick = onRefreshRequested) {
                Text(stringResource(id = R.string.home_refresh))
            }
        }
    }
}
package nacholab.soundboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import nacholab.soundboard.domain.MainViewModel
import nacholab.soundboard.ui.home.HomeScreen
import nacholab.soundboard.ui.theme.NachoLabsSoundBoardTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.refreshAudioclips()

        setContent {
            NachoLabsSoundBoardTheme {
                HomeScreen(mainViewModel)
            }
        }
    }

    override fun onPause() {
        mainViewModel.stopAudioClipPlayback()
        super.onPause()
    }
}

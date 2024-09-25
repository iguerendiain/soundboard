package nacholab.soundboard

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import nacholab.soundboard.domain.AudioClip
import nacholab.soundboard.domain.MainViewModel
import org.junit.Rule
import org.junit.Test

class MainViewModelUnitTest {
    private val fakeAudioClipList = listOf(
        AudioClip("sample_To_be_converted_to_correct_string.mp3"),
        AudioClip("OtherThing.mp3"),
        AudioClip("AnotherOneBitesTheDust.mp3"),
        AudioClip("Bravo.mp3"),
        AudioClip("OhNoAnyway.mp3"),
        AudioClip("Something_something_something_darkside.mp3"),
        AudioClip("OtherThing.mp3")
    )

    private val mainViewModel = MainViewModel(
        mainRepository = FakeMainRepository(fakeAudioClipList),
        mediaPlayer = null
    )

    @get:Rule val dispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun selectAudioClipTest() = runTest {
        val audioClipToBeSelected = fakeAudioClipList[0]

        launch {
            mainViewModel.selectAudioClip(audioClipToBeSelected)
        }

        advanceUntilIdle()
        val selectedAudioClip = mainViewModel.state.value.selectedAudioClip
        assert(selectedAudioClip == fakeAudioClipList[0])
    }

    @Test
    fun audioClipNameBuildTest(){
        val audioClipToBeSelected = fakeAudioClipList[0]
        val builtName = audioClipToBeSelected.buildAudioName()
        assert(builtName == "Sample To Be Converted To Correct String")
    }

    @Test
    fun audioURLBuildTest(){
        val audioClipToBeSelected = fakeAudioClipList[0]
        val builtURL = audioClipToBeSelected.buildAudioURL()
        assert(builtURL == "https://audioclips.nacholab.net/audioclips/${audioClipToBeSelected.name}")
    }
}
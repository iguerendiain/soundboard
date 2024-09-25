package nacholab.soundboard.domain

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.room.Entity
import androidx.room.PrimaryKey
import nacholab.soundboard.BuildConfig

@Entity
data class AudioClip(@PrimaryKey val name: String){
    fun buildAudioURL(): String {
        return "${BuildConfig.API_URL}/audioclips/$name"
    }

    fun buildAudioName(): String {
        val nameParts = name.split(".")
        return nameParts
            .subList(0, nameParts.size - 1)
            .joinToString(".")
            .split("_")
            .joinToString(" ") { it.capitalize(Locale.current) }
    }
}
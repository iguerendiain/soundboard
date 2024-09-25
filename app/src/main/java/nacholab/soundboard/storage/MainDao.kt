package nacholab.soundboard.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nacholab.soundboard.domain.AudioClip

@Dao
interface MainDao {
    @Query("select * from audioclip order by name")
    fun getAudioClips(): List<AudioClip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeAudioClips(audioClips: List<AudioClip>)

    @Query("delete from audioclip")
    fun clearAudioClips()
}
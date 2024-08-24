package nacholab.soundboard.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import nacholab.soundboard.model.AudioClip

@Database(entities = [AudioClip::class], version = 1)
abstract class MainDB : RoomDatabase() {
    abstract fun mainDao(): MainDao
}
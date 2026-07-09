package pl.kozaps.movy.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/movy.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
}

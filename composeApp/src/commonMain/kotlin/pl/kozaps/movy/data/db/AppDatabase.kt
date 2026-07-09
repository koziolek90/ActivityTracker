package pl.kozaps.movy.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import kotlin.time.Instant
import pl.kozaps.movy.data.dao.ActivityDao
import pl.kozaps.movy.data.model.ActivityRecord
import pl.kozaps.movy.domain.model.ActivityType

@Database(entities = [ActivityRecord::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}

// The following is required by Room 2.7.0+ for KMP
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }

    @TypeConverter
    fun fromActivityType(value: String?): ActivityType? {
        return value?.let { ActivityType.valueOf(it) }
    }

    @TypeConverter
    fun activityTypeToString(type: ActivityType?): String? {
        return type?.name
    }
}

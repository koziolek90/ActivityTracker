package pl.kozaps.movy.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.kozaps.movy.data.model.ActivityRecord

@Dao
interface ActivityDao {
    @Query("SELECT * FROM ActivityRecord ORDER BY startTime DESC LIMIT 20")
    fun getAllActivities(): Flow<List<ActivityRecord>>

    @Query("SELECT * FROM ActivityRecord WHERE startTime >= :since")
    fun getActivitiesSince(since: kotlin.time.Instant): Flow<List<ActivityRecord>>

    @Query("SELECT * FROM ActivityRecord ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastActivity(): ActivityRecord?

    @Insert
    suspend fun insert(record: ActivityRecord)

    @Update
    suspend fun update(record: ActivityRecord)
}

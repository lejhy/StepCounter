package strathclyde.emb15144.stepcounter.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DayDao {
    @Insert
    fun insert(day: Day)
    @Update
    fun update(day: Day)
    @Query("UPDATE days_table SET steps = steps + :stepsToAdd WHERE id IN(SELECT id FROM days_table ORDER BY date DESC LIMIT 1)")
    fun addLatestSteps(stepsToAdd: Int)
    @Query("SELECT * FROM days_table WHERE id = :key")
    fun get(key: Long): Day?
    @Query("SELECT * FROM days_table ORDER BY date DESC")
    fun getAllObservable(): LiveData<List<Day>>
    @Query("SELECT * FROM days_table ORDER BY date DESC LIMIT 1")
    fun getLatest(): Day
    @Query("SELECT * FROM days_table ORDER BY date DESC LIMIT 1")
    fun getLatestObservable(): LiveData<Day>
    @Query("DELETE FROM days_table WHERE id NOT IN(SELECT id FROM days_table ORDER BY date DESC LIMIT 1)")
    fun deleteAllButLatest()
}


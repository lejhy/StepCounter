package strathclyde.emb15144.stepcounter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDao {
    @Insert
    fun insert(goal: Goal)
    @Update
    fun update(goal: Goal)
    @Query("SELECT * FROM goals_table WHERE id = :key")
    fun get(key: Long): Goal?
    @Query("SELECT * FROM goals_table")
    fun getAll(): LiveData<List<Goal>>
}

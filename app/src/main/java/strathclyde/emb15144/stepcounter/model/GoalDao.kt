package strathclyde.emb15144.stepcounter.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GoalDao {
    @Insert
    fun insert(goal: Goal)
    @Update
    fun update(goal: Goal)
    @Delete
    fun delete(goal: Goal)
    @Query("SELECT * FROM goals_table WHERE id = :key")
    fun get(key: Long): Goal?
    @Query("SELECT * FROM goals_table WHERE name = :name")
    fun get(name: String): Goal?
    @Query("SELECT * FROM goals_table")
    fun getAllObservable(): LiveData<List<Goal>>
}

package strathclyde.emb15144.stepcounter.model

import android.database.Cursor
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

    @Query("SELECT * FROM goals_table")
    fun getAllCursor(): Cursor

    @Query("SELECT * FROM goals_table")
    fun getAll(): List<Goal>

    @Query("SELECT * FROM goals_table WHERE id = :key")
    fun getCursor(key: Long): Cursor
}

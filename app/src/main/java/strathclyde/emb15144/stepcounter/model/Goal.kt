package strathclyde.emb15144.stepcounter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals_table")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String = "",
    var steps: Int = 0
)

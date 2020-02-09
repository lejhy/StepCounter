package strathclyde.emb15144.stepcounter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Goal::class, Day::class],
    version = 6,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val goalDao: GoalDao
    abstract val dayDao: DayDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "main_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(CALLBACK)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO goals_table VALUES (NULL, 'Default Goal', 2000)")
                db.execSQL("INSERT INTO days_table VALUES (NULL, date(), 0, 1, 'Default Goal', 2000)")
            }
        }
    }
}

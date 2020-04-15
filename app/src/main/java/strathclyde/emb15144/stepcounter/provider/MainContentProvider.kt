package strathclyde.emb15144.stepcounter.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.model.MainDatabase

class MainContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("strathclyde.emb15144.stepcounter.provider", "goals", 1)
        addURI("strathclyde.emb15144.stepcounter.provider", "today", 2)
        addURI("strathclyde.emb15144.stepcounter.provider", "history", 3)
        addURI("strathclyde.emb15144.stepcounter.provider", "progress", 4)
        addURI("strathclyde.emb15144.stepcounter.provider", "week", 5)
        addURI("strathclyde.emb15144.stepcounter.provider", "next_unsatisfied_goal", 6)
    }

    private lateinit var datasource: MainDatabase

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            1 -> datasource.goalDao.getAllCursor()
            2 -> datasource.dayDao.getLatestCursor()
            3 -> datasource.dayDao.getAllCursor()
            4 -> {
                val today = datasource.dayDao.getLatest()
                val cursor = MatrixCursor(arrayOf("value", "max"))
                cursor.addRow(arrayOf(today.steps, today.goal_steps))
                cursor
            }
            5 -> {

                val cursor = datasource.dayDao.getAllCursor()
                val return_cursor = MatrixCursor(cursor.columnNames,7)
                cursor.moveToFirst()
                for(i in 0..6){
                    return_cursor.addRow(listOf(cursor.getLong(0),cursor.getString(1),cursor.getInt(2),cursor.getLong(3),cursor.getString(4),cursor.getInt(5)))
                    if(!cursor.moveToNext())
                        break
                }
                return_cursor
            }
            6 -> {
                return datasource.goalDao.getCursor(getClosestGoal(datasource).id)
            }
            else -> null
        }
    }

    companion object {
        fun getClosestGoal(datasource: MainDatabase): Goal {

            val day = datasource.dayDao.getLatest()
            val goals = datasource.goalDao.getAll()


            var closest_goal = Pair(day.goal_name, day.goal_steps)
            var closest_id: Int = (day.goal_id + 1).toInt()
            val steps = day.steps

            goals.forEach { goal ->
                if ((goal.steps < steps && closest_goal.second < steps && goal.steps > closest_goal.second)
                    || (goal.steps > steps && closest_goal.second < steps)
                    || (goal.steps > steps && closest_goal.second > steps && goal.steps < closest_goal.second)
                ) {
                    closest_goal = Pair(goal.name, goal.steps)
                    closest_id = goal.id.toInt()
                }
            }

            return Goal(closest_id.toLong(), closest_goal.first, closest_goal.second)
        }
    }

    override fun onCreate(): Boolean {
        context?.let {
            datasource = MainDatabase.getInstance(it)
            return true
        }
        return false
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }


}


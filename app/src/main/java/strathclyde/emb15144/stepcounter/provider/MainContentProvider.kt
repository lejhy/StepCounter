package strathclyde.emb15144.stepcounter.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import strathclyde.emb15144.stepcounter.model.MainDatabase

class MainContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("strathclyde.emb15144.stepcounter.provider", "goals", 1)
        addURI("strathclyde.emb15144.stepcounter.provider", "state", 2)
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
        return when(uriMatcher.match(uri)) {
            1 -> datasource.goalDao.getAllCursor()
            2 -> datasource.dayDao.getLatestCursor()
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        context?.let {
            datasource = MainDatabase.getInstance(it)
            return true
        }
        return false
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}

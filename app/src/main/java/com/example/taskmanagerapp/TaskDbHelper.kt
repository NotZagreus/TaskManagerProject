import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taskmanagerapp.TaskContract

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskManager.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} (" +
                    "${TaskContract.TaskEntry._ID} INTEGER PRIMARY KEY," +
                    "${TaskContract.TaskEntry.COLUMN_CREATION_TIME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_DUE_TIME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_CREATOR_NAME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaskContract.TaskEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}
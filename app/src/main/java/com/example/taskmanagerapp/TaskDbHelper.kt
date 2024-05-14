import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taskmanagerapp.TaskContract

// Helper class to manage database creation and version management
class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database version. If you change the database schema, you must increment the database version.
        private const val DATABASE_VERSION = 1
        // Name of the database file
        private const val DATABASE_NAME = "TaskManager.db"

        // SQL statement to create the tasks table
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} (" +
                    "${TaskContract.TaskEntry._ID} INTEGER PRIMARY KEY," +
                    "${TaskContract.TaskEntry.COLUMN_CREATION_TIME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_DUE_TIME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_CREATOR_NAME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION} TEXT)"

        // SQL statement to drop the tasks table
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaskContract.TaskEntry.TABLE_NAME}"
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        // Execute the SQL statement to create the tasks table
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Execute the SQL statement to drop the tasks table
        db.execSQL(SQL_DELETE_ENTRIES)
        // Call onCreate to recreate the table
        onCreate(db)
    }
}
package com.example.ab.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ab.news.data.NewsContract.NewsEntry;

/**
 * Created by ab on 2/4/15.
 */
public class NewsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "news.db";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsEntry.COLUMN_NEWS_TITLE + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_NEWS_URL + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_NEWS_NUM_COMMENTS + " REAL NOT NULL, " +
                NewsEntry.COLUMN_NEWS_COMMENTS_URL + " TEXT, " +
                NewsEntry.COLUMN_NEWS_ORIGIN + " REAL NOT NULL);";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade
        // policy is to simply to discard the data and start over Note
        // that this only fires if you change the version number for your
        // database. It does NOT depend on the version number for your
        // application. If you want to update the schema without wiping
        // data, commenting out the next line should be your top priority
        // before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(db);

    }
}

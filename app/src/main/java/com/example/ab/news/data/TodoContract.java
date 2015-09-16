package com.example.ab.news.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by ab on 3/29/15.
 */
public class TodoContract {
    public static final String LOG_TAG = "TodoContract";

    // The "Content authority" is a name for the entire content provider,
    // similar to the relationship between a domain name and its website.
    // A convenient string to use for the content authority is the package
    // name for the app, which is guaranteed to be unique on the device.
    public static final String CONTENT_AUTHORITY = "com.example.ab.news.todo";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps
    // will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
            + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/
    // is a valid path for looking at weather data. content://com.example
    // .android.sunshine.app/givemeroot/ will fail, as the ContentProvider
    // hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_ITEMS = "todo";

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /* Inner class that defines the table contents of the location table */
    public static final class TodoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        // Table name
        public static final String TABLE_NAME = "todo";

        public static final String COLUMN_TODO_Entry = "todo_entry";
        public static final String COLUMN_TODO_ORIGIN = "todo_origin";

        public static Uri buildLocationUri(long id) {
            Log.d(LOG_TAG, ContentUris.withAppendedId(CONTENT_URI, id) + "");
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

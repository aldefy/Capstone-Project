package techgravy.nextstop.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    public static final String TABLE_SEARCH = "search";

    public static final class SearchColumns implements BaseColumns {
        //Task description
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";

    }

    //Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "nextstop.authority";

    //Alphabetically arranged
    public static final String ALPHA_SORT = String.format("%s COLLATE NOCASE", SearchColumns.KEY_NAME);

    //Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_SEARCH)
            .build();


    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }


}

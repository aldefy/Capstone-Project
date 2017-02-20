package techgravy.nextstop.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    static final String TABLE_SEARCH = "search";  // cant be saved to strings , context prob

    public static final class SearchColumns implements BaseColumns {
        //Task description
        public static final String KEY_ID = "id";  // cant be saved to strings , context prob
        public static final String KEY_NAME = "name";  // cant be saved to strings , context prob

    }

    //Unique authority string for the content provider
    static final String CONTENT_AUTHORITY = "nextstop.authority";  // cant be saved to strings , context prob

    //Alphabetically arranged
    public static final String ALPHA_SORT = String.format("%s COLLATE NOCASE", SearchColumns.KEY_NAME);   // cant be saved to strings , context prob

    //Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")   // cant be saved to strings , context prob
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_SEARCH)
            .build();


    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }


}

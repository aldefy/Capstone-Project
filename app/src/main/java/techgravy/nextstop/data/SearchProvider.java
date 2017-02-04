package techgravy.nextstop.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by aditlal on 04/02/17 - 04.
 */

public class SearchProvider extends ContentProvider {

    private static final String TAG = SearchProvider.class.getSimpleName();
    private static final int SEARCH = 100;
    private static final int SEARCH_WITH_ID = 101;
    private DatabaseHandler mDatabaseHandler;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://nextstop.authority/search
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_SEARCH,
                SEARCH);

        // content://nextstop.authority/search/id
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_SEARCH + "/#",
                SEARCH_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHandler = new DatabaseHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase db = mDatabaseHandler.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case SEARCH:
                retCursor = db.query(DatabaseContract.TABLE_SEARCH,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SEARCH_WITH_ID:
                retCursor = db.query(DatabaseContract.TABLE_SEARCH,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
         Not Used
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHandler.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case SEARCH:
                long id = db.insert(DatabaseContract.TABLE_SEARCH, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = mDatabaseHandler.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case SEARCH:
                //Rows aren't counted with null selection
                tasksDeleted = db.delete(DatabaseContract.TABLE_SEARCH, "is_complete=?", new String[]{String.valueOf(1)});
                break;
            case SEARCH_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(DatabaseContract.TABLE_SEARCH, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHandler.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksUpdated;
        switch (match) {
            case SEARCH_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksUpdated = db.update(DatabaseContract.TABLE_SEARCH, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksUpdated;
    }
}

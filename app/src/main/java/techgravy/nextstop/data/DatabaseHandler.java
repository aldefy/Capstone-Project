package techgravy.nextstop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static techgravy.nextstop.data.DatabaseContract.SearchColumns;
import static techgravy.nextstop.data.DatabaseContract.TABLE_SEARCH;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nextstop.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_SEARCH = String.format("create table %s"
                    + " (%s text primary key, %s text)",
            TABLE_SEARCH,
            SearchColumns.KEY_ID,
            SearchColumns.KEY_NAME
    );

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SEARCH);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SEARCH);
        onCreate(db);
    }

}
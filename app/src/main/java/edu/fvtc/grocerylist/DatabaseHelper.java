package edu.fvtc.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "items_db";

    // Constructors
    public DatabaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS tblItem (" +
                "id integer primary key autoincrement, " +
                "description text not null, " +
                "isOnShoppingList integer not null, " +
                "isInCart integer not null);";

        Log.d(TAG, "onCreate: " + sql);
        // Create the table
        db.execSQL(sql);

        // Insert default items.
        /*sql = "INSERT INTO tblItem (Description, IsOnShoppingList, IsInCart) VALUES " +
                "('Milk', 1, 0)," +
                "('Eggs', 1, 1)," +
                "('Bacon', 1, 1)," +
                "('Bread', 0, 0)," +
                "('Coffee', 0, 1);";
        db.execSQL(sql);*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Upgrading database from version " + oldVersion + " to " + newVersion);

        if (oldVersion < 2) {
            // Upgrade logic for version 1 to version 2
            // For example, add a new column or modify existing tables
            String sql = "ALTER TABLE tblItem ADD COLUMN DateAdded INTEGER DEFAULT 0;";
            db.execSQL(sql);
        }
    }
}

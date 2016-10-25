package edu.sjsu.starruc.sjsumap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by StarRUC on 10/23/16.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String TABLE_BUILDING = "building";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHOTO_URL = "photo_url";
    public static final String COLUMN_X1 = "x1";
    public static final String COLUMN_Y1 = "y1";
    public static final String COLUMN_X2 = "x2";
    public static final String COLUMN_Y2 = "y2";
    public static final String COLUMN_CX = "cx";
    public static final String COLUMN_CY = "cy";

    private static final String DATABASE_NAME = "buildings.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BUILDING + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_ADDRESS
            + " text not null, " + COLUMN_PHOTO_URL
            + " text not null, " + COLUMN_X1
            + " integer, " + COLUMN_Y1
            + " integer, " + COLUMN_X2
            + " integer, " + COLUMN_Y2
            + " integer, " + COLUMN_CX
            + " integer, " + COLUMN_CY
            + ");";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("drop table if exists " + TABLE_BUILDING);
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        onCreate(db);
    }
}

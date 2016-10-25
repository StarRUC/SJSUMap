package edu.sjsu.starruc.sjsumap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StarRUC on 10/23/16.
 */

public class BuildingDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = { DbHelper.COLUMN_ID,
            DbHelper.COLUMN_NAME,
            DbHelper.COLUMN_ADDRESS,
            DbHelper.COLUMN_PHOTO_URL,
            DbHelper.COLUMN_X1,
            DbHelper.COLUMN_Y1,
            DbHelper.COLUMN_X2,
            DbHelper.COLUMN_Y2,
            DbHelper.COLUMN_CX,
            DbHelper.COLUMN_CY
    };

    public BuildingDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addBuilding(String name, String address, String photoUrl,
                                int x1, int y1, int x2, int y2) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME, name);
        values.put(DbHelper.COLUMN_ADDRESS, address);
        values.put(DbHelper.COLUMN_PHOTO_URL, photoUrl);
        values.put(DbHelper.COLUMN_X1, x1);
        values.put(DbHelper.COLUMN_Y1, y1);
        values.put(DbHelper.COLUMN_X2, x2);
        values.put(DbHelper.COLUMN_Y2, y2);
        values.put(DbHelper.COLUMN_CX, (x1 + x2) / 2);
        values.put(DbHelper.COLUMN_CY, (y1 + y2) / 2);

        long insertId = database.insert(DbHelper.TABLE_BUILDING, null,
                values);
//        Cursor cursor = database.query(DbHelper.TABLE_BUILDING,
//                allColumns, DbHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        Building newBuilding = cursorToBuilding(cursor);
//        cursor.close();
//        return newBuilding;
        return;
    }

    public Building findBuildingByName(String name) {
        Cursor cursor = database.query(DbHelper.TABLE_BUILDING,
                allColumns, DbHelper.COLUMN_NAME + " like \"" + name + "\"",
                null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Building newBuilding = cursorToBuilding(cursor);
        cursor.close();
        return newBuilding;
    }

    public Building findBuildingByLocation(int x, int y) {
        Cursor cursor = database.query(DbHelper.TABLE_BUILDING, allColumns,
                DbHelper.COLUMN_X1 + " <= " + x + " and "
                    + DbHelper.COLUMN_X2 + " >= " + x + " and "
                    + DbHelper.COLUMN_Y1 + " <= " + y + " and "
                    + DbHelper.COLUMN_Y2 + " >= " + y,
                null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Building newBuilding = cursorToBuilding(cursor);
        cursor.close();
        return newBuilding;
    }

//    public void deleteBuilding(Building building) {
//        long id = building.getId();
//        System.out.println("Building deleted with id: " + id);
//        database.delete(DbHelper.TABLE_BUILDING, DbHelper.COLUMN_ID
//                + " = " + id, null);
//    }
//
//    public List<Building> getAllComments() {
//        List<Building> buildings = new ArrayList<Building>();
//
//        Cursor cursor = database.query(DbHelper.TABLE_BUILDING,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Building building = cursorToBuilding(cursor);
//            buildings.add(building);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
//        return buildings;
//    }

    private Building cursorToBuilding(Cursor cursor) {
        Building building = new Building();
        building.setId(cursor.getLong(0));
        building.setName(cursor.getString(1));
        building.setAddress(cursor.getString(2));
        building.setPhotoUrl(cursor.getString(3));
        return building;
    }

}

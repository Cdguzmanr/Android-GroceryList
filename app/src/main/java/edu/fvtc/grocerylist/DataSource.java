package edu.fvtc.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DataSource {
    SQLiteDatabase database;
    DatabaseHelper dbHelper;
    public static final String TAG = "DataSource";


    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context,
                DatabaseHelper.DATABASE_NAME,
                null,
                DatabaseHelper.DATABASE_VERSION);
        open();
    }

    public void open() throws SQLException {
        open(false);
    }

    public void open(boolean refresh) throws SQLException {

        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + database.isOpen());
        if (refresh) refreshData();
    }

    public void close() {
        dbHelper.close();
    }

    public void refreshData() {
        Log.d(TAG, "refreshData: Start");
        ArrayList<Item> items = new ArrayList<Item>();

        items.add(new Item("Database Data", 1, 1));
        items.add(new Item("Milk", 1, 0));
        items.add(new Item("Eggs", 1, 1));
        items.add(new Item("Bacon", 1, 1));
        items.add(new Item("Bread", 0, 0));
        items.add(new Item("Coffee", 0, 1));

        // Delete and reinsert all the items
        int results = 0;
        for (Item item : items) {
            results += insert(item);
        }
        Log.d(TAG, "refreshData: End: " + results + " rows...");
    }

    public Item get(int id) {
        ArrayList<Item> items = new ArrayList<Item>();
        Item item = null;

        try {
            String query = "Select * from tblItem where id = " + id;
            Cursor cursor = database.rawQuery(query, null);

            //Cursor cursor = database.query("tblItem",null, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                item.setIsOnShoppingList(cursor.getInt(2));
                item.setIsInCart(cursor.getInt(3));
                item.setImgId(cursor.getInt(4));

                //item.setLatitude(cursor.getDouble(7));
                //item.setLongitude(cursor.getDouble(8));

                Log.d(TAG, "get: " + item.toString());

                cursor.moveToNext();

            }
        } catch (Exception e) {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return item;
    }

    public ArrayList<Item> get(String sortBy, String sortOrder) {
        Log.d(TAG, "get: Start");
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            String sql = "SELECT * from tblItem ORDER BY " + sortBy + " " + sortOrder;
            Cursor cursor = database.rawQuery(sql, null);
            Item item;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                item.setIsOnShoppingList(cursor.getInt(2));
                item.setIsInCart(cursor.getInt(3));
                item.setImgId(cursor.getInt(4));

                if (item.getImgId() == 0)
                    item.setImgId(R.drawable.photoicon);

                items.add(item);
                Log.d(TAG, "get: " + item.toString());
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    public int deleteAll() {
        try {
            return database.delete("tblItem", null, null);
        } catch (Exception e) {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Item item) {
        Log.d(TAG, "delete: Start");
        try {
            int id = item.getId();
            if (id < 1)
                return 0;
            Log.d(TAG, "delete: " + id);
            return delete(id);
        } catch (Exception e) {
            Log.d(TAG, "Delete: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(int id) {
        try {
            Log.d(TAG, "delete: Start : " + id);
            Log.d(TAG, "delete: database" + database.isOpen());
            return database.delete("tblItem", "id = " + id, null);
        } catch (Exception e) {
            Log.d(TAG, "Delete: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int getNewId() {
        int newId = -1;
        try {
            // Get the highest id in the table and add 1
            String sql = "SELECT max(id) from tblItem";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            newId = cursor.getInt(0) + 1;
            cursor.close();
        } catch (Exception e) {

        }
        return newId;
    }

    public int update(Item item) {
        Log.d(TAG, "update: Start" + item.toString());
        int rowsaffected = 0;

        if (item.getId() < 1)
            return insert(item);

        try {
            ContentValues values = new ContentValues();
            values.put("description", item.getDescription());
            values.put("isOnShoppingList", item.getIsOnShoppingList());
            values.put("isInCart", item.getIsInCart());
            values.put("imgId", item.getImgId());

            //values.put("latitude", item.getLatitude());
            //values.put("longitude", item.getLongitude());

            String where = "id = " + item.getId();

            rowsaffected = (int) database.update("tblItem", values, where, null);
        } catch (Exception e) {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsaffected;
    }

    public int insert(Item item) {
        Log.d(TAG, "insert: Start");
        int rowsaffected = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("description", item.getDescription());
            values.put("isOnShoppingList", item.getIsOnShoppingList());
            values.put("isInCart", item.getIsInCart());
            values.put("imgId", item.getImgId());
            //values.put("latitude", item.getLatitude());
            //values.put("longitude", item.getLongitude());

            rowsaffected = (int) database.insert("tblItem", null, values);
        }
        catch (Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsaffected;

    }
}
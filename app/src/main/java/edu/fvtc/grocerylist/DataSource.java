package edu.fvtc.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static final String TAG = "DataSource";
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context,
                DatabaseHelper.DATABASE_NAME,
                null,
                DatabaseHelper.DATABASE_VERSION);
    }

    public void open() throws SQLException{
        open(false);
    }

    public void open(boolean refresh)  throws SQLException{

        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + database.isOpen());
        if(refresh) refreshData();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void refreshData()
    {
        Log.d(TAG, "refreshData: Start");
        ArrayList<Item> items = new ArrayList<Item>();

        items.add(new Item("Database Data", 1,1));
        items.add(new Item("Milk", 1,0));
        items.add(new Item("Eggs", 1,1));
        items.add(new Item("Bacon", 1,1));
        items.add(new Item("Bread", 0,0));
        items.add(new Item("Coffee", 0,1));

        // Delete and reinsert all the items
        int results = 0;
        for(Item item : items){
            results += insert(item);
        }
        Log.d(TAG, "refreshData: End: " + results + " rows...");
    }

    public Item get(int id)
    {
        return new Item();
    }

    public ArrayList<Item> get()
    {
        Log.d(TAG, "get: Start");
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            String sql = "SELECT * from tblItem";
            Cursor cursor = database.rawQuery(sql, null);
            Item item;
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                item.setIsOnShoppingList(cursor.getInt(2));
                item.setIsInCart(cursor.getInt(3));


                // This project doesn't have images (yet ?)
/*                if(item.getImgId() == 0)
                    item.setImgId(R.drawable.photoicon);*/

                items.add(item);
                Log.d(TAG, "get: " + item.toString());
                cursor.moveToNext();
            }

        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    public int deleteAll()
    {
        return 0;
    }

    public int delete(int id)
    {
        return 0;
    }

    public int getNewId()
    {
        int newId =-1;
        try{
            // Get the highest id in the table and add 1
            String sql = "SELECT max(id) from tblItem";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            newId = cursor.getInt(0) + 1;
            cursor.close();
        }
        catch(Exception e)
        {

        }
        return newId;
    }

    public int insert(Item item)
    {
        return 0;
    }
    public int update(Item item)
    {
        return 0;
    }












    // -----------------------------------------------------------------------------------
    public long addItem(String description, int isOnShoppingList, int isInCart) {
        ContentValues values = new ContentValues();
        values.put("Description", description);
        values.put("IsOnShoppingList", isOnShoppingList);
        values.put("IsInCart", isInCart);
        return database.insert("tblItem", null, values);
    }

//    public List<Item> getAllItems() {
//        List<Item> items = new ArrayList<>();
//        Cursor cursor = database.query("tblItem", null, null, null, null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Item item = cursorToItem(cursor);
//            items.add(item);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return items;
//    }


//    private Item cursorToItem(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex("Id"));
//        String description = cursor.getString(cursor.getColumnIndex("Description"));
//        int isOnShoppingList = cursor.getInt(cursor.getColumnIndex("IsOnShoppingList"));
//        int isInCart = cursor.getInt(cursor.getColumnIndex("IsInCart"));
//        Item newItem = new Item(description, isOnShoppingList, isInCart);
//        newItem.setId(id);
//        return newItem;
//    }

    public void updateItem(Item item) {
        ContentValues values = new ContentValues();
        values.put("Description", item.getDescription());
        values.put("IsOnShoppingList", item.getIsOnShoppingList());
        values.put("IsInCart", item.getIsInCart());
        database.update("tblItem", values, "Id=?", new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(Item item) {
        database.delete("tblItem", "Id=?", new String[]{String.valueOf(item.getId())});
    }
}

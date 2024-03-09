package edu.fvtc.grocerylist;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";
    public ItemAdapter itemAdapter;

    ArrayList<Item> items;
    ArrayList<Item> shoppingItems = new ArrayList<>();

    boolean isMaster; // flag to identify the current view

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            // Use the index to get an item
            int position = viewHolder.getAdapterPosition();
            String description = items.get(position).getDescription();
            Item item = items.get(position);
            Log.d(TAG, "onClick: " + item.toString());

            // Add code to startActivity of another activity.
        }
    };

    // Initial Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ---------------------------- New Run --------------------------------------");

        // Initialize isMaster flag to true
        isMaster = true;
        this.setTitle("Master List");
        // Populate items
        PopulateItems();

        // Adds current items in the class to an array // Do I need this?
/*        ArrayList<String> descriptions = new ArrayList<String>();
        for(Item item : items)
        {
            descriptions.add(item.toString());
        }*/

        bindRecyclerView(items);

/*        // Bind the Recyclerview || Allows the display of items in the array into the RecyclerView at activity_main
        RecyclerView rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(items, this);
        itemAdapter.setOnItemClickListener(onClickListener);
        rvItems.setAdapter(itemAdapter);*/


    }


    private void bindRecyclerView(ArrayList<Item> data) {
        // Bind the RecyclerView
        RecyclerView rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(data, this, isMaster); // Pass isMaster flag to the adapter
        itemAdapter.setOnItemClickListener(onClickListener);
        rvItems.setAdapter(itemAdapter);
    }


    // Verifies if XML can be readed
    private void PopulateItems() {
        try {
            boolean xmlFileEmpty = ReadXMLFile();
            if (xmlFileEmpty) {
                Log.d(TAG, "PopulateItems: XML is Empty. Creating default items:");
                createItems();
            }
            updateShoppingItems();
        } catch (Exception e) {
            Log.e(TAG, "Error populating items: " + e.getMessage());
        }
    }

    // Creates a list default list of objects to display
    private void createItems() {
        items = new ArrayList<Item>();

        // Description, isOnShoppingList, isInCart
        items.add(new Item("Milk", 1,0));
        items.add(new Item("Eggs", 1,1));
        items.add(new Item("Bacon", 1,1));
        items.add(new Item("Bread", 0,0));
        items.add(new Item("Coffee", 0,1));

/*        items.add(new Item("Apples", 1, 0));
        items.add(new Item("Oranges", 1, 1));
        items.add(new Item("Bananas", 1, 1));
        items.add(new Item("Cereal", 0, 0));
        items.add(new Item("Butter", 0, 1));
        items.add(new Item("Cheese", 1, 0));
        items.add(new Item("Yogurt", 1, 1));
        items.add(new Item("Spinach", 1, 0));
        items.add(new Item("Tomatoes", 0, 1));
        items.add(new Item("Potatoes", 0, 1));
        items.add(new Item("Onions", 1, 0));
        items.add(new Item("Carrots", 1, 1));
        items.add(new Item("Pasta", 0, 0));
        items.add(new Item("Rice", 0, 1));
        items.add(new Item("Chicken", 1, 0));
        items.add(new Item("Beef", 1, 1));
        items.add(new Item("Pork", 0, 1));
        items.add(new Item("Salmon", 1, 0));
        items.add(new Item("Shrimp", 0, 1));
        items.add(new Item("Ice Cream", 1, 0));
        items.add(new Item("Frozen Pizza", 1, 1));
        items.add(new Item("Frozen Vegetables", 0, 1));
        items.add(new Item("Olive Oil", 1, 0));
        items.add(new Item("Vinegar", 0, 1));
        items.add(new Item("Salt", 1, 0));
        items.add(new Item("Pepper", 0, 1));
        items.add(new Item("Sugar", 1, 1));
        items.add(new Item("Flour", 0, 0));
        items.add(new Item("Chocolate", 1, 0));
        items.add(new Item("Cookies", 0, 1));*/

        Log.d(TAG, "createItems: Items created: " + items.size());
        // Save new items in XMLFile
        WriteXMLFile();
    }

    private void updateShoppingItems() {
        shoppingItems.clear(); // Clear existing shopping items

        // Iterate through items and add those where isOnShoppingList = 1 to shoppingItems
        for (Item item : items) {
            if (item.getIsOnShoppingList() == 1) {
                shoppingItems.add(item);
            }
        }
        Log.d(TAG, "updateShoppingItems: Shopping List Items updated. Added: " + shoppingItems.size());
    }

    // Menu Methods -----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) // Generate Menu Items
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) // Menu Item Selection + Event Trigger
    {
        int id = item.getItemId();

        if(id == R.id.action_showMasterList)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            ShowMasterList();
        }
        else if (id == R.id.action_showShoppingList)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            ShowShoppingList();
        } else if (id == R.id.action_addItem)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            addItemDialog();

        } else if (id == R.id.action_clearAll)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            ClearAll();

        } else { // Delete Checked
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            DeleteChecked();
        }
        return super.onOptionsItemSelected(item);
    }


    private void DeleteChecked() {

        ArrayList<Item> deletedItems = new ArrayList<>();
        // Find checked items
        int counter = 0;
        if (isMaster){
            for (Item item : items) {
                if (item.getIsInCart() == 1) {
                    deletedItems.add(item);
                    counter ++;
                }
            }
            Log.d(TAG, "DeleteChecked: Items deleted from Master List: " + counter);
        } else {
            for (Item item : items) {
                if (item.getIsInCart() == 1) {
                    item.setIsOnShoppingList(0);
                    item.setIsInCart(0);
                    counter ++;
                }
            }
            Log.d(TAG, "DeleteChecked: Items deleted from Shopping List: " + counter);
        }

        // Notify adapter of data change and save changes
        items.removeAll(deletedItems);
        itemAdapter.notifyDataSetChanged();
        WriteXMLFile();
    }

    private void ClearAll() {
        // Iterate through items and clear isChecked state
        for (Item item : items) {
            item.setIsInCart(0); // Clear checkbox state
        }
        itemAdapter.notifyDataSetChanged(); // Notify adapter of data change
        WriteXMLFile(); // Save changes to file
    }

    private void ShowShoppingList() {
        isMaster = false;
        updateShoppingItems();
        bindRecyclerView(shoppingItems); // Refresh RecyclerView with Shopping List data
        this.setTitle("Shopping List");
    }

    private void ShowMasterList() {
        isMaster = true;
        bindRecyclerView(items); // Refresh RecyclerView with Master List data
        this.setTitle("Master List");
    }


    // Document Manipulation - Read and Write TXT and XML files --------------------------------------------------------------
    public void WriteXMLFile() {
        try{
            Log.d(TAG, "WriteXMLFile: Start");
            FileIO fileIO = new FileIO();
            fileIO.WriteXMLFile(XMLFILENAME, this, items);
            Log.d(TAG, "WriteXMLFile: End");

            updateShoppingItems(); // Updates the ShoppingList array
        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteXMLFile: " + e.getMessage());
        }
    }

    public boolean ReadXMLFile() {
        try {
            FileIO fileIO = new FileIO();
            items = fileIO.ReadFromXMLFile(XMLFILENAME, this);
            Log.d(TAG, "ReadXMLFile: Items loaded: " + items.size());

            if (items.isEmpty()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.d(TAG, "ReadXMLFile: " + e.getMessage());
            return  false;
        }
    }



    private void addItemDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View addItemView = layoutInflater.inflate(R.layout.additem, null);

        // Show the Dialog to the user modularly.
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_item)
                .setView(addItemView)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: OK");
                                EditText etItem = addItemView.findViewById(R.id.etAddItem);
                                String itemDecription = etItem.getText().toString();

                                // Add new item created to list
                                if (isMaster){
                                    items.add(new Item(itemDecription, 0,0));
                                } else {
                                    items.add(new Item(itemDecription, 1,0));
                                }



                                Log.d(TAG, "onClick: New item added: " + itemDecription);
                                // Save items in xml file
                                WriteXMLFile();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: Cancel");
                            }
                        }).show();

    }



    // Not using TXT file manipulation
    private void WriteTextFile() {
        try{
            FileIO fileIO = new FileIO();
            int counter = 0;
            String[] data = new String[items.size()];

            for(Item item : items)
            {
                data[counter++] = item.toString();
            }
            fileIO.writeFile(FILENAME, this, data);

        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteTextFile: " + e.getMessage());
        }
    }


    private void ReadTextFile() {
        try {
            FileIO fileIO = new FileIO();
            ArrayList<String> strData = fileIO.readFile(FILENAME, this);

            items = new ArrayList<Item>();

            for(String s : strData)
            {
                String[] data = s.split("\\|");
                items.add(new Item(data[0],
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2])));
                Log.d(TAG, "ReadTextFile: " + items.get(items.size()-1).getDescription());
            }
            Log.d(TAG, "ReadTextFile: " + items.size());

        }
        catch(Exception e)
        {
            Log.d(TAG, "ReadTextFile: " + e.getMessage());
        }
    }


}
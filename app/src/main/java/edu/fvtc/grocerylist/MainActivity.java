package edu.fvtc.grocerylist;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";
    public ItemAdapter itemAdapter;

    ArrayList<Item> items = new ArrayList<Item>();
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
            Log.d(TAG, "onClick: " + item.getDescription());
            Intent intent = new Intent(MainActivity.this, ItemsEditActivity.class);
            intent.putExtra("itemid", item.getId());
            Log.d(TAG, "onClick: " + item.getId());
            startActivity(intent);
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
        //items = new ArrayList<Item>();


        // Init API
        readFromAPI();

        // Init Database and load DB files
        //initDatabase();


        // Populate items from local XML Files
        //PopulateItemsXML();


        // Adds current items in the class to an array // Do I need this?
/*        ArrayList<String> descriptions = new ArrayList<String>();
        for(Item item : items)
        {
            descriptions.add(item.toString());
        }*/

        // Bind the Recyclerview || Allows the display of items in the array into the RecyclerView at activity_main
        bindRecyclerView(items);




    }

    private void AddItem() {
        Intent intent = new Intent(MainActivity.this, ItemsEditActivity.class);
        intent.putExtra("itemid", -1);
        Log.d(TAG, "onClick: ");
        startActivity(intent);
    }

    private void readFromAPI() {
        try{
            Log.d(TAG, "readFromAPI: Start");
            RestClient.execGetRequest(getString(R.string.api_url_bfoote),
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {
                            Log.d(TAG, "onSuccess: Got Here!");
                            items = result;
                            bindRecyclerView(items);
                        }
                    });
        }
        catch(Exception e){
            Log.e(TAG, "readFromAPI: Error: " + e.getMessage());
        }
    }


//    private void initDatabase() {
//        DataSource ds = new DataSource(this);
//        ds.open(true);
//        items = ds.get();
//        Log.d(TAG, "initDatabase: Items: " + items.size());
//    }

//    private void initDatabase() {
//        DataSource ds = new DataSource(this);
//        ds.open(false);
//        String sortBy = getSharedPreferences("itemspreferences",
//                Context.MODE_PRIVATE)
//                .getString("sortby", "name");
//        String sortOrder = getSharedPreferences("itemspreferences",
//                Context.MODE_PRIVATE)
//                .getString("sortorder", "ASC");
//        items = ds.get(sortBy, sortOrder);
//        Log.d(TAG, "initDatabase: Items: " + items.size());
//    }




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
    private void PopulateItemsXML() {
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
        items.add(new Item("Local Data", 1,1));
        items.add(new Item("Milk", 1,0));
        items.add(new Item("Eggs", 1,1));
        items.add(new Item("Bacon", 1,1));
        items.add(new Item("Bread", 0,0));
        items.add(new Item("Coffee", 0,1));

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
            AddItem();

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
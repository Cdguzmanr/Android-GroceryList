package edu.fvtc.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";

    ArrayList<Item> items;

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



        // Added Methods:
        createItems(); // Call method to generate items

        // Adds current items in the class to an array
        ArrayList<String> descriptions = new ArrayList<String>();
        for(Item item : items)
        {
            descriptions.add(item.toString());
        }

        // Bind the Recyclerview || Allows the display of items in the array into the RecyclerView at activity_main
        RecyclerView rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(items, this);
        itemAdapter.setOnItemClickListener(onClickListener);

        rvItems.setAdapter(itemAdapter);
        Log.d(TAG, "onCreate: ");
        
        

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
        items.add(new Item("Apples", 1, 0));
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
        items.add(new Item("Cookies", 0, 1));

    }

    // Menu Methods
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
            ReadTextFile();
        }
        else if (id == R.id.action_showShoppingList)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            WriteTextFile();
        } else if (id == R.id.action_addItem)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            ReadXMLFile();

        } else if (id == R.id.action_clearAll)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            WriteXMLFile();

        } else { // Delete Checked
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //WriteXMLFile();
        }
        return super.onOptionsItemSelected(item);
    }


    // Document Manipulation - Read and Write TXT and XML files
    private void WriteXMLFile() {
        try{
            Log.d(TAG, "WriteXMLFile: Start");
            FileIO fileIO = new FileIO();
            fileIO.WriteXMLFile(XMLFILENAME, this, items);
            Log.d(TAG, "WriteXMLFile: End");
        }
        catch(Exception e)
        {
            Log.d(TAG, "WriteXMLFile: " + e.getMessage());
        }
    }

    private void ReadXMLFile() {
        try {
            FileIO fileIO = new FileIO();
            items = fileIO.ReadFromXMLFile(XMLFILENAME, this);
            Log.d(TAG, "ReadXMLFile: Items: " + items.size());
        }
        catch (Exception e)
        {
            Log.d(TAG, "ReadXMLFile: " + e.getMessage());
        }

    }

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
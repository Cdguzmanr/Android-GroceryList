package edu.fvtc.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILENAME = "data.txt";
    public static final String XMLFILENAME = "data.xml";


    // Initial Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
            //ReadTextFile();
        }
        else if (id == R.id.action_showShoppingList)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //WriteTextFile();
        } else if (id == R.id.action_addItem)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //ReadXMLFile();

        } else if (id == R.id.action_clearAll)
        {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //ReadXMLFile();

        } else { // Delete Checked
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
            //WriteXMLFile();
        }
        return super.onOptionsItemSelected(item);
    }



}
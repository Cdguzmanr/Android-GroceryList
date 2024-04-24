package edu.fvtc.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SetOwner extends AppCompatActivity {
    public static final String TAG = "SetOwner";


    EditText etOwner;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);*/
        setContentView(R.layout.activity_set_owner);
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        etOwner = findViewById(R.id.etOwner);

        btnSave = findViewById(R.id.btnSetOwner);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOwner();
            }
        });

    }

    private void setOwner() {
        String owner = etOwner.getText().toString().trim();

        if(!owner.isEmpty()){
            // SAve owner in Shared Preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("owner", owner); // set the owner in local sessions (preferences)
            editor.apply();

            // Redirect to MainActivity
            Intent intent = new Intent(SetOwner.this,  MainActivity.class);
            Log.d(TAG, "onClick: ");
            startActivity(intent);
            finish(); // Finishes the current activity to prevent the user from navigating back to it
        }
    }


}
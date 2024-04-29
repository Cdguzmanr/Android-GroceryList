package edu.fvtc.grocerylist;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ItemsEditActivity extends AppCompatActivity {
    public static final String TAG = "ItemsEdit";
    public static final int PERMISSION_REQUEST_PHONE = 102;
    public static final int PERMISSION_REQUEST_CAMERA = 103;
    public static final int CAMERA_REQUEST = 1888;
    Item item;
    boolean loading = true;
    int itemId = -1;

    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_edit);

        Log.d(TAG, "onCreate: Start");

        Bundle extras = getIntent().getExtras();
        itemId = extras.getInt("itemid");

        this.setTitle("Item: " + itemId);

        if (itemId != -1) {
            // Get the item
            initItem(itemId);
        } else {
            item = new Item();
        }


        initToggleButton();
        initSaveButton();

        initTextChanged(R.id.etName);

        // Get the items
        //items = ItemsListActivity.readItems(this);

        
        setForEditting(false);
        initImageButton();
        Log.d(TAG, "onCreate: End");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Here");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageButton = findViewById(R.id.imageItem);
                imageButton.setImageBitmap(scaledPhoto);
                item.setPhoto(scaledPhoto);
            }
        }
    }

    private void initImageButton() {
        ImageButton imageItem = findViewById(R.id.imageItem);

        imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    // Check for the manifest permission
                    if (ContextCompat.checkSelfPermission(ItemsEditActivity.this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ItemsEditActivity.this, Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.activity_items_edit), "Items requires this permission to take a photo.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "onClick: snackBar");
                                    ActivityCompat.requestPermissions(ItemsEditActivity.this,
                                            new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_PHONE);
                                }
                            }).show();
                        } else {
                            Log.d(TAG, "onClick: ");
                            ActivityCompat.requestPermissions(ItemsEditActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_PHONE);
                            takePhoto();
                        }
                    } else {
                        Log.d(TAG, "onClick: ");
                        takePhoto();
                    }
                } else {
                    // Only rely on the previous permissions
                    takePhoto();
                }
            }
        });

    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void readFromAPI(int itemId) {
        try {
            Log.d(TAG, "readFromAPI: Start");
            RestClient.execGetOneRequest(getString(R.string.api_url) + itemId,
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {
                            Log.d(TAG, "onSuccess: Got Here!");
                            item = result.get(0);
                            rebindItem();
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "readFromAPI: Error: " + e.getMessage());
        }
    }

    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemId == -1)
                {
                    Log.d(TAG, "onClick: Create New Item: " + item.toString());

                    RestClient.execPostRequest(item, getString(R.string.api_url),
                            ItemsEditActivity.this,
                            new VolleyCallback() {
                                @Override // work correctly please sobs***  T~T
                                public void onSuccess(ArrayList<Item> result) {
                                    item.setId(result.get(0).getId());
                                    Log.d(TAG, "onSuccess: ** Post: " + item.getId());

                                    // Redirect to Main List View
                                    Intent intent = new Intent(ItemsEditActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                    );
                }
                else {
                    RestClient.execPutRequest(item, getString(R.string.api_url) + itemId,
                            ItemsEditActivity.this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Item> result) {
                                    Log.d(TAG, "onSuccess: Put" + item.getId());
                                }
                            });
                }
            }
        });
    }

    private void initTextChanged(int controlId) {
        EditText editText = findViewById(controlId);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setControlText(controlId, s.toString());
            }
        });
    }

    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.toggleButtonEdit);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditting(toggleButton.isChecked());
            }
        });

    }

    private void setForEditting(boolean checked) {
        EditText editName = findViewById(R.id.etName);

        editName.setEnabled(checked);

        if (checked) {
            // Set Focus to the editName
            editName.requestFocus();
        } else {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    private void initItem(int itemId) {

        // Get the items
        //items = ItemsListActivity.readItems(this);
        // Get the item
        //item = items.get(itemId);
        Log.d(TAG, "initItem: " + itemId);

        //ItemsDataSource ds = new ItemsDataSource(ItemsEditActivity.this);
        //items = ds.get();
        //item = ds.get(itemId);
        //rebindItem();

        readFromAPI(itemId);
    }

    private void rebindItem() {
        EditText editName = findViewById(R.id.etName);
        ImageButton imageButtonPhoto = findViewById(R.id.imageItem);

        editName.setText(item.getDescription());

        if (item.getPhoto() == null) {
            Log.d(TAG, "rebindItem: Null photo");
            item.setPhoto(BitmapFactory.decodeResource(this.getResources(), R.drawable.photoicon));
        }
        imageButtonPhoto.setImageBitmap(item.getPhoto());
    }
}
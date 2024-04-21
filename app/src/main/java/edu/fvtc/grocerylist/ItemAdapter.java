package edu.fvtc.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    private CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;
    private boolean isDeleting;
    public void setDelete(boolean b)
    {
        isDeleting = b;
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public CheckBox chkItem;
        public ImageButton imgItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chkItem = itemView.findViewById(R.id.chkItem);
            imgItem = itemView.findViewById(R.id.imgItem);

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);

            // Set an onClickListener for the checkbox
            chkItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Update the isChecked state of the item
                        Item item = itemData.get(position);
                        item.setIsInCart(chkItem.isChecked() ? 1 : 0);
                        // Notify the activity to save changes
                        ((MainActivity) parentContext).WriteXMLFile();
                    }
                }
            });
        }

        public CheckBox getChkItem() {
            return chkItem;
        }
        public ImageButton getImageButtonPhoto() {return imgItem;}
    }



    // Item Adapter Class -----------------------------------------------
    private boolean isMaster; // Flag to determine current view type

    public ItemAdapter(ArrayList<Item> data, Context context, boolean isMaster) {
        itemData = data;
        parentContext = context;
        this.isMaster = isMaster;

        Log.d(TAG, "ItemAdapter: " + data.size());
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v);
    }


    // Old binder
/*    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemData.get(position);
        holder.getChkItem().setText(item.getDescription());
        // Set the checkbox state based on the item's isInCart value
        holder.getChkItem().setChecked(item.getIsInCart() == 1);
    }*/

    // New binder
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemData.get(position);

        // Set image
        if(itemData.get(position).getPhoto() != null)
            holder.getImageButtonPhoto().setImageBitmap(itemData.get(position).getPhoto());


        holder.getChkItem().setText(item.getDescription());

        // Determine checkbox state based on the item's isInCart value
        holder.getChkItem().setChecked(item.getIsInCart() == 1);

        // Handle checkbox click event to update isInCart property and save changes
        holder.getChkItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Item clickedItem = itemData.get(adapterPosition);
                    clickedItem.setIsInCart(holder.getChkItem().isChecked() ? 1 : 0);
                    ((MainActivity) parentContext).WriteXMLFile(); // Save changes to file
                }
            }
        });
    }


    private void deleteItem(int position) {
        Log.d(TAG, "deleteItem: " + position);
        Item item = itemData.get(position);
        itemData.remove(position);

        //FileIO.writeFile(ItemsListActivity.FILENAME,
        //                (AppCompatActivity) parentContext.getApplicationContext(),
        //                ItemsListActivity.createDataArray(itemData));
        //                ItemsListActivity.createDataArray(itemData));
        //                notifyDataSetChanged();

        Log.d(TAG, "deleteItem: parentContext: " + parentContext);
        RestClient.execDeleteRequest(item,
                parentContext.getString(R.string.api_url_bfoote) + item.getId(),
                parentContext,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Item> result) {
                        itemData.remove(item);
                        notifyDataSetChanged();
                        Log.d(TAG, "deleteItem");
                    }
                });

        //ItemsDataSource ds = new ItemsDataSource(parentContext);
        //Log.d(TAG, "deleteItem: " + item.toString());
        //boolean didDelete = ds.delete(item) > 0;



    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }
}
















/*

public class ItemAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public CheckBox chkItem;


        //private View.OnClickListener onClickListener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chkItem = itemView.findViewById(R.id.chkItem);
            // Code involving in click an item in the list
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
        public CheckBox getChkItem()
        {
            return chkItem;
        }

    }

    public ItemAdapter(ArrayList<Item> data, Context context)
    {
        itemData = data;
        Log.d(TAG, "ItemAdapter: " + data.size());
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + itemData.get(position));
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getChkItem().setText(itemData.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }
}
*/

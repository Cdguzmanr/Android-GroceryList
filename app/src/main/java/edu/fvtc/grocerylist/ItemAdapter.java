package edu.fvtc.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public CheckBox chkItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chkItem = itemView.findViewById(R.id.chkItem);
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
    }

    public ItemAdapter(ArrayList<Item> data, Context context) {
        itemData = data;
        parentContext = context;
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

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemData.get(position);
        holder.getChkItem().setText(item.getDescription());
        // Set the checkbox state based on the item's isInCart value
        holder.getChkItem().setChecked(item.getIsInCart() == 1);
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

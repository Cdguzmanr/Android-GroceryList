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

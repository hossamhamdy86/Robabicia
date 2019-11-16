package com.example.robabicia;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robabicia.model.item;
import com.example.robabicia.ui.ItemDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.itemViewHolder> {

    private ArrayList<item> itemArrayList ;
    Context context;

    public ItemListAdapter(ArrayList<item> itemArrayList, Context context) {
        this.itemArrayList = itemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, final int position) {
        holder.item_name.setText(itemArrayList.get(position).getItem_name());
        holder.item_description.setText(itemArrayList.get(position).getItem_description());
        Picasso.get().load(itemArrayList.get(position).getItem_imageUrl()).into(holder.item_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ItemDetails.class);
                intent.putExtra("Item_name",itemArrayList.get(position).getItem_name());
                intent.putExtra("description",itemArrayList.get(position).getItem_description());
                intent.putExtra("Person_name",itemArrayList.get(position).getPerson_name());
                intent.putExtra("Date",itemArrayList.get(position).getDate());
                intent.putExtra("Phone",itemArrayList.get(position).getPhone());
                intent.putExtra("price",itemArrayList.get(position).getPrice());
                intent.putExtra("Location",itemArrayList.get(position).getLocation());
                intent.putExtra("Item_imageUrl",itemArrayList.get(position).getItem_imageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder {
        TextView item_name , item_description;
        ImageView item_image;
        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name         = itemView.findViewById(R.id.item_name);
            item_description  = itemView.findViewById(R.id.item_describe);
            item_image        = itemView.findViewById(R.id.item_image);
        }
    }

    public void updateList (ArrayList<item> newList){
        itemArrayList = new ArrayList<>();
        itemArrayList.addAll(newList);
        notifyDataSetChanged();
    }
}

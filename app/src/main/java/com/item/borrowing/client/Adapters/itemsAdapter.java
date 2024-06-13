package com.item.borrowing.client.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.item.borrowing.R;
import com.item.borrowing.admin.AdminItem;
import com.item.borrowing.client.Models.itemsModels;
import com.squareup.picasso.Picasso;

public class itemsAdapter extends FirestoreRecyclerAdapter<itemsModels, itemsAdapter.myViewHolder> {

    public itemsAdapter(@NonNull FirestoreRecyclerOptions<itemsModels> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull itemsModels model) {
        holder.setData(model);
        holder.itemView.setOnClickListener(v -> {
            Intent goItem = new Intent(holder.itemView.getContext(), AdminItem.class);
            goItem.putExtra("key", model.getIic());
            holder.itemView.getContext().startActivity(goItem);
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_ui_tool_list, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, genDesc, brandModel;
        ImageView itemImage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.assetName);
            genDesc = itemView.findViewById(R.id.genDesc);
            brandModel = itemView.findViewById(R.id.brandModel);
            itemImage = itemView.findViewById(R.id.itemImage);
        }

        public void setData(itemsModels model) {
            itemName.setText(model.getAssetName());
            genDesc.setText(model.getGenSpecs());
            brandModel.setText(model.getBrandModel());
            if (model.getItemImage() == null) {
                Picasso.get().load(R.drawable.google_logo).into(itemImage);
            } else {
                Picasso.get().load(model.getItemImage()).into(itemImage);
            }
        }
    }
}

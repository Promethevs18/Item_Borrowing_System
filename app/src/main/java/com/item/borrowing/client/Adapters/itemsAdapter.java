package com.item.borrowing.client.Adapters;

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
import com.item.borrowing.client.Models.itemsModels;
import com.squareup.picasso.Picasso;

public class itemsAdapter extends FirestoreRecyclerAdapter<itemsModels, itemsAdapter.myViewHolder> {
    public itemsAdapter(@NonNull FirestoreRecyclerOptions<itemsModels> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull itemsModels itemsModels) {
        myViewHolder.setData(itemsModels);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.client_ui_tool_list, parent, false));
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, genDesc, brandModel;
        ImageView itemImage;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.assetName);
            genDesc = itemView.findViewById(R.id.genDesc);
            brandModel = itemView.findViewById(R.id.brandModel);

            itemImage = itemView.findViewById(R.id.itemImage);
        }

        public void setData(itemsModels itemsModels) {
            itemName.setText(itemsModels.getAssetName());
            genDesc.setText(itemsModels.getGenSpecs());
            brandModel.setText(itemsModels.getBrandModel());

            Picasso.get().load(itemsModels.getItemImage()).into(itemImage);
        }
    }
}

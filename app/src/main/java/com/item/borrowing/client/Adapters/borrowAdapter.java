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
import com.item.borrowing.client.Models.borrowModel;
import com.squareup.picasso.Picasso;

public class borrowAdapter extends FirestoreRecyclerAdapter<borrowModel, borrowAdapter.borrowViewHolder> {

    public borrowAdapter(@NonNull FirestoreRecyclerOptions<borrowModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull borrowAdapter.borrowViewHolder holder, int position, @NonNull borrowModel model) {
        holder.setData(model);
    }

    @NonNull
    @Override
    public borrowAdapter.borrowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new borrowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.client_ui_tool_list, parent, false));
    }

    public class borrowViewHolder extends RecyclerView.ViewHolder {
        TextView borrower, date, tools;
        ImageView picture;
        public borrowViewHolder(@NonNull View itemView) {
            super(itemView);
            borrower = itemView.findViewById(R.id.assetName);
            date = itemView.findViewById(R.id.brandModel);
            tools = itemView.findViewById(R.id.genDesc);
            picture = itemView.findViewById(R.id.itemImage);
        }

        public void setData(borrowModel model) {
            borrower.setText(model.getBorrower());
            date.setText(model.getDate());
            tools.setText(model.getTools());
            Picasso.get().load(model.getProfileImage()).into(picture);
        }
    }
}

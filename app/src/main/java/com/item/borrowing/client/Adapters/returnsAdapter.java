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
import com.item.borrowing.client.Models.returnsModel;
import com.squareup.picasso.Picasso;

public class returnsAdapter extends FirestoreRecyclerAdapter<returnsModel, returnsAdapter.myViewHolder> {

    public returnsAdapter(@NonNull FirestoreRecyclerOptions<returnsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull returnsAdapter.myViewHolder holder, int position, @NonNull returnsModel model) {
     holder.setData(model);
    }

    @NonNull
    @Override
    public returnsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.client_ui_tool_list, parent, false));
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView borrower, date, tools;
        ImageView picture;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            borrower = itemView.findViewById(R.id.assetName);
            date = itemView.findViewById(R.id.brandModel);
            tools = itemView.findViewById(R.id.genDesc);
            picture = itemView.findViewById(R.id.itemImage);
        }

        public void setData(returnsModel model) {
            borrower.setText(model.getBorrower());
            date.setText(model.getDate());
            tools.setText(model.getAssetName());
            Picasso.get().load(model.getItemImage()).into(picture);
        }
    }
}

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
import com.item.borrowing.client.Models.calibrationModels;
import com.squareup.picasso.Picasso;

public class calibrationAdapter extends FirestoreRecyclerAdapter<calibrationModels, calibrationAdapter.myViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public calibrationAdapter(@NonNull FirestoreRecyclerOptions<calibrationModels> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull calibrationAdapter.myViewHolder holder, int position, @NonNull calibrationModels model) {
        holder.setData(model);
    }

    @NonNull
    @Override
    public calibrationAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        public void setData(calibrationModels model) {
            borrower.setText(model.getInstrumentType());
            date.setText(model.getDateForCalibration());
            tools.setText(model.getStatus());
            Picasso.get().load("https://pluspng.com/img-png/tools-png-open-in-media-viewerconfiguration-tool-clipart-2000.png").into(picture);
        }
    }
}

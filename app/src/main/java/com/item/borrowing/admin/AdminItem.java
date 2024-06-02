package com.item.borrowing.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.item.borrowing.R;
import com.item.borrowing.tools.LoadingDialog;
import com.item.borrowing.tools.MessageDisplayer;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AdminItem extends AppCompatActivity {

    TextView itemName, asset, brand, generalSpecs, iicNum, location;
    ImageView resibo, piktyur;

    CollectionReference item;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        itemName = findViewById(R.id.itemName);
        asset = findViewById(R.id.asset);
        brand = findViewById(R.id.brand);
        generalSpecs = findViewById(R.id.generalSpecs);
        iicNum = findViewById(R.id.iicNum);
        location = findViewById(R.id.location);

        resibo = findViewById(R.id.resibo);
        piktyur = findViewById(R.id.piktyur);

        intent = getIntent();
        String id = intent.getStringExtra("key");

        item = FirebaseFirestore.getInstance().collection("Items");

        LoadingDialog load = new LoadingDialog(AdminItem.this, "Fetching Data...");
        load.Show();
        try{
            item.document(Objects.requireNonNull(id)).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    itemName.setText(documentSnapshot.getString("assetName"));
                    asset.setText(documentSnapshot.getString("assetName"));
                    brand.setText(documentSnapshot.getString("brandModel"));
                    generalSpecs.setText(documentSnapshot.getString("genSpecs"));
                    iicNum.setText(documentSnapshot.getString("iic"));
                    location.setText(documentSnapshot.getString("location"));


                    Picasso.get().load(documentSnapshot.getString("receiptImage")).into(resibo);
                    Picasso.get().load(documentSnapshot.getString("itemImage")).into(piktyur);
                    load.Close();
                }
            }).addOnFailureListener(e -> {
                MessageDisplayer show = new MessageDisplayer(AdminItem.this, "Data not found", "No corresponding entry was found in the database", true);
                show.show();
                load.Close();
            });
        }
        catch (Exception e) {
            MessageDisplayer show = new MessageDisplayer(AdminItem.this, "Data not found", "No corresponding entry was found in the database", true);
            show.show();
            load.Close();
        }


    }
}
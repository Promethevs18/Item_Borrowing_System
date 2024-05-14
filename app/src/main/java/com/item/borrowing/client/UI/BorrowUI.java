package com.item.borrowing.client.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.item.borrowing.R;
import com.item.borrowing.client.Models.itemsModels;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BorrowUI extends AppCompatActivity {

    TextView name, department;
    Button borrow;
    ChipGroup toolChips;
    FirebaseFirestore db;
    Query query;
    FirebaseAuth auth;
    FirebaseUser user;
    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrow_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.name);
        department = findViewById(R.id.course);
        borrow = findViewById(R.id.borrow);
        toolChips = findViewById(R.id.toolChip);
        profile = findViewById(R.id.userImage);

        db = FirebaseFirestore.getInstance();
        query = db.collection("Items");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges()){
                    itemsModels item = doc.getDocument().toObject(itemsModels.class);

                    //initialize the chip
                    Chip chip = new Chip(BorrowUI.this);
                    //bibigyan natin sya ng descriptions
                    chip.setText(item.getAssetName());

                    //set a viewGroup to handle the layout of our chips
                    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //then we will set some margins para naman may space sila from each other
                    layoutParams.setMargins(2, 2, 2, 2);
                    //next is to set the layout params to the chip
                    chip.setLayoutParams(layoutParams);
                    //letting the system generate and id
                    chip.setId(View.generateViewId());
                    //write the blueprint
                    ChipDrawable drawChip = ChipDrawable.createFromAttributes(BorrowUI.this, null, com.google.android.material.R.attr.checkedChip, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Filter);
                    //pass the blueprint to our chips
                    chip.setChipDrawable(drawChip);
                    //give the chips to the chip group
                    toolChips.addView(chip);
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();

            name.setText(user.getDisplayName());
            department.setText(user.getEmail());

        Picasso.get().load(user.getPhotoUrl()).into(profile);

    }
}
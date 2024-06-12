package com.item.borrowing.client.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.item.borrowing.R;
import com.item.borrowing.SignInInterface;
import com.item.borrowing.client.Models.itemsModels;
import com.item.borrowing.tools.DateGetter;
import com.item.borrowing.tools.LoadingDialog;
import com.item.borrowing.tools.MessageDisplayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

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

    ArrayList<String> toolsSelected;
    ArrayList<String> toolsRealName;
    String toolsBorrowed = " ";
    String toolsName = "";

    HashMap<String, String> IICs;
    DateGetter date;
    MessageDisplayer displayer;

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

        toolsSelected = new ArrayList<>();
        toolsRealName = new ArrayList<>();

        IICs = new HashMap<>();

        db = FirebaseFirestore.getInstance();
        query = db.collection("Items");

        date = new DateGetter();


        //taga kuha ng chips
        query.whereNotEqualTo("ItemExistence", "borrowed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : Objects.requireNonNull(value).getDocumentChanges()){
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

                    //add the items to the hashmap
                    IICs.put(chip.getText().toString(), item.getIic());
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


        //The picker responsible for getting the data from the chips
        toolChips.setOnCheckedStateChangeListener((group, checkedId) -> {
            toolsSelected.clear();
            toolsRealName.clear();
            int count = group.getChildCount();
            for(int i = 0; i < count; i++){
                Chip chip = (Chip) group.getChildAt(i);
                if(chip.isChecked()){
                    if(!toolsSelected.contains(String.valueOf(chip.getText()))){
                        toolsSelected.add(IICs.get(chip.getText().toString()));
                        toolsRealName.add(String.valueOf(chip.getText()));
                    }
                    else{
                        toolsSelected.remove(IICs.get(chip.getText().toString()));
                        toolsRealName.remove(String.valueOf(chip.getText()));
                    }
                }
            }
            StringJoiner pagsama = new StringJoiner(", ");
            StringJoiner names = new StringJoiner(", ");
            for (int x = 0; x < toolsSelected.size(); x++) {
                pagsama.add(toolsSelected.get(x));
                names.add(toolsRealName.get(x));
            }
            toolsBorrowed = pagsama.toString();
            toolsName = names.toString();
        });


        //The button responsible for borrowing the items
        borrow.setOnClickListener(v -> {
            LoadingDialog load = new LoadingDialog(BorrowUI.this, "Sending Request");
            load.Show();
            HashMap<String, Object> data = new HashMap<>();
            data.put("borrower", user.getDisplayName());
            data.put("tools", toolsName);
            data.put("email", user.getEmail());
            data.put("date", date.postDate());
            data.put("profileImage", user.getPhotoUrl());

            HashMap<String, Object> data2 = new HashMap<>();
            data2.put("ItemExistence", "borrowed");

            Task<DocumentSnapshot> snap = FirebaseFirestore.getInstance().collection("Counters").document("transactionCounter").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String value = documentSnapshot.get("value").toString();
                    data.put("transactionCode", value);
                    db.collection("Requests").add(data).addOnSuccessListener(documentReference -> {

                        db.collection("Users list").document(Objects.requireNonNull(user.getDisplayName())).update("borrowedStatus", "borrower").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                String[] itemNames = toolsBorrowed.split(",");

                                for(int i = 0; i < itemNames.length; i++){
                                    itemNames[i] = itemNames[i].trim();
                                }
                                for(String items: itemNames){
                                    db.collection("Items").document(items).set(data2, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            load.Close();
                                            Intent intent = new Intent(BorrowUI.this, SignInInterface.class);
                                            displayer = new MessageDisplayer(BorrowUI.this, "Request Sent","Request Sent Successfully! Please wait for an email for confirmation. For security purposes, you will be directed to the login page",true,intent);
                                            FirebaseAuth.getInstance().signOut();
                                            displayer.showAndGo();
                                        }
                                    });
                                }
                            }
                        });
                    });
                }
            });


        });
    }

}
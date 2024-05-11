package com.item.borrowing.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.item.borrowing.R;
import com.item.borrowing.SignInInterface;
import com.item.borrowing.client.Adapters.itemsAdapter;
import com.item.borrowing.client.Models.itemsModels;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Client_UI extends AppCompatActivity {

    RecyclerView listahan;
    FirebaseFirestore apoy;
    FirebaseAuth auth;
    Query query;
    TextView intro;
    CircleImageView Krizzia;
    Button GoBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listahan = findViewById(R.id.toolList);
        intro = findViewById(R.id.textView2);
        Krizzia = findViewById(R.id.krizzia);
        GoBorrow = findViewById(R.id.gotoBorrow);

        //firestore
        apoy = FirebaseFirestore.getInstance();

        //query
        query = apoy.collection("Items");


        //Creating a layoutManager
        RecyclerView.LayoutManager manager = new LinearLayoutManager(Client_UI.this, RecyclerView.VERTICAL, false);
        listahan.setLayoutManager(manager);


        //Create FirestoreRecyclerOptions
        FirestoreRecyclerOptions<itemsModels> options = new FirestoreRecyclerOptions.Builder<itemsModels>()
                .setQuery(query,itemsModels.class).build();


        //call the adapter, then set the RecyclerOptions to the adapter
        itemsAdapter adapter = new itemsAdapter(options);
        listahan.setAdapter(adapter);

        //Don't forget to use start Listening
        adapter.startListening();

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        intro.setText(String.format("Welcome, \n%s!", Objects.requireNonNull(user).getDisplayName()));
        Picasso.get().load(user.getPhotoUrl()).into(Krizzia);

        GoBorrow.setOnClickListener(v -> {
            Intent goBorrow = new Intent(Client_UI.this, BorrowUI.class);
            startActivity(goBorrow);
        });

        Krizzia.setOnClickListener(v -> {
            auth.signOut();
            Intent goSignOut = new Intent(Client_UI.this, SignInInterface.class);
            startActivity(goSignOut);
            finish();
        });
    }
}
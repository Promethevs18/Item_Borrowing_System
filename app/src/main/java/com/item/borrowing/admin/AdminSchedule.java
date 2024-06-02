package com.item.borrowing.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.item.borrowing.R;
import com.item.borrowing.tools.MessageDisplayer;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSchedule extends AppCompatActivity {

    TextView name, email, date, tools;
    CircleImageView image;

    CollectionReference collectionReference;
    String dataTaken;

    Intent intent;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();
        dataTaken = intent.getStringExtra("key");

        user = FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.asset);
        email = findViewById(R.id.brand);
        date = findViewById(R.id.generalSpecs);
        tools = findViewById(R.id.iicNum);
        image = findViewById(R.id.borrowerImage);

        collectionReference = FirebaseFirestore.getInstance().collection("Requests");
        try{
            collectionReference.document(dataTaken).get().addOnSuccessListener(v -> {
                if (v.exists()) {
                    name.setText(v.getString("borrower"));
                    email.setText(v.getString("email"));
                    date.setText(v.getString("date"));
                    tools.setText(v.getString("tools"));
                    Picasso.get().load(v.getString("profileImage")).into(image);
                }
                else{
                    MessageDisplayer show = new MessageDisplayer(AdminSchedule.this, "Data not found", "No corresponding entry was found in the database", true);
                    show.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MessageDisplayer show = new MessageDisplayer(AdminSchedule.this, "Data not found", "No corresponding entry was found in the database", true);
                    show.show();
                }
            });
        }
        catch (Exception e){
            MessageDisplayer show = new MessageDisplayer(AdminSchedule.this, "Data not found", "No corresponding entry was found in the database", true);
            show.show();
        }



    }
}
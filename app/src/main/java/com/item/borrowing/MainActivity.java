package com.item.borrowing;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button button;
    FirebaseFirestore apoy = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //Do not touch
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Write code here =>

        button = findViewById(R.id.button);
    }

    @Override
    protected void onStart() {
        super.onStart();

        button.setOnClickListener(v -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("First Name", "Von");
            mapa.put("Last Name", "Abuloc");

            apoy.collection("sampleUser").add(mapa).addOnSuccessListener(reprense -> {
                Toast.makeText(this, String.format("Task success! {0}", reprense.getId()), Toast.LENGTH_SHORT).show();
            });
        });
    }
}
package com.item.borrowing.client.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.item.borrowing.R;
import com.item.borrowing.SignInInterface;
import com.item.borrowing.tools.LoadingDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Account_Customization extends AppCompatActivity {

    CircleImageView profileImage;
    TextInputLayout name, email, phone, department;
    TextInputEditText user_name, user_email, user_phone, user_department;
    Button save, logout;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_account_customization);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        profileImage = findViewById(R.id.userPhoto);

        name = findViewById(R.id.nameLayout);
        email = findViewById(R.id.emailLayout);
        phone = findViewById(R.id.phoneLayout);
        department = findViewById(R.id.departmentLayout);

        user_name = findViewById(R.id.nameEditText);
        user_email = findViewById(R.id.emailEditText);
        user_phone = findViewById(R.id.phoneEditText);
        user_department = findViewById(R.id.departmentEditText);

        save = findViewById(R.id.updateData);
        logout = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        user_name.setText(user.getDisplayName());
        user_email.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(profileImage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //EditText textwatcher
        user_department.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //update data
        save.setOnClickListener(v -> {
            LoadingDialog load = new LoadingDialog(this, "Updating data...");
            load.Show();

            HashMap<String, Object> detailsMap = new HashMap<>();
            detailsMap.put("name", Objects.requireNonNull(user_name.getText()).toString());
            detailsMap.put("email", Objects.requireNonNull(user_email.getText()).toString());
            detailsMap.put("phone", Objects.requireNonNull(user_phone.getText()).toString());
            detailsMap.put("department", Objects.requireNonNull(user_department.getText()).toString());

            db.collection("Users list").document(user_name.getText().toString()).update(detailsMap).addOnSuccessListener(aVoid -> {
                load.Close();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Success!");
                builder.setCancelable(true);
                builder.setMessage("Data updated successfully! \nClick anywhere to continue");
                builder.setIcon(R.drawable.success);
                builder.show();
                save.setVisibility(View.GONE);

            });
        });

        //logout button
        logout.setOnClickListener(v -> {
            auth.signOut();
            finish();
            Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(User_Account_Customization.this, SignInInterface.class));
        });
    }
}
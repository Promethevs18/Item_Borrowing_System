package com.item.borrowing.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.item.borrowing.R;
import com.item.borrowing.SignInInterface;
import com.item.borrowing.tools.Capture;
import com.item.borrowing.tools.MessageDisplayer;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

public class Admin_UI extends AppCompatActivity {


    Button logout, item, sched;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logout = findViewById(R.id.adminOut);
        item = findViewById(R.id.item);
        sched = findViewById(R.id.schedule);


        FirebaseAuth auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        item.setOnClickListener(v -> {
            Scanner();
        });
        sched.setOnClickListener(v -> {
            Scanner();
        });
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });
    }
    public void Scanner(){
        ScanOptions option = new ScanOptions();
        option.setPrompt("Scan Item QR Code\n\nPress Volume up to turn on light, press volume down to turn it off");
        option.setBeepEnabled(true);
        option.setCaptureActivity(Capture.class);
        scanLauncher.launch(option);
    }
    ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            String[] scanResult = result.getContents().split(": ");

            if(Objects.equals(scanResult[0], "Item")){
                intent = new Intent(Admin_UI.this, AdminItem.class);
                intent.putExtra("key", scanResult[1]);
                startActivity(intent);
            }
            else if(Objects.equals(scanResult[0], "Schedule")){
                intent = new Intent(Admin_UI.this, AdminSchedule.class);
                intent.putExtra("key", scanResult[1]);
                startActivity(intent);
            }
            else{
                MessageDisplayer showMessage = new MessageDisplayer(Admin_UI.this, "Invalid QR Code", "QR scanned is not a valid QR code", true);
                showMessage.show();
            }
        }
    });
}
package com.item.borrowing.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.item.borrowing.R;
import com.item.borrowing.SignInInterface;
import com.item.borrowing.client.Adapters.borrowAdapter;
import com.item.borrowing.client.Adapters.calibrationAdapter;
import com.item.borrowing.client.Adapters.itemsAdapter;
import com.item.borrowing.client.Adapters.returnsAdapter;
import com.item.borrowing.client.Models.borrowModel;
import com.item.borrowing.client.Models.calibrationModels;
import com.item.borrowing.client.Models.itemsModels;
import com.item.borrowing.client.Models.returnsModel;
import com.item.borrowing.tools.Capture;
import com.item.borrowing.tools.MessageDisplayer;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

public class Admin_UI extends AppCompatActivity {


    Button logout, item, sched;
    Intent intent;
    RecyclerView items, returns, borrow, calibration;
    FirebaseFirestore db;

    itemsAdapter adapter;
    returnsAdapter adapter2;
    borrowAdapter adapter3;
    calibrationAdapter adapter4;
    TextView caliText;
    Intent intent2;
    ScrollView scrollView;

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

        items = findViewById(R.id.itemRecycler);
        returns = findViewById(R.id.returnRecycler);
        borrow = findViewById(R.id.borrowRecycler);
        calibration = findViewById(R.id.calibrationRecycler);

        caliText = findViewById(R.id.textView6);
        scrollView = findViewById(R.id.calibrationScroll);


        intent2 = getIntent();

        if(Objects.requireNonNull(intent2.getStringExtra("admin")).contains("user")){
            caliText.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }

        db = FirebaseFirestore.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();


        //Layouts
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Admin_UI.this, RecyclerView.VERTICAL, false);
        items.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Admin_UI.this, RecyclerView.VERTICAL, false);
        returns.setLayoutManager(layoutManager2);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(Admin_UI.this, RecyclerView.VERTICAL, false);
        borrow.setLayoutManager(layoutManager3);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(Admin_UI.this, RecyclerView.VERTICAL, false);
        calibration.setLayoutManager(layoutManager4);

        //Firestores
        FirestoreRecyclerOptions<itemsModels> gamit = new FirestoreRecyclerOptions.Builder<itemsModels>()
                .setQuery(db.collection("Items"), itemsModels.class)
                .build();
        FirestoreRecyclerOptions<returnsModel> balik = new FirestoreRecyclerOptions.Builder<returnsModel>()
                .setQuery(db.collection("Returned Items"), returnsModel.class)
                .build();
        FirestoreRecyclerOptions<borrowModel> hiram = new FirestoreRecyclerOptions.Builder<borrowModel>()
                .setQuery(db.collection("Requests"), borrowModel.class)
                .build();
        FirestoreRecyclerOptions<calibrationModels> calib = new FirestoreRecyclerOptions.Builder<calibrationModels>()
                .setQuery(db.collection("Calibrated Items"), calibrationModels.class)
                .build();

        //adapters
        adapter = new itemsAdapter(gamit);
        adapter2 = new returnsAdapter(balik);
        adapter3 = new borrowAdapter(hiram);
        adapter4 = new calibrationAdapter(calib);

        //use adapters
        items.setAdapter(adapter);
        returns.setAdapter(adapter2);
        borrow.setAdapter(adapter3);
        calibration.setAdapter(adapter4);

        //listen
        adapter.startListening();
        adapter2.startListening();
        adapter3.startListening();
        adapter4.startListening();


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
            Intent goHome = new Intent(Admin_UI.this, SignInInterface.class);
            startActivity(goHome);
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
package com.item.borrowing;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.PublicKeyCredential;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SignInInterface extends AppCompatActivity {

    Button button;

    FirebaseFirestore apoy = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin_activity);
        //Do not touch
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Write code here =>

        button = findViewById(R.id.google_sign);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //First, let's call the necessary variables
        CancellationSignal cancellationSignal = new CancellationSignal();
        Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;

        //First get the GoogleIdOption First
        GetGoogleIdOption idOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)       //serverClientID is yung nasa Credentials (yung web service)
                .setServerClientId("425070832005-dttif4dcpc7qfb8ktehri5k5s3cpr9m6.apps.googleusercontent.com")
                .setAutoSelectEnabled(true)
                .build();

        //Next, declare a credentialManager
        CredentialManager credentialManager = CredentialManager.create(SignInInterface.this);

        //Then, create a GetCredentialRequest
        GetCredentialRequest credRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(idOption)
                        .build();

        //When the button is pressed
        button.setOnClickListener(v -> {
            Toast.makeText(this, "Starting now", Toast.LENGTH_SHORT).show();
            credentialManager.getCredentialAsync(
                        SignInInterface.this, credRequest, cancellationSignal, executor, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                        @Override
                        public void onResult(GetCredentialResponse getCredentialResponse) {
                            handleSignIn(getCredentialResponse);
                        }
                        @Override
                        public void onError(@NonNull GetCredentialException e) {
                            Log.e("An error happened", e.getMessage());
                        }
                    }
            );

        });
    }

    //The method that handles the GoogleSignIn using the new Credential Manager
    private void handleSignIn(GetCredentialResponse getCredentialResponse) {
        Credential creds = getCredentialResponse.getCredential();

        if(creds instanceof PublicKeyCredential){
            String responseJson = ((PublicKeyCredential) creds).getAuthenticationResponseJson();
        }
        else if(creds instanceof CustomCredential){
            if(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(creds.getType())){
                try{
                    GoogleIdTokenCredential googol = GoogleIdTokenCredential.createFrom(((CustomCredential) creds).getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInInterface.this, googol.getDisplayName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                   Log.e("Error", e.getMessage());
                }
            }
            else{
                Log.e("Invalid Google", "Invalid Google Credentials was found");
            }
        }
        else {
            Log.e("Invalid Creds", "Invalid Creds");
        }

    }
}
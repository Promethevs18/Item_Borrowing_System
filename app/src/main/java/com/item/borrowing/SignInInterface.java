package com.item.borrowing;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class SignInInterface extends AppCompatActivity {

    Button button;
    FirebaseAuth userAuth;
    FirebaseUser currentUser;

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
        userAuth = FirebaseAuth.getInstance();

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
                .setServerClientId(getResources().getString(R.string.web_client_id))
                .setAutoSelectEnabled(true)
                .build();

        //Next, declare a credentialManager
        CredentialManager credentialManager = CredentialManager.create(SignInInterface.this);

        //Then, create a GetCredentialRequest
        GetCredentialRequest credRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(idOption)
                        .build();

        currentUser = userAuth.getCurrentUser();


        //When the Continue with Google button is pressed
        button.setOnClickListener(v -> {
            if(currentUser != null){
                Toast.makeText(this, "User is currently signed in", Toast.LENGTH_SHORT).show();
            }
            else{
                credentialManager.getCredentialAsync(
                        SignInInterface.this, credRequest, cancellationSignal, executor, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                            @Override
                            public void onResult(GetCredentialResponse getCredentialResponse) {
                                handleSignIn(getCredentialResponse);
                            }
                            @Override
                            public void onError(@NonNull GetCredentialException e) {
                                Log.e("An error happened", e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignInInterface.this, String.format("An error occurred due to: \n{0}", e.getMessage()), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
            }
        });
    }

    //The method that handles the GoogleSignIn using the new Credential Manager
    private void handleSignIn(GetCredentialResponse getCredentialResponse) {

        //Call a credential object
        Credential creds = getCredentialResponse.getCredential();

        //check if the object is an instance of the three
        if(creds instanceof PublicKeyCredential){
            String responseJson = ((PublicKeyCredential) creds).getAuthenticationResponseJson();
        }
        else if(creds instanceof CustomCredential){
            //if the object is a customCredential (like a third-party provider)
            //kunin ang object, then i compare sya sa GoogleIdTokenCredential
            if(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(creds.getType())){
                try{
                    //If nag-match, kunin natin ang buong data from the GoogleIdTokenCredential
                    GoogleIdTokenCredential googol = GoogleIdTokenCredential.createFrom(((CustomCredential) creds).getData());

                    //Now, we are going to use the token to sign in to our Firebase Authentication
                    SignInToFirebase(googol);

                }catch (Exception e){
                   Log.e("Error", Objects.requireNonNull(e.getMessage()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInInterface.this, String.format("An error occurred due to: \n{0}", e.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else{
                Log.e("Invalid Google", "Invalid Google Credentials was found");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignInInterface.this, "Invalid Google credential detected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            Log.e("Invalid Creds", "Invalid Creds");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SignInInterface.this, "Invalid credential provider was detected", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //method that handles sign in to firebase using Google ID Token
    private void SignInToFirebase(GoogleIdTokenCredential googol) {
        if(googol != null){
            AuthCredential fireCreds = GoogleAuthProvider.getCredential(googol.getIdToken(), null);
            userAuth.signInWithCredential(fireCreds).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignInInterface.this, authResult.getUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Something went wrong with requesting the Sign in to Firebase", Toast.LENGTH_SHORT).show();
        }
    }
}
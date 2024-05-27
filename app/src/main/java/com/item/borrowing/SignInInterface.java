package com.item.borrowing;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.item.borrowing.admin.Admin_UI;
import com.item.borrowing.client.UI.Client_UI;
import com.item.borrowing.tools.LoadingDialog;
import com.item.borrowing.tools.MessageDisplayer;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;

public class SignInInterface extends AppCompatActivity {

    Button button, login;
    FirebaseAuth userAuth;
    FirebaseUser currentUser;
    TextInputLayout forEmail, forPass;
    TextInputEditText email, pass;
    LoadingDialog load;

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
        login = findViewById(R.id.login);

        userAuth = FirebaseAuth.getInstance();

        forEmail = findViewById(R.id.textLayout_user);
        forPass = findViewById(R.id.textLayout_pass);

        email = findViewById(R.id.textInput_user);
        pass = findViewById(R.id.textInput_pass);

        //getting the current user from Firebase
        currentUser = userAuth.getCurrentUser();

        if(currentUser != null){
            for(UserInfo info : currentUser.getProviderData()){
                Intent goSomewhere;
                if(info.getProviderId().contains("google")){
                    goSomewhere = new Intent(SignInInterface.this, Client_UI.class);
                    startActivity(goSomewhere);
                }
                else{
                    goSomewhere = new Intent(SignInInterface.this, Admin_UI.class);
                    startActivity(goSomewhere);
                }
            }
        }
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


        //When the Continue with Google button is pressed
        button.setOnClickListener(v -> {
            load = new LoadingDialog(SignInInterface.this, "Verifying credentials");
            load.Show();
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
        });

        login.setOnClickListener(v -> {
            load = new LoadingDialog(SignInInterface.this, "Verifying credentials");
            load.Show();
            if(email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                forEmail.setError("Fields are required");
                forPass.setError("Fields are required");
            }
            else{
                userAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent goAdmin = new Intent(SignInInterface.this, Admin_UI.class);
                            startActivity(goAdmin);
                        }
                        else{
                            forEmail.setError("Credentials do not match");
                            forPass.setError("Credentials do not match");
                        }
                    }
                });
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
                    GoogleIdTokenCredential googol = GoogleIdTokenCredential.createFrom((creds).getData());

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
                    HashMap<String, Object> userInfo = new HashMap<>();
                    userInfo.put("fullName", authResult.getUser().getDisplayName());
                    userInfo.put("email", authResult.getUser().getEmail());
                    userInfo.put("profileImage", authResult.getUser().getPhotoUrl());
                    userInfo.put("accountLevel", "user");
                    userInfo.put("status", "Activated");

                    apoy.collection("Users list").document(Objects.requireNonNull(Objects.requireNonNull(authResult.getUser()).getDisplayName())).update(userInfo).addOnSuccessListener(unused -> {
                        load.Close();
                        Intent goUser = new Intent(SignInInterface.this, Client_UI.class);
                        startActivity(goUser);
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MessageDisplayer message = new MessageDisplayer(SignInInterface.this, "Cannot sign in","I can't sign you in at the moment. Try later",true);
                        }
                    });

                }
            });
        }
        else {
            Toast.makeText(this, "Something went wrong with requesting the Sign in to Firebase", Toast.LENGTH_SHORT).show();
        }
    }

}
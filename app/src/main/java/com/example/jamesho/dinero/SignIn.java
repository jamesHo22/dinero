package com.example.jamesho.dinero;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignIn extends AppCompatActivity {

    // the requestCode for the startActivityForResult in the google sign in OnClick
    static final int RC_SIGN_IN = 1;
    private final String TAG = SignIn.class.getName();
    static final int SIGN_IN = 0;
    static final int SIGN_OUT = 2;

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(SIGN_IN);
    }

    private void updateUI(int signInOrOut) {
        TextView signedIn = findViewById(R.id.sign_in_status);
        TextView userInfo = findViewById(R.id.user_info);

        switch (signInOrOut) {
            case SIGN_IN:
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personGivenName = acct.getGivenName();
                    String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();
                    userInfo.setText(personName + " " +
                            personGivenName + " " +
                            personFamilyName + " " +
                            personEmail + " " +
                            personId + " " +
                            personPhoto);
                }
                signedIn.setText("You are now signed in!");
                break;
            case SIGN_OUT:
                signedIn.setText("You have signed out");
                userInfo.setText("");
        }

        // this activity will open up the MainActivity so the user only needs to sign in once.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Create the google sign in object to request user information upon sign in.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient;
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Do something when the sign-in button is clicked
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Sign out button
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(SIGN_OUT);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(SIGN_IN);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(SIGN_OUT);
        }
    }
}

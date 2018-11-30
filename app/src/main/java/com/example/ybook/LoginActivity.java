package com.example.ybook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.util.StringValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Class responsible for logging the user to the application.
 * @author Cesar Sales, David Souza, Evan Harrison
 * @version 1.0
 * @since November, 12, 2018
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText email, password;
    private Button login, signUp;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get a instance of firebase library
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.loginSignUpButton);
        forgotPassword = findViewById(R.id.loginForgotPassword);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class );
                startActivity(intent);
            }
        });

        /* Call SendNewPasswordLinkActivity.  */
        forgotPassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SendNewPasswordLinkActivity.class );
                startActivity(intent);
            }
        });

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, SendNewPasswordLinkActivity.class));
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.i(TAG, "Signing out");
        //todo remove!
        signOut();
    }

    /**
     * Sign in user with firebase.
     * @param email
     * @param password
     */
    public void signIn(String email, String password) {
        Log.i(TAG, "Validating input");
        if(!isValidUserInput(email, password)) {
            return;
        }

        Log.i(TAG, "Signing in");
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this,
                                BookListActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void signOut() {
        mAuth.signOut();
    }

    /**
     * Validate user input.
     * @param email
     * @param password
     * @return true if is valid input
     */
    private boolean isValidUserInput(String email, String password) {
        try {
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email address",
                        Toast.LENGTH_LONG).show();
                return false;
            }

            StringValidation.isValidEmail(email);

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your password",
                        Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (StringFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}


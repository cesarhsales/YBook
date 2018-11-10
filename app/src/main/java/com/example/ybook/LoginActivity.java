package com.example.ybook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


public class LoginActivity extends AppCompatActivity {
    private EditText Email, Password;
    private Button Login, SignUp;
    private TextView ForgotPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get a instance of firebase library
        mAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.loginEmail);
        Password = findViewById(R.id.loginPassword);
        Login = findViewById(R.id.loginButton);
        SignUp = findViewById(R.id.loginSignUpButton);
        ForgotPassword = findViewById(R.id.loginForgotPassword);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class );
                startActivity(intent);
            }
        });


        /* Call SendNewPasswordLinkActivity.  */
        ForgotPassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SendNewPasswordLinkActivity.class );
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, SendNewPasswordLinkActivity.class));
                }
            }
        };


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void startSignIn() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(isValidUserInput(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                       // Log.w("HOWDY", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

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

    /**
     * Authenticate the user w/ email and password
     * @param user
     */
    public void authenticateUser (User user) {

    }
}


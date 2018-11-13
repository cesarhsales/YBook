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
import android.widget.Toast;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.customexceptions.StringInvalidCharactersException;
import com.example.ybook.customexceptions.StringLengthException;
import com.example.ybook.util.StringValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText username, email, password, confirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.signUpUsername);
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        confirmPassword = findViewById(R.id.signUpConfirmPassword);
        Button signup = findViewById(R.id.signUpButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        confirmPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createAccount(String username, String email, String password,
                              String confirmPassword) {
        Log.i(TAG, "Validating input");
        if (!isValidUserInput(username, email, password, confirmPassword)) {
            return;
        }

        Log.i(TAG, "Creating user with email and password");
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(SignupActivity.this, user.getDisplayName(),
                    Toast.LENGTH_SHORT).show();
        }
        /*
        Intent intent = new Intent(SignupActivity.this, BookListActivity.class);
        startActivity(intent);
        */
    }

    private boolean isValidUserInput(String username, String email, String password,
                                     String confirmPassword) {
        try {
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please enter your username",
                        Toast.LENGTH_LONG).show();
                return false;
            }

            StringValidation.isValidUsername(username);

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

            StringValidation.isValidPasswordLength(password);

            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please confirm your password",
                        Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (StringFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } catch (StringInvalidCharactersException e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return false;
        } catch (StringLengthException e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords don't match",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}


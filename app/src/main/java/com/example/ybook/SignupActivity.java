package com.example.ybook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignupActivity extends AppCompatActivity {
    private EditText username, email, password, confirmPassword;
    private Button signup;
    private FirebaseAuth mAuth;
    private static final String TAG = "Sign Up Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.signUpUsername);
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        confirmPassword = findViewById(R.id.signUpConfirmPassword);
        signup = findViewById(R.id.signUpButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    public void startSignIn() {
        if (isValidUserInput(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                confirmPassword.getText().toString())) {
            /*
            mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                            Log.d(TAG, "onComplete: malformed_email");
                                            Toast.makeText(SignupActivity.this, "E-mail inválido",
                                                    Toast.LENGTH_LONG).show();
                                            // TODO: Take your action
                                        } catch (FirebaseAuthUserCollisionException existEmail) {
                                            Log.d(TAG, "onComplete: exist_email");
                                            Toast.makeText(SignupActivity.this, "E-mail já existe",
                                                    Toast.LENGTH_LONG).show();
                                            // TODO: Take your action
                                        } catch (Exception e) {
                                            Log.d(TAG, "onComplete: " + e.getMessage());
                                        }
                                    }
                                }
                            });
                            */
        }
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


package com.example.ybook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {
    private EditText username, email, password, confirmPasswprd;
    private Button signup;
    private FirebaseAuth mAuth;
    private static final String TAG = "Sign Up Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.etUsername);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPasswprd = (EditText) findViewById(R.id.etConfirmPassword);
        signup = (Button) findViewById(R.id.btnSignup);

        String _email = email.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String cPassword = confirmPasswprd.getText().toString().trim();
        if (!_password.equals(cPassword)) {
            Toast.makeText(SignupActivity.this, "Password don't match",
                    Toast.LENGTH_LONG).show();
            }
        else {
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
        }
    }
}

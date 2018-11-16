package com.example.ybook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.util.StringValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SendNewPasswordLinkActivity extends AppCompatActivity {
    String TAG = "SendNewPasswordLinkActivity";
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_password_link);

        Button sendLink = findViewById(R.id.sendLinkButton);
        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmailFormat();
            }
        });
    }

    private void validateEmailFormat() {
         email = findViewById(R.id.sendLinkEmail);


        try {
            StringValidation.isValidEmail(email.getText().toString());
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(SendNewPasswordLinkActivity.this,
                                        "Email sent.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (StringFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

package com.example.ybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ybook.customexceptions.StringFormatException;
import com.example.ybook.util.StringValidation;

public class SendNewPasswordLinkActivity extends AppCompatActivity {

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
        EditText email = findViewById(R.id.sendLinkEmail);

        try {
            StringValidation.isValidEmail(email.getText().toString());
            Toast.makeText(this, "Email sent", Toast.LENGTH_LONG).show();
        } catch (StringFormatException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

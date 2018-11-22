package com.example.ybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SharedHeader extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        ImageView profileImage = findViewById(R.id.profile_image);

        Picasso.get()
                .load("C:\\Users\\cesar\\Desktop\\CS246")
                .placeholder(R.drawable.circle)
                .into(profileImage);
                */
    }
}

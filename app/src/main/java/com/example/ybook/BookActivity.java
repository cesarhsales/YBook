package com.example.ybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //MOCKS FOR TESTING PURPOSES
        Book book1 = new Book("Star Wars", "Fantasy", "George Lucas", 1980,
                300, "No comments.", true, 1);

        Book book2 = new Book("Star Wars 2", "Fantasy", "George Lucas", 1982,
                300, "No comments.", false, 2);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        //GET CURRENT USER FROM FIREBASE AND SET A NEW USER
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User("cesarhs", currentUser.getEmail(), books);

        //GET FIREBASE REFERENCE AND STORE A VALUE USING SETVALUE()
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(currentUser.getUid()).setValue(user);
    }
}

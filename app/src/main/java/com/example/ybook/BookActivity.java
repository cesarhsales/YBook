package com.example.ybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    private EditText Title, Type, Author, Year, NumPages, Comments;
    private Button SaveBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Title = (EditText) findViewById(R.id.bookTitle);
        Type = (EditText) findViewById(R.id.bookType);
        Author = (EditText) findViewById(R.id.bookAuthor);
        Year = (EditText) findViewById(R.id.bookYearPublished);
        NumPages = (EditText) findViewById(R.id.bookNumPages);
        Comments = (EditText) findViewById(R.id.bookComments);
        SaveBook = (Button) findViewById(R.id.bookSave);



        SaveBook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 List<Book> books = new ArrayList<Book>();

                 //parsing Year and NumPages to int to fit inside bookClass
                 int _year = Integer.parseInt(Year.getText().toString());
                 int _numPages = Integer.parseInt(NumPages.getText().toString());

                 Book book = new Book(Comments.getText().toString(), Title.getText().toString(),
                         Type.getText().toString(), Author.getText().toString(), _year,
                         _numPages, false);

                 books.add(book);

                 //set values to the user class
                 User user = null;
                 FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                 user.setUID(currentFirebaseUser.getUid());
                 user.setBooks(books);

                 // Write a message to the database
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference("user");

                 myRef.setValue("user");
             }
         });
    }
}

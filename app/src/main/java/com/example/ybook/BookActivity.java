package com.example.ybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    private EditText title, type, author, year, numPages, comments;
    private Button SaveBook;
    private Book book;
    private CheckBox read;
    private FirebaseAuth mAuth;
    // [START declare_database_ref]

    FirebaseDatabase mDatabase;
    DatabaseReference user;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        title = (EditText) findViewById(R.id.bookTitle);
        type = (EditText) findViewById(R.id.bookType);
        author = (EditText) findViewById(R.id.bookAuthor);
        year = (EditText) findViewById(R.id.bookYearPublished);
        numPages = (EditText) findViewById(R.id.bookNumPages);
        comments = (EditText) findViewById(R.id.bookComments);
        SaveBook = (Button) findViewById(R.id.bookSave);
        read = findViewById(R.id.checkRead);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        user = mDatabase.getReference("User");


        //Close activity if user not logged in
        if(mAuth.getCurrentUser() == null){
            finish();
        }


        SaveBook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 addBook(title.getText().toString(), type.getText().toString(), author.getText().toString(),
                         year.getText().toString(), numPages.getText().toString(), comments.getText().toString());
                 FirebaseUser firebaseUser = mAuth.getCurrentUser();
                 user.child(firebaseUser.getUid()).setValue(books);
             }
         });
    }


    List<Book> books = new ArrayList<Book>();
    /**
     * Create a new book to be added to book list.
     * @param title
     * @param type
     * @param author
     * @param year
     * @param pages
     * @param comments
     * @return Book object newBook
     */
    public void addBook(String title, String type, String author,
                        String year, String pages, String comments) {



        if (title.isEmpty()) {
            Toast.makeText(BookActivity.this, "Please enter the title of the book.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Create new book w/ values
        book = new Book(title, type, author, year, pages, comments);
        if (read.isChecked()) {
            Toast.makeText(BookActivity.this, "Checked.",
                    Toast.LENGTH_LONG).show();
            book.setRead(true);
        } else {
            book.setRead(false);
        }


        //Begin work with firebase

        books.add(book);







        //set values to the user class
        //User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
       // user.setUID(firebaseUser.getUid());
        //user.setBooks(books);

        // This will need to be changed, was just using it to see if I could get
        // the database to store data... didn't quite get far enough to finish yet.
        //databaseReference.child("user").child(firebaseUser.getUid()).setValue(book);
        // Just to make sure correct UID is accessed.
        // Toast.makeText(BookActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
        Toast.makeText(BookActivity.this, "Book saved.", Toast.LENGTH_LONG).show();
    }
}

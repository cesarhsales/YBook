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
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private EditText title, type, author, year, numPages, comments;
    private Button SaveBook;
    private Book book;
    private CheckBox read;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        /*
        //MOCKS FOR TESTING PURPOSES
        Book book1 = new Book("Star Wars", "Fantasy", "George Lucas", 1980,
                300, "No comments.", true, 1);

        Book book2 = new Book("Star Wars 2", "Fantasy", "George Lucas", 1982,
                300, "No comments.", false, 2);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        */

        title = (EditText) findViewById(R.id.bookTitle);
        type = (EditText) findViewById(R.id.bookType);
        author = (EditText) findViewById(R.id.bookAuthor);
        year = (EditText) findViewById(R.id.bookYearPublished);
        numPages = (EditText) findViewById(R.id.bookNumPages);
        comments = (EditText) findViewById(R.id.bookComments);
        SaveBook = (Button) findViewById(R.id.bookSave);
        read = findViewById(R.id.checkRead);
        mAuth = FirebaseAuth.getInstance();

        //Close activity if user not logged in
        if(mAuth.getCurrentUser() == null){
            finish();
        }

        SaveBook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 List<Book> books = new ArrayList<>();
                 books.add(addBook(title.getText().toString(), type.getText().toString(),
                         author.getText().toString(),
                         Integer.parseInt(year.getText().toString()),
                         Integer.parseInt(numPages.getText().toString()),
                         comments.getText().toString()));

                 //GET CURRENT USER FROM FIREBASE AND SET A NEW USER
                 currentUser = FirebaseAuth.getInstance().getCurrentUser();
                 User user = new User("cesarhs", currentUser.getEmail(), books);

                 //GET FIREBASE REFERENCE AND STORE A VALUE USING SETVALUE()
                 databaseReference = FirebaseDatabase.getInstance().getReference();
                 databaseReference.child("users").child(currentUser.getUid()).setValue(user);

                 Toast.makeText(BookActivity.this, "Book saved.", Toast.LENGTH_LONG).show();
             }
         });
    }

    List<Book> books = new ArrayList<>();
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
    public Book addBook(String title, String type, String author,
                        int year, int pages, String comments) {

        if (title.isEmpty()) {
            Toast.makeText(BookActivity.this, "Please enter the title of the book.",
                    Toast.LENGTH_LONG).show();
            return null;
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

        book.setPosition(1);

        return book;
    }
}

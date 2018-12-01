package com.example.ybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ybook.customexceptions.CharacterLengthException;
import com.example.ybook.util.StringValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private List<Book> books;
    private User user;

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
        books = new ArrayList<>();

        //GET CURRENT USER AND FIREBASE REFERENCES
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //THIS EVENT LISTENER IS ALSO RESPONSIBLE FOR CHECKING FOR CHANGES,
        ValueEventListener preListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // UPDATE BOOKLIST FROM USER OBJECT USING
                // BOOKS STORED IN FIREBASE
                user = dataSnapshot.child("users").child(currentUser.getUid())
                        .getValue(User.class);

                // USER WILL BE NULL UNTIL FIRST BOOK IS ADDED TO FIREBASE
                // NEED TO ACCOUNT FOR THAT
                if (user != null) {
                    Log.i("howdy", "User:" + user.getEmail());
                    books = user.getBooks();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("BookListActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //Add the listener to firebase reference
        databaseReference.addValueEventListener(preListener);


        //Close activity if user not logged in
        if(mAuth.getCurrentUser() == null){
            finish();
        }

        SaveBook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (isValidInput(title.getText().toString(), type.getText().toString(),
                         author.getText().toString(), year.getText().toString(),
                         numPages.getText().toString(), comments.getText().toString())){

                     books.add(addBook(title.getText().toString(), type.getText().toString(),
                             author.getText().toString(), year.getText().toString(),
                             numPages.getText().toString(), comments.getText().toString()));

                     //SET NEW USER
                     User user = new User(currentUser.getDisplayName(), currentUser.getEmail(), books);

                     //GET FIREBASE REFERENCE AND STORE A VALUE USING SETVALUE()
                     databaseReference = FirebaseDatabase.getInstance().getReference();
                     databaseReference.child("users").child(currentUser.getUid()).setValue(user);

                     Toast.makeText(BookActivity.this, "Book saved.", Toast.LENGTH_LONG).show();
                 }
             }
         });
    }

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
                        String year, String pages, String comments) {

        if(isValidInput(title, type, author, pages, year, comments)) {
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
        } else {
            return null;
        }
    }

    /**
     * Check user input to ensure no field exceeds 200 characters.
     * Ensure at least title and author field are filled out.
     * @param title
     * @param type
     * @param author
     * @param pages
     * @param year
     * @param comments
     * @return
     */
    public boolean isValidInput(String title, String type, String author, String pages, String year,
                                String comments){
        try {
            // REQUIRE ONLY TITLE AND AUTHOR
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Please enter book title.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(author)) {
                Toast.makeText(this, "Please enter author of book.",
                        Toast.LENGTH_LONG).show();
                return false;
            }

            // ENFORCE CHARACTER LIMIT ON ALL FIELDS
            StringValidation.isValidCharacterCount(title);
            StringValidation.isValidCharacterCount(type);
            StringValidation.isValidCharacterCount(author);
            StringValidation.isValidCharacterCount(pages);
            StringValidation.isValidCharacterCount(year);
            StringValidation.isValidCharacterCount(comments);

        } catch (CharacterLengthException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}

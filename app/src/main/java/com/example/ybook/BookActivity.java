package com.example.ybook;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ybook.customexceptions.CharacterLengthException;
import com.example.ybook.util.StringValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class responsible for displaying the screen to add, edit and delete a book
 * @author Cesar Sales, David Souza, Evan Harrison
 * @version 1.0
 * @since December, 15, 2018
 */
public class BookActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private EditText title, type, author, year, numPages, comments;
    private Button SaveBook, DeleteBook;
    private Book book;
    private CheckBox favorite;
    private FirebaseAuth mAuth;
    private List<Book> books;
    private User user;
    int position;
    private boolean isEdit;

    //FIREBASE VARIABLES
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // RECEIVE POSITION FROM LIST IN BOOKLISTACTIVITY TO ALLOW BOOK EDITING
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        Log.i("Position", String.valueOf(position));

        // INITIALIZE FIELDS
        title = (EditText) findViewById(R.id.bookTitle);
        type = (EditText) findViewById(R.id.bookType);
        author = (EditText) findViewById(R.id.bookAuthor);
        year = (EditText) findViewById(R.id.bookYearPublished);
        numPages = (EditText) findViewById(R.id.bookNumPages);
        comments = (EditText) findViewById(R.id.bookComments);
        SaveBook = (Button) findViewById(R.id.bookSave);
        DeleteBook = (Button) findViewById(R.id.bookDelete);
        favorite = findViewById(R.id.checkFavorite);
        mAuth = FirebaseAuth.getInstance();
        books = new ArrayList<>();
        isEdit = false;

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

                //Set username in shared header
                TextView sharedUsername = findViewById(R.id.include).findViewById(R.id.sharedUsername);
                sharedUsername.setText(currentUser.getDisplayName());

                // USER WILL BE NULL UNTIL FIRST BOOK IS ADDED TO FIREBASE
                // NEED TO ACCOUNT FOR THAT
                if (user != null) {
                    // SET USERNAME IN SHARED HEADER
                    if(!user.getUsername().isEmpty()) {
                        sharedUsername.setText(user.getUsername());
                    }

                    Log.i("BookListActivity", "User:" + user.getEmail());
                    if(user.getBooks() != null) {
                        // SET VALUES OF BOOK LIST TO STORED USER BOOK LIST
                        books = user.getBooks();
                    }

                    // LOAD CLICKED BOOK IN LIST ACCORDING TO POSITION IN LIST
                    // BUT ONLY IF BOOK IS ACCESSED DIRECTLY FROM LIST
                    if(position > -1 && position <= books.size() -1 )
                    {
                        load(position);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // LOG FAILED MESSAGE
                Log.w("BookListActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        //ADD LISTENER TO FIREBASE DATABASE REFERENCE
        databaseReference.addValueEventListener(preListener);


        //Close activity if user not logged in
        if(mAuth.getCurrentUser() == null){
            finish();
        }

        /**
         * Save book to list.
         * Book will be added to the book list (at the top) only if the add book
         * button (on the BookListActivity was clicked. If a list item was clicked,
         * the edited book values will be saved in the same position on the list.
         */
        SaveBook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // MAKE SURE INPUT IS VALID FIRST
                 if(isValidInput(title.getText().toString(), type.getText().toString(),
                         author.getText().toString(), year.getText().toString(),
                         numPages.getText().toString(), comments.getText().toString())){

                     // ADD BOOK TO LIST IF ADD BOOK BUTTON IS CLICKED
                     if (!isEdit){
                         books.add(addBook(title.getText().toString(), type.getText().toString(),
                                 author.getText().toString(), year.getText().toString(),
                                 numPages.getText().toString(), comments.getText().toString()));

                     }
                     // IF BOOK CLICKED FROM LIST VIEW, SET EDITED BOOK TO SAME POSITION IN LIST
                     else if(isEdit){
                         books.set(position, addBook(title.getText().toString(), type.getText().toString(),
                                 author.getText().toString(), year.getText().toString(),
                                 numPages.getText().toString(), comments.getText().toString()));

                     }

                     storageReference = storage.getReference();

                     //GET FIREBASE REFERENCE AND STORE A VALUE USING SETVALUE()
                     databaseReference = FirebaseDatabase.getInstance().getReference();
                     databaseReference.child("users").child(currentUser.getUid())
                             .child("books").setValue(books);
                     Toast.makeText(BookActivity.this, "Book saved.", Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(BookActivity.this, BookListActivity.class);
                     startActivity(intent);
                     finish();
                 }
             }
         });

        /**
         * Delete book from list
         * Book will be deleted from Book List at its location.
         */
        DeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit){
                    delete(position);
                    Intent intent = new Intent(BookActivity.this, BookListActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(BookActivity.this, "Only saved books can be deleted.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //SHARED HEADER - MAKE IMAGE CLICKABLE
        CircleImageView profileImage = findViewById(R.id.include).findViewById(R.id.defaultProfileImage);
        Log.i("SharedHeader", "about to set listener...");

        /**
         * Start profile activity.
         */
        profileImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SharedHeader", "clicked");
                Intent intent = new Intent(BookActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        //GET PROFILE IMAGE
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(currentUser.getUid()!= null) {
            storageReference.child("images/" + currentUser.getUid())
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.profile_img);
                            requestOptions.error(R.drawable.profile_img);

                            CircleImageView profileImage = findViewById(R.id.include).findViewById(R.id.defaultProfileImage);
                            Glide.with(BookActivity.this)
                                    .load(uri)
                                    .apply(requestOptions)
                                    .into(profileImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("BookListActivity", "Unable to download profile image! " + exception);
                }
            });
        }
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
            // CREATE NEW BOOK WITH VALUES
            book = new Book(title, type, author, year, pages, comments);
            if (favorite.isChecked()) {
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
     * @return true if the input is valid, false if not
     */
    public boolean isValidInput(String title, String type, String author, String pages, String year,
                                String comments){
        try {
            // REQUIRE AT LEAST TITLE AND AUTHOR OF BOOK
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

    /**
     * Load book values into EditText fields for editing/viewing.
     *
     * Book values are loaded into appropriate EditText fields. Position
     * is passed to ensure that correct book is loaded, according to which
     * is clicked in the ListView. Loading of values only occurs if a book in the displayed
     * Book List is clicked, checked by the passed position parameter.
     * @param position
     */
    public void load(int position){
        if(position >= 0 && books.get(position)!= null){
            isEdit = true;
            Log.i("LOAD", "Book array size: " + String.valueOf(books.size()));
            title.setText(books.get(position).getTitle());
            type.setText(books.get(position).getType());
            author.setText(books.get(position).getAuthor());
            year.setText(books.get(position).getYear());
            numPages.setText(books.get(position).getPages());
            comments.setText(books.get(position).getComments());
            favorite.setChecked(books.get(position).isRead());
        }
    }

    /**
     * Delete currently selected book from list.
     *
     * Book is deleted from list stored in firebase
     * at the passed position in the list.
     * @param position
     */
    public void delete(int position){
        if(position >= 0){
            // DELETE BOOK FROM ITS POSITION IN THE LIST
            isEdit = true;
            Log.i("DELETE", "Book array size before: " + String.valueOf(books.size()));
            books.remove(position);
            Log.i("DELETE", "Book array size after: " + String.valueOf(books.size()));

            // RESET POSITION TO ORIGINAL VALUE, PREVENTING CALL
            // TO LOAD(INT) AGAIN
            this.position = -1;

            // UPDATE LIST ON FIREBASE
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(currentUser.getUid())
                    .child("books").setValue(books);
            Toast.makeText(BookActivity.this, "Book deleted.", Toast.LENGTH_LONG).show();
        }
    }
}

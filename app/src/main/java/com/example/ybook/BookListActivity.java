package com.example.ybook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
 * Class responsible for displaying the list of books and provides means to add and edit a book
 * @author Cesar Sales, David Souza, Evan Harrison
 * @version 1.0
 * @since December, 15, 2018
 */
public class BookListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    //Firebase
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //GET PROFILE IMAGE
        storageReference.child("images/"+ currentUser.getUid())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile_img);
                requestOptions.error(R.drawable.profile_img);

                CircleImageView profileImage = findViewById(R.id.include).findViewById(R.id.defaultProfileImage);
                Glide.with(BookListActivity.this)
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

        //SHARED HEADER - MAKE IMAGE CLICKABLE
        //|CircleImageView profileImage = findViewById(R.id.include).findViewById(R.id.defaultProfileImage);
        Log.i("SharedHeader", "about to set listener...");

        CircleImageView profileImage = findViewById(R.id.include).findViewById(R.id.defaultProfileImage);
        profileImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("SharedHeader", "clicked");
                        Intent intent = new Intent(BookListActivity.this, ProfileActivity.class);
                        startActivity(intent);

            }
        });

        ImageButton add = findViewById(R.id.addItemIcon);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, BookActivity.class );
                startActivity(intent);
            }
        });

        //GET CURRENT USER AND FIREBASE REFERENCES
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //THIS EVENTLISTENER IS REPONSIBLE FOR CHECKING FOR CHANGES
        //SO IT UPDATES AS NEEDED
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                User user = dataSnapshot.child("users").child(currentUser.getUid())
                        .getValue(User.class);
                if (user != null) {
                    //Set username in shared header
                    TextView sharedUsername = findViewById(R.id.include).findViewById(R.id.sharedUsername);
                    sharedUsername.setText(user.getUsername());

                    Log.i("BookListActivity", "User:" + user.getEmail());

                    //Create the list adapter
                    BookListAdapter adapter = new BookListAdapter(BookListActivity.this,
                            generateData(user.getBooks()));

                    //Set the adapter updating the UI
                    ListView list = findViewById(R.id.booksListView);
                    list.setOnItemClickListener(BookListActivity.this);
                    list.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("BookListActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //Adding the listener to firebase reference
        databaseReference.addValueEventListener(postListener);
    }

    private List<BookListModel> generateData(List<Book> books){
        List<BookListModel> models = new ArrayList<>();

        if (books != null) {
            for (Book b : books) {
                if(b!=null) {
                    if (b.isRead()) {
                        models.add(new BookListModel(R.drawable.ic_baseline_star_24px, b.getTitle()));
                    } else {
                        models.add(new BookListModel(R.drawable.ic_baseline_star_border_24px, b.getTitle()));
                    }
                }

            }
        }
        return models;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(BookListActivity.this, BookActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}

package com.example.ybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

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
                    Log.i("BookListActivity", "User:" + user.getEmail());

                    //Create the list adapter
                    BookListAdapter adapter = new BookListAdapter(BookListActivity.this,
                            generateData(user.getBooks()));

                    //Set the adapter updating the UI
                    ListView list = findViewById(R.id.booksListView);
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
                if(b.isRead()) {
                    models.add(new BookListModel(R.drawable.ic_baseline_star_24px, b.getTitle()));
                }
                else {
                    models.add(new BookListModel(R.drawable.ic_baseline_star_border_24px, b.getTitle()));
                }

            }
        }
        return models;
    }
}

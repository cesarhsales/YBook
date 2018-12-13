package com.example.ybook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Button btnChoose, btnUpload, btnLogout;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private User user;
    boolean hasUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get a instance of firebase library
        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnLogout = (Button) findViewById(R.id.btnLogOut);
        btnChoose = (Button) findViewById(R.id.chooseProfileImg);
        btnUpload = (Button) findViewById(R.id.saveProfile);
        imageView = (ImageView) findViewById(R.id.defaultProfileImage);

        //THIS EVENTLISTENER IS REPONSIBLE FOR CHECKING FOR CHANGES
        //SO IT UPDATES AS NEEDED
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                user = dataSnapshot.child("users").child(currentUser.getUid())
                        .getValue(User.class);

                if (user != null) {
                    TextView username = findViewById(R.id.profileUsername);
                    username.setText(user.getUsername());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ProfileActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //Adding the listener to firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(postListener);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class );
                startActivity(intent);
            }
        });

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

                        CircleImageView profileImage = findViewById(R.id.defaultProfileImage);

                        Glide.with(ProfileActivity.this)
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        EditText username = findViewById(R.id.profileUsername);

        Log.i("ProfileActivity", "User: " + user);

        if(user != null) {
            //SET NEW USER
            //User userProfile = new User(user.getUsername(), user.getEmail(), user.getBooks());
            User userProfile = new User(currentUser.getDisplayName(), user.getEmail(), user.getBooks());
            userProfile.setUsername(username.getText().toString());

            Log.i("ProfileActivity", "userProfile: " + user.getUsername());

            //GET FIREBASE REFERENCE AND STORE A VALUE USING SETVALUE()
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(currentUser.getUid()).setValue(userProfile);

            hasUpdate = true;
        }

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            StorageReference ref = storageReference.child("images/"+ currentUser.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProfileActivity.this, BookListActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

        if(hasUpdate && filePath == null) {
            Intent intent = new Intent(ProfileActivity.this, BookListActivity.class);
            startActivity(intent);
        }
    }
}

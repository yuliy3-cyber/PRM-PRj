package com.example.movieticketapp.Activity.Account;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.Firebase.FirebaseRequest;
import com.example.movieticketapp.Model.Users;
import com.example.movieticketapp.NetworkChangeListener;
import com.example.movieticketapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    EditText fullNameET, emailET;
    ImageView addImage;
    RoundedImageView avatarImg;
    Uri filePath;
    String img;
    String uploadedAvatarUrl;

    FirebaseUser currentUser = FirebaseRequest.mAuth.getCurrentUser();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore databaseReference = FirebaseFirestore.getInstance();
    UploadTask uploadTask;

    ActivityResultLauncher<Intent> activityLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            filePath = data.getData();
                            avatarImg.setImageURI(filePath);
                            img = UUID.randomUUID().toString();
                        }
                    }
                }
            });

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_screen);

        addImage = findViewById(R.id.addimage);
        avatarImg = findViewById(R.id.avatarImg);
        fullNameET = findViewById(R.id.fullname);
        emailET = findViewById(R.id.emailaddress);

        // Load dữ liệu người dùng hiện tại
        FirebaseRequest.database.collection("Users")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Users user = documentSnapshot.toObject(Users.class);
                    if (user != null) {
                        fullNameET.setText(user.getName());
                        emailET.setText(user.getEmail());
                        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                            Picasso.get().load(user.getAvatar()).into(avatarImg);
                        }
                    }
                });

        addImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityLaunch.launch(intent);
        });

        Button backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(view -> finish());

        Button updateBtn = findViewById(R.id.UpdateBtn);
        updateBtn.setOnClickListener(view -> UpdateUser());
    }

    void UpdateUser() {
        if (fullNameET.length() == 0) {
            fullNameET.setError("Full Name is not empty!!!");
            return;
        }
        if (emailET.length() == 0) {
            emailET.setError("Email is not empty!!!");
            return;
        }

        // Cập nhật dữ liệu
        Update();
        updateAvatar(); // upload ảnh nếu có
    }

    void Update() {
        String newEmail = emailET.getText().toString().trim();
        String newName = fullNameET.getText().toString().trim();

        if (!newEmail.equals(currentUser.getEmail())) {
            UpdateEmail(newEmail);
        }
        if (!newName.equals(currentUser.getDisplayName())) {
            UpdateFullName(newName);
        }
    }

    void UpdateFullName(String newName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseRequest.database.collection("Users")
                        .document(currentUser.getUid())
                        .update("Name", newName);
            } else {
                UpdateError("Full Name");
            }
        });
    }

    void UpdateEmail(String newEmail) {
        currentUser.updateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseRequest.database.collection("Users")
                        .document(currentUser.getUid())
                        .update("Email", newEmail)
                        .addOnSuccessListener(unused -> {
                            FirebaseRequest.mAuth.signOut();
                            Intent loginIntent = new Intent(EditProfileActivity.this, SignInActivity.class);
                            TaskStackBuilder.create(EditProfileActivity.this)
                                    .addNextIntentWithParentStack(loginIntent)
                                    .startActivities();
                        });
            } else {
                UpdateError("Email");
            }
        });
    }

    void updateAvatar() {
        if (filePath != null) {
            StorageReference ref = storageReference.child("avatars/" + img);
            uploadTask = ref.putFile(filePath);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uploadedAvatarUrl = task.getResult().toString();

                        // Cập nhật Firestore
                        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");
                        DocumentReference doc = userCollection.document(currentUser.getUid());
                        doc.update("avatar", uploadedAvatarUrl);

                        // Cập nhật Firebase Auth profile
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(uploadedAvatarUrl))
                                .build();

                        currentUser.updateProfile(profileUpdates);
                        Toast.makeText(EditProfileActivity.this, "Avatar updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        UpdateError("Avatar Upload");
                    }
                }
            });
        } else {
            finish();
        }
    }

    void UpdateError(String error) {
        Toast.makeText(EditProfileActivity.this, "Edit Profile failed: " + error, Toast.LENGTH_SHORT).show();
    }
}

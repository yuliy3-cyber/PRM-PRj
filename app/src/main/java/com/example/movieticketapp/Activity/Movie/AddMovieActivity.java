package com.example.movieticketapp.Activity.Movie;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
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

    public static List<Uri> videoUris= new ArrayList<>();
    public static  Uri defaultUri;
    public  ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    public  ActivityResultLauncher<PickVisualMediaRequest> pickVideo;
    int th;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference storageReference2 = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore databaseReference = FirebaseFirestore.getInstance();
    DocumentReference document;
    ImageView moviebackground;
    TextView textbg;
    ImageView imbg;

    ImageView movieavatar;
    TextView textavt;
    ImageView imavt;
    EditText description;
    EditText movieName;
    TextView movieKind;
    EditText movieDurarion;
    Button applyButton;
    Button cancleButton;
    Uri backgrounduri;
    Uri avataruri = null;

    String urlbackground;
    Timestamp BeginDate;
    Timestamp EndDate;
    String urlavatar;
    Button BeginDateCalendarButton;
    Button EndDateCalendarButton;
    TrailerMovieApdapter adapter;
    List<String> InStorageVideoUris=new ArrayList<>();
    loadingAlert loadingDialog;
    public static String defaultAddTrailer = "Add";

    public static List<String> videos=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_screen);
        InStorageVideoUris.clear();
        loadingDialog= new loadingAlert(AddMovieActivity.this);
        defaultUri=Uri.parse("https://example.com/default");;
        BeginDateCalendarButton = findViewById(R.id.BeginDateCalendar);
        EndDateCalendarButton= findViewById(R.id.EndDateCalendar);
        document = databaseReference.collection("Movies").document();

        BeginDateCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show calendar dialog
                showBeginDateCalendarDialog();
                dismissKeyboard(v);
            }
        });
        EndDateCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show calendar dialog
                showEndDateCalendarDialog();
                dismissKeyboard(v);
            }
        });
}
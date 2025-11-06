package com.example.movieticketapp.Activity.Movie;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity {

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

    public static List<Uri> videoUris = new ArrayList<>();
    public static Uri defaultUri;
    public ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Timestamp BeginDate;
    Timestamp EndDate;
    Button BeginDateCalendarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_screen);
        InStorageVideoUris.clear();
        loadingDialog = new loadingAlert(AddMovieActivity.this);
        defaultUri = Uri.parse("https://example.com/default");
        BeginDateCalendarButton = findViewById(R.id.BeginDateCalendar);
        EndDateCalendarButton = findViewById(R.id.EndDateCalendar);
        document = databaseReference.collection("Movies").document();
    }

    BeginDateCalendarButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Show calendar dialog
            showBeginDateCalendarDialog();
            dismissKeyboard(v);
        }
    });

    EnđateCalendarButton.setOnClickLinsterner(new View.OnClickListener(){
        public void onClick(View v) {
            showEndDateCalendarDialog();
            dismissKeyboard(v);
        }
    });

    moviebackground = (ImageView) findViewById(R.id.moviebackground);
    textbg = (TextView) findViewById(R.id.textbackground);
    imbg = (ImageView) findViewById(R.id.imbackground);

    movieavatar =  findViewById(R.id.movieavatar);
    textavt = (TextView) findViewById(R.id.textavt);
    imavt = (ImageView) findViewById(R.id.imavt);

}
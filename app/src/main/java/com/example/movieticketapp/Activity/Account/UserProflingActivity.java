package com.example.movieticketapp.Activity.Account;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.NetworkChangeListener;
import com.example.movieticketapp.R;

import java.util.Arrays;
import java.util.List;

public class UserProfilingActivity extends AppCompatActivity {

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private int selectedCount = 0;
    private ImageButton btnNext;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeListener, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profiling_screen); // sửa lỗi chính tả tên layout: user_profiling_screen

        btnNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Danh sách các button thể loại
        List<Button> genreButtons = Arrays.asList(
                findViewById(R.id.btnHorror),
                findViewById(R.id.btnAction),
                findViewById(R.id.btnDrama),
                findViewById(R.id.btnWar),
                findViewById(R.id.btnComedy),
                findViewById(R.id.btnCrime)
        );

        // Danh sách các button ngôn ngữ
        List<Button> languageButtons = Arrays.asList(
                findViewById(R.id.btnBahasa),
                findViewById(R.id.btnEnglish),
                findViewById(R.id.btnJapanese),
                findViewById(R.id.btnKorean)
        );

        // Gộp tất cả button lại
        List<Button> allButtons = Arrays.asList();
        allButtons.addAll(genreButtons);
        allButtons.addAll(languageButtons);

        // Gán sự kiện click cho tất cả button
        for (Button btn : allButtons) {
            btn.setOnClickListener(this::onSelectButtonClicked);
        }

        // Nút Next
        btnNext.setOnClickListener(v -> {
            if (selectedCount > 0) {
                Intent i = new Intent(getApplicationContext(), ConfirmationProfileActivity.class);
                startActivity(i);
            }
        });

        // Nút Back
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void onSelectButtonClicked(View view) {
        Button button = (Button) view;
        boolean isSelected = button.isSelected();

        button.setSelected(!isSelected);
        selectedCount += isSelected ? -1 : 1;

        btnNext.setEnabled(selectedCount > 0);
        btnNext.setAlpha(selectedCount > 0 ? 1f : 0.5f); // làm mờ nút nếu chưa chọn
    }
}

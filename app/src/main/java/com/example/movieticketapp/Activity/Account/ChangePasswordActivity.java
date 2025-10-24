package com.example.movieticketapp.Activity.Account;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.Firebase.FirebaseRequest;
import com.example.movieticketapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

public class ChangePasswordActivity extends AppCompatActivity {

    Button backBtn, changePasswordBtn;
    EditText currentPasswordET, newPasswordET, confirmPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Ánh xạ
        backBtn = findViewById(R.id.backbutton);
        newPasswordET = findViewById(R.id.password);
        confirmPasswordET = findViewById(R.id.confirmpassword);
        currentPasswordET = findViewById(R.id.currentPassword);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        // Quay lại màn trước
        backBtn.setOnClickListener(v -> finish());

        // Đổi mật khẩu
        changePasswordBtn.setOnClickListener(v -> changePassword());
    }

    void changePassword() {
        String currentPassword = currentPasswordET.getText().toString().trim();
        String newPassword = newPasswordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();

        // ✅ Kiểm tra dữ liệu nhập
        if (currentPassword.isEmpty()) {
            currentPasswordET.setError("Current password is required!");
            return;
        }
        if (newPassword.isEmpty()) {
            newPasswordET.setError("New password is required!");
            return;
        }
        if (newPassword.length() < 6) {
            newPasswordET.setError("Password should be at least 6 characters!");
            return;
        }
        if (!confirmPassword.equals(newPassword)) {
            confirmPasswordET.setError("Passwords do not match!");
            return;
        }
        if (currentPassword.equals(newPassword)) {
            Toast.makeText(this, "New password must be different from current password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Re-authenticate
        AuthCredential credential = EmailAuthProvider.getCredential(
                FirebaseRequest.mAuth.getCurrentUser().getEmail(),
                currentPassword
        );

        FirebaseRequest.mAuth.getCurrentUser()
                .reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // ✅ Sau khi xác thực lại thành công → cập nhật mật khẩu
                        updatePassword(newPassword);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Current password is incorrect!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void updatePassword(String newPassword) {
        FirebaseRequest.mAuth.getCurrentUser()
                .updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Password changed successfully! Please sign in again.",
                                Toast.LENGTH_SHORT).show();

                        // ✅ Đăng xuất và điều hướng về màn hình đăng nhập
                        FirebaseRequest.mAuth.signOut();
                        Intent loginIntent = new Intent(ChangePasswordActivity.this, SignInActivity.class);
                        TaskStackBuilder.create(ChangePasswordActivity.this)
                                .addNextIntentWithParentStack(loginIntent)
                                .startActivities();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Failed to change password: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

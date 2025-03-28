package vn.edu.tlu.tlucontact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import vn.edu.tlu.tlucontact.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private ImageView ivBack;
    private TextInputLayout tilName, tilEmail, tilPhone, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLoginPrompt;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FrameLayout loadingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth và Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Ánh xạ view
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLoginPrompt = findViewById(R.id.tv_login_prompt);
        loadingContainer = findViewById(R.id.loadingContainer);

        tvLoginPrompt.setOnClickListener(v -> {
            // Điều hướng to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignup.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (TextUtils.isEmpty(name)) {
            tilName.setError("Họ và tên không được để trống");
            return;
        } else {
            tilName.setError(null); // Xóa thông báo lỗi
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email không được để trống");
            return;
        } else if (!isValidTLUEmail(email)) {
            tilEmail.setError("Email không đúng định dạng");
            return;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Số điện thoại không được để trống");
            return;
        } else {
            tilPhone.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Mật khẩu không được để trống");
            return;
        } else if (password.length() < 6) {
            tilPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        } else {
            tilPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng nhập lại mật khẩu");
            return;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Mật khẩu không khớp");
            return;
        } else {
            tilConfirmPassword.setError(null);
        }

        loadingContainer.setVisibility(View.VISIBLE);
        btnSignup.setEnabled(false);

        // Đăng ký với Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Đăng ký thành công
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Xác định role dựa trên định dạng email
                                String role = email.endsWith("@tlu.edu.vn") ? "CBGV" : "SV";

                                // Lưu thông tin người dùng vào Firestore
                                saveUserToFirestore(user.getUid(), role, name, email, phone);
                            }
                        } else {
                            // Đăng ký thất bại
                            loadingContainer.setVisibility(View.GONE);
                            btnSignup.setEnabled(true);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Phương thức lưu thông tin người dùng vào Firestore
    private void saveUserToFirestore(String userId, String role, String name, String email, String phone) {
        // Tạo đối tượng Map để lưu thông tin người dùng
        Map<String, Object> user = new HashMap<>();
        user.put("uid", userId);
        user.put("role", role);
        user.put("dissplayName", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("createdAt", System.currentTimeMillis());

        // Lưu vào collection "users" với document ID là userId
        mFirestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User information saved to Firestore");
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển đến MainActivity
                    updateUI(mAuth.getCurrentUser());
                })
                .addOnFailureListener(e -> {
                    loadingContainer.setVisibility(View.GONE);
                    btnSignup.setEnabled(true);
                    Log.w(TAG, "Error saving user information to Firestore", e);
                    Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin người dùng: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private boolean isValidTLUEmail(String email) {
        return (email.endsWith("@tlu.edu.vn") || email.endsWith("@e.tlu.edu.vn"))
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Chuyển đến MainActivity với thông tin role
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

            // Xác định role dựa trên định dạng email
            String email = user.getEmail();
            String role = email != null && email.endsWith("@tlu.edu.vn") ? "CBGV" : "SV";

            intent.putExtra("USER_ROLE", role);
            startActivity(intent);
            finish();
        }
    }
}

package vn.edu.tlu.tlucontact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private View tvSignupPrompt;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;
    private FrameLayout loadingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Ánh xạ view
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignupPrompt = findViewById(R.id.tvRegister);
        loadingContainer = findViewById(R.id.loadingContainer);

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(v -> loginWithEmailPassword());

        // Chuyển đến màn hình đăng ký
        tvSignupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Kiểm tra người dùng đã đăng nhập chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRoleAndNavigate(currentUser.getUid());
        }
    }

    private void loginWithEmailPassword() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email không được để trống");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Mật khẩu không được để trống");
            return;
        }

        btnLogin.setEnabled(false); // Disable nút đăng nhập để tránh spam
        loadingContainer.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    loadingContainer.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserRoleAndNavigate(user.getUid());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        btnLogin.setEnabled(true);
                    }
                });
    }

    private void checkUserRoleAndNavigate(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String role = document.getString("role");
                    navigateToMain(role);
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMain(String role) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_ROLE", role); // Truyền role sang MainActivity
        startActivity(intent);
        finish();
    }
}

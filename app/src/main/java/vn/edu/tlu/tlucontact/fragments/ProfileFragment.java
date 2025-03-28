package vn.edu.tlu.tlucontact.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.activities.LoginActivity;

public class ProfileFragment extends Fragment {
    private TextView tvProfileName, tvProfileEmail, tvProfilePhone;
    private Button btnLogout;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfilePhone = view.findViewById(R.id.tvProfilePhone);
        btnLogout = view.findViewById(R.id.btnLogout);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserProfile();
        btnLogout.setOnClickListener(v -> logOutUser());
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                tvProfileName.setText(document.getString("dissplayName"));
                                tvProfileEmail.setText(document.getString("email"));
                                tvProfilePhone.setText(document.getString("phone"));
                            } else {
                                Toast.makeText(getActivity(), "Không tìm thấy hồ sơ người dùng", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Lỗi khi tải hồ sơ: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void logOutUser() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    auth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

}

package vn.edu.tlu.tlucontact.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import vn.edu.tlu.tlucontact.R;

public class HomeFragment extends Fragment {
    private MaterialButton btnEmployees, btnStudents, btnUnits;
    private String userRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Lấy role từ arguments (truyền từ MainActivity)
        if (getArguments() != null) {
            userRole = getArguments().getString("USER_ROLE", "SV"); // Mặc định là SV nếu không có dữ liệu
        }

        btnEmployees = view.findViewById(R.id.btnEmployees);
        btnStudents = view.findViewById(R.id.btnStudents);
        btnUnits = view.findViewById(R.id.btnUnits);

        // Nếu người dùng là sinh viên, vô hiệu hóa nút danh bạ CBNV
        if ("SV".equals(userRole)) {
            btnEmployees.setEnabled(false);
            btnEmployees.setAlpha(0.5f); // Làm mờ nút để thể hiện không khả dụng
        } else {
            btnEmployees.setOnClickListener(v -> openFragment(new StaffListFragment()));
        }

        btnStudents.setOnClickListener(v -> openFragment(new StudentListFragment()));
        btnUnits.setOnClickListener(v -> openFragment(new UnitListFragment()));


        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

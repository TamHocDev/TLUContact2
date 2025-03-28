package vn.edu.tlu.tlucontact.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.card.MaterialCardView;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.models.Student;

public class StudentDetailFragment extends Fragment {
    private static final String ARG_STUDENT = "student";

    private Student student;

    public static StudentDetailFragment newInstance(Student student) {
        StudentDetailFragment fragment = new StudentDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable(ARG_STUDENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_detail, container, false);

        TextView tvName = view.findViewById(R.id.tvStudentDetailName);
        TextView tvClass = view.findViewById(R.id.tvStudentDetailClass);
        TextView tvUnit = view.findViewById(R.id.tvStudentDetailUnit);
        TextView tvPhone = view.findViewById(R.id.tvStudentDetailPhone);
        TextView tvEmail = view.findViewById(R.id.tvStudentDetailEmail);
        TextView tv_Class = view.findViewById(R.id.tvStudentClassTop);
        TextView tv_Unit = view.findViewById(R.id.tvStudentUnitTop);
        MaterialCardView cardCall = view.findViewById(R.id.cardCall);
        MaterialCardView cardEmail = view.findViewById(R.id.cardEmail);

        // Xử lý sự kiện nút gọi điện
        cardCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString().replace(".", "");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút gửi email
        cardEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + tvEmail.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với " + tvEmail.getText().toString());
                startActivity(intent);
            }
        });

        // Xử lý nút back
        ImageView ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        });

        if (student != null) {
            tv_Class.setText(student.getClassName());
            tv_Unit.setText(student.getUnit());
            tvName.setText(student.getFullName());
            tvClass.setText(student.getClassName());
            tvUnit.setText(student.getUnit());
            tvPhone.setText(student.getPhone());
            tvEmail.setText(student.getEmail());
        }

        return view;
    }
}

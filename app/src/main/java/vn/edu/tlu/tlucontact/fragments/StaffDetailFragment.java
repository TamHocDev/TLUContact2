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
import vn.edu.tlu.tlucontact.models.Staff;

public class StaffDetailFragment extends Fragment {
    private static final String ARG_STAFF = "staff";

    private Staff staff;

    public static StaffDetailFragment newInstance(Staff staff) {
        StaffDetailFragment fragment = new StaffDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STAFF, staff);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            staff = (Staff) getArguments().getSerializable(ARG_STAFF);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_detail, container, false);

        TextView tvName = view.findViewById(R.id.tvStaffDetailName);
        TextView tvPosition = view.findViewById(R.id.tvStaffDetailPosition);
        TextView tvUnit = view.findViewById(R.id.tvStaffDetailUnit);
        TextView tvPhone = view.findViewById(R.id.tvStaffDetailPhone);
        TextView tvEmail = view.findViewById(R.id.tvStaffDetailEmail);
        TextView tv_Position = view.findViewById(R.id.tvStaffPositionTop);
        TextView tv_Unit = view.findViewById(R.id.tvStaffUnitTop);
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

        if (staff != null) {
            tv_Position.setText(staff.getPosition());
            tv_Unit.setText(staff.getUnit());
            tvName.setText(staff.getFullName());
            tvPosition.setText(staff.getPosition());
            tvUnit.setText(staff.getUnit());
            tvPhone.setText(staff.getPhone());
            tvEmail.setText(staff.getEmail());
        }

        return view;
    }
}

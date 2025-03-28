package vn.edu.tlu.tlucontact.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.models.Unit;


public class UnitDetailFragment extends Fragment {

    private static final String ARG_UNIT = "unit";

    private Unit unit;

    public static UnitDetailFragment newInstance(Unit unit) {
        UnitDetailFragment fragment = new UnitDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UNIT, unit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unit = (Unit) getArguments().getSerializable(ARG_UNIT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_detail, container, false);

        TextView tvName = view.findViewById(R.id.tvUnitDetailName);
        TextView tvAddress = view.findViewById(R.id.tvUnitDetailAddress);
        TextView tvPhone = view.findViewById(R.id.tvUnitDetailPhone);
        TextView tvEmail = view.findViewById(R.id.tvUnitDetailEmail);
        TextView tvDescription = view.findViewById(R.id.tvUnitDetailDescription);
        Button btnCall = view.findViewById(R.id.btnCall);
        Button btnEmail = view.findViewById(R.id.btnEmail);

        // Xử lý sự kiện nút gọi điện
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString().replace(".", "");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút gửi email
        btnEmail.setOnClickListener(new View.OnClickListener() {
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

        if (unit != null) {
            tvName.setText(unit.getName());
            tvAddress.setText(unit.getAddress());
            tvPhone.setText(unit.getPhone());
            tvEmail.setText(unit.getEmail());
            tvDescription.setText(unit.getDescription());
        }

        return view;
    }
}
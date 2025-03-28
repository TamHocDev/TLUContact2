package vn.edu.tlu.tlucontact.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.databinding.ActivityMainBinding;
import vn.edu.tlu.tlucontact.fragments.HomeFragment;
import vn.edu.tlu.tlucontact.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy vai trò người dùng từ intent và lưu trữ nó
        userRole = getIntent().getStringExtra("USER_ROLE");

        if (savedInstanceState == null) {
            binding.bottomNavigation.setItemSelected(R.id.home, true);
            loadHomeFragment();
        }

        // Thiết lập navigation dưới cùng
        setupBottomNavigation();
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public void selectTab(int tabId) {
        binding.bottomNavigation.setItemSelected(tabId, true);
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                loadHomeFragment();
            } else if (id == R.id.profile) {
                setFragment(new ProfileFragment());
            }
        });
    }

    // Tạo một phương thức riêng biệt để tải HomeFragment với vai trò người dùng
    private void loadHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();

        // Truyền vai trò người dùng cho HomeFragment
        Bundle bundle = new Bundle();
        bundle.putString("USER_ROLE", userRole);
        homeFragment.setArguments(bundle);

        setFragment(homeFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // Phương thức để lấy userRole (có thể được sử dụng bởi các fragment)
    public String getUserRole() {
        return userRole;
    }
}
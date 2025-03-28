package vn.edu.tlu.tlucontact.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.adapters.StaffAdapter;
import vn.edu.tlu.tlucontact.models.Staff;

public class StaffListFragment extends Fragment implements StaffAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private StaffAdapter staffAdapter;
    private FirebaseFirestore db;
    private CollectionReference staffRef;
    private SearchView searchView;
    private Spinner spinnerSort;
    private ConstraintLayout loadingContainer;
    private LinearLayout emptyStateContainer;

    private List<Staff> staffList = new ArrayList<>();
    private List<Staff> displayList = new ArrayList<>();
    private String searchQuery = "";
    private String sortOption = "Mặc định";
    private ListenerRegistration listenerRegistration;
    private Collator collator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchView);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        loadingContainer = view.findViewById(R.id.loadingContainer);
        emptyStateContainer = view.findViewById(R.id.emptyStateContainer);

        // Khởi tạo Collator cho tiếng Việt
        collator = Collator.getInstance(new Locale("vi", "VN"));

        db = FirebaseFirestore.getInstance();
        staffRef = db.collection("staff");

        setupRecyclerView();
        setupSearch();
        setupSortSpinner();

        // Xử lý nút back
        ImageView ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        staffAdapter = new StaffAdapter();
        staffAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(staffAdapter);
    }

    // Triển khai phương thức interface OnItemClickListener
    @Override
    public void onItemClick(Staff staff) {
        // Điều hướng đến StaffDetailFragment
        StaffDetailFragment detailFragment = StaffDetailFragment.newInstance(staff);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Không làm gì khi nhấn submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                updateDisplayList();
                return true;
            }
        });
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortOption = parent.getItemAtPosition(position).toString();
                updateDisplayList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateDisplayList() {
        // Lọc danh sách dựa trên từ khóa tìm kiếm
        List<Staff> filteredList = new ArrayList<>();

        if (TextUtils.isEmpty(searchQuery)) {
            filteredList.addAll(staffList);
        } else {
            String normalizedQuery = removeDiacritics(searchQuery).toLowerCase();
            for (Staff staff : staffList) {
                String normalizedName = removeDiacritics(staff.getFullName()).toLowerCase();
                if (normalizedName.contains(normalizedQuery)) {
                    filteredList.add(staff);
                }
            }
        }

        // Sắp xếp danh sách theo tên cán bộ
        if (sortOption.equals("A → Z")) {
            Collections.sort(filteredList, (s1, s2) ->
                    collator.compare(getLastName(s1.getFullName()), getLastName(s2.getFullName())));
        } else if (sortOption.equals("Z → A")) {
            Collections.sort(filteredList, (s1, s2) ->
                    collator.compare(getLastName(s2.getFullName()), getLastName(s1.getFullName())));
        }

        // Cập nhật adapter
        displayList = filteredList;
        staffAdapter.setData(displayList);

        //Hiển thị trạng thái trống nếu không tìm thấy kết quả
        if (displayList.isEmpty() && !staffList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateContainer.setVisibility(View.GONE);
        }
    }

    private String getLastName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split("\\s+");
        return parts.length > 0 ? parts[parts.length - 1] : "";
    }

    // Phương thức loại bỏ dấu tiếng Việt
    private String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    @Override
    public void onStart() {
        super.onStart();

        // Hiển thị trạng thái loading
        loadingContainer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);

        // Thiết lập listener theo thời gian thực
        listenerRegistration = staffRef
                .addSnapshotListener((snapshot, error) -> {
                    //  Ẩn trạng thái loading
                    loadingContainer.setVisibility(View.GONE);

                    if (error != null) {
                        //Xử lý lỗi - hiển thị thông báo lỗi
                        return;
                    }

                    if (snapshot == null || snapshot.isEmpty()) {
                        // Hiển thị trạng thái trống nếu không có dữ liệu
                        recyclerView.setVisibility(View.GONE);
                        emptyStateContainer.setVisibility(View.VISIBLE);
                        return;
                    }

                    staffList.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Staff staff = doc.toObject(Staff.class);
                        if (staff != null) {
                            staffList.add(staff);
                        }
                    }

                    updateDisplayList();
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
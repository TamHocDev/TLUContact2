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
import vn.edu.tlu.tlucontact.adapters.UnitAdapter;
import vn.edu.tlu.tlucontact.models.Unit;

public class UnitListFragment extends Fragment implements UnitAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private UnitAdapter unitAdapter;
    private FirebaseFirestore db;
    private CollectionReference unitsRef;
    private SearchView searchView;
    private Spinner spinnerSort;
    private ConstraintLayout loadingContainer;
    private LinearLayout emptyStateContainer;

    private List<Unit> unitList = new ArrayList<>();
    private List<Unit> displayList = new ArrayList<>();
    private String searchQuery = "";
    private String sortOption = "Mặc định";
    private ListenerRegistration listenerRegistration;
    private Collator collator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchView);
        spinnerSort = view.findViewById(R.id.spinnerSort);

        loadingContainer = view.findViewById(R.id.loadingContainer);
        emptyStateContainer = view.findViewById(R.id.emptyStateContainer);

        // Khởi tạo Collator cho tiếng Việt
        collator = Collator.getInstance(new Locale("vi", "VN"));

        db = FirebaseFirestore.getInstance();
        unitsRef = db.collection("units");

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
        unitAdapter = new UnitAdapter();
        unitAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(unitAdapter);
    }

    // Triển khai phương thức interface OnItemClickListener
    @Override
    public void onItemClick(Unit unit) {
        // Điều hướng đến UnitDetailFragment
        UnitDetailFragment detailFragment = UnitDetailFragment.newInstance(unit);
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
        List<Unit> filteredList = new ArrayList<>();

        if (TextUtils.isEmpty(searchQuery)) {
            filteredList.addAll(unitList);
        } else {
            String normalizedQuery = removeDiacritics(searchQuery).toLowerCase();
            for (Unit unit : unitList) {
                String normalizedName = removeDiacritics(unit.getName()).toLowerCase();
                if (normalizedName.contains(normalizedQuery)) {
                    filteredList.add(unit);
                }
            }
        }

        // Sắp xếp danh sách theo tên đơn vị
        if (sortOption.equals("A → Z")) {
            Collections.sort(filteredList, (u1, u2) ->
                    collator.compare(u1.getName(), u2.getName()));
        } else if (sortOption.equals("Z → A")) {
            Collections.sort(filteredList, (u1, u2) ->
                    collator.compare(u2.getName(), u1.getName()));
        }

        // Cập nhật adapter
        displayList = filteredList;
        unitAdapter.setData(displayList);

        if (displayList.isEmpty() && !unitList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateContainer.setVisibility(View.GONE);
        }
    }

    // Phương thức loại bỏ dấu tiếng Việt
    private String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    @Override
    public void onStart() {
        super.onStart();

        loadingContainer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);

        // Thiết lập listener theo thời gian thực
        listenerRegistration = unitsRef
                .addSnapshotListener((snapshot, error) -> {
                    loadingContainer.setVisibility(View.GONE);
                    if (error != null) {
                        return;
                    }

                    if (snapshot == null || snapshot.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyStateContainer.setVisibility(View.VISIBLE);
                        return;
                    }

                    unitList.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Unit unit = doc.toObject(Unit.class);
                        if (unit != null) {
                            unitList.add(unit);
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
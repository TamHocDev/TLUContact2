package vn.edu.tlu.tlucontact.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.models.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private List<Staff> staffList;
    private OnItemClickListener listener;

    public StaffAdapter() {
        this.staffList = new ArrayList<>();
    }

    public void setData(List<Staff> newList) {
        this.staffList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.tvName.setText(staff.getFullName());
        holder.tvPosition.setText(staff.getPosition());
        holder.tvUnit.setText(staff.getUnit());
        holder.tvPhone.setText(staff.getPhone());

        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(staff);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPosition, tvPhone, tvUnit;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStaffName);
            tvPosition = itemView.findViewById(R.id.tvStaffPosition);
            tvPhone = itemView.findViewById(R.id.tvStaffPhone);
            tvUnit = itemView.findViewById(R.id.tvStaffUnit);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Staff staff);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
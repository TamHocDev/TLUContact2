package vn.edu.tlu.tlucontact.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.models.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {

    private List<Unit> unitList;
    private OnItemClickListener listener;

    public UnitAdapter() {
        this.unitList = new ArrayList<>();
    }

    public void setData(List<Unit> newList) {
        this.unitList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = unitList.get(position);
        holder.tvUnitName.setText(unit.getName());
        holder.tvUnitPhone.setText(unit.getPhone());
        holder.tvUnitAddress.setText(unit.getAddress());

        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(unit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return unitList.size();
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, parent, false);
        return new UnitViewHolder(view);
    }

    public static class UnitViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName, tvUnitPhone, tvUnitAddress;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitPhone = itemView.findViewById(R.id.tvUnitPhone);
            tvUnitAddress = itemView.findViewById(R.id.tvUnitAddress);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Unit unit);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
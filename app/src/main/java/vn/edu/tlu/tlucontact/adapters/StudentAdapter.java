package vn.edu.tlu.tlucontact.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.tlucontact.R;
import vn.edu.tlu.tlucontact.models.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private OnItemClickListener listener;

    public StudentAdapter() {
        this.studentList = new ArrayList<>();
    }

    public void setData(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getFullName());
        holder.tvStudentClass.setText("Lớp: " + student.getClassName());
        holder.tvStudentUnit.setText("Khoa: " + student.getUnit());
        holder.tvStudentPhone.setText(student.getPhone());

        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentClass, tvStudentUnit, tvStudentPhone;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentClass = itemView.findViewById(R.id.tvStudentClass);
            tvStudentUnit = itemView.findViewById(R.id.tvStudentUnit);
            tvStudentPhone = itemView.findViewById(R.id.tvEmployeePhone);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
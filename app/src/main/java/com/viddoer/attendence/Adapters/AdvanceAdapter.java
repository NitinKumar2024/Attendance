package com.viddoer.attendence.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Models.AdvancedAttendanceModel;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.Student;
import com.viddoer.attendence.Students.StudentAdapter;

import java.util.List;

public class AdvanceAdapter extends RecyclerView.Adapter<AdvanceAdapter.StudentViewHolder> {

    private Context context;
    private List<AdvancedAttendanceModel> students;

    public AdvanceAdapter(Context context, List<AdvancedAttendanceModel> students) {
        this.context = context;
        this.students = students;
    }


    @NonNull
    @Override
    public AdvanceAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_perfect_student, parent, false);
        return new AdvanceAdapter.StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvanceAdapter.StudentViewHolder holder, int position) {
        AdvancedAttendanceModel student = students.get(position);

        holder.textName.setText(student.getName());
        holder.textRoll.setText(student.getRoll());
        holder.textStatus.setText(student.getStatus());
        String date = student.getDate();
        if (date == null || date.isEmpty()) {
            holder.textDate.setVisibility(View.GONE);
        } else {
            holder.textDate.setText(date);
            holder.textDate.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textRoll, textStatus, textDate;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textRoll = itemView.findViewById(R.id.textRoll);
            textStatus = itemView.findViewById(R.id.textStatus);
            textDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}


package com.viddoer.attendence.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Models.StudentAddModel;
import com.viddoer.attendence.R;

import java.util.List;

public class StudentAddAdapter extends RecyclerView.Adapter<StudentAddAdapter.ViewHolder> {

    private List<StudentAddModel> studentList;

    public StudentAddAdapter(List<StudentAddModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentAddModel student = studentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textRegNo;
        private TextView textName;
        private TextView textPhone;
        private TextView textEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRegNo = itemView.findViewById(R.id.text_reg_no);
            textName = itemView.findViewById(R.id.text_name);
            textPhone = itemView.findViewById(R.id.text_phone);
            textEmail = itemView.findViewById(R.id.text_email);
        }

        public void bind(StudentAddModel student) {
            textRegNo.setText(student.getRegNo());
            textName.setText(student.getName());
            textPhone.setText(student.getPhone());
            textEmail.setText(student.getEmail());
        }
    }
}

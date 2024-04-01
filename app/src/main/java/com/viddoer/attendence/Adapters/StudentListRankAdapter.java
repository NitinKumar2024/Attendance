package com.viddoer.attendence.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Models.StudentListRankModel;
import com.viddoer.attendence.R;

import java.util.List;

public class StudentListRankAdapter extends RecyclerView.Adapter<StudentListRankAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentListRankModel> studentItemList;

    public StudentListRankAdapter(Context context, List<StudentListRankModel> studentItemList) {
        this.context = context;
        this.studentItemList = studentItemList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_student_rank, parent, false);
        return new StudentViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentListRankModel currentItem = studentItemList.get(position);

        holder.textRollNo.setText(currentItem.getRollNumber());
        holder.textName.setText(currentItem.getName());
        holder.textRank.setText(currentItem.getRank());
        holder.progressBar.setProgress(currentItem.getAttendancePercentage());
        holder.textPercentage.setText(currentItem.getAttendancePercentage() + "%");


        int presentColor = ContextCompat.getColor(context, R.color.presentColor);
        int absentColor = ContextCompat.getColor(context, R.color.absentColor);

        if (currentItem.getAttendancePercentage() >= 75) {
            holder.linearLayout.setBackgroundColor(presentColor);
        } else {
            holder.linearLayout.setBackgroundColor(absentColor);
        }

    }

    @Override
    public int getItemCount() {
        return studentItemList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView textRollNo;
        public TextView textName;
        public TextView textRank;
        public ProgressBar progressBar;
        public TextView textPercentage;
        public CardView cardView;
        public LinearLayout linearLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textRollNo = itemView.findViewById(R.id.text_roll_no);
            textName = itemView.findViewById(R.id.text_name);
            textRank = itemView.findViewById(R.id.text_rank);
            progressBar = itemView.findViewById(R.id.progress_bar);
            textPercentage = itemView.findViewById(R.id.text_percentage);
            cardView = itemView.findViewById(R.id.cardView1);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

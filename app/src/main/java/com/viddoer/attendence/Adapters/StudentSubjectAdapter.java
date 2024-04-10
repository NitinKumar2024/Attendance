package com.viddoer.attendence.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Models.StudentSubjectModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentFrontDashboard.AllStudentRankDisplayActivity;
import com.viddoer.attendence.Students.StudentSubjectDisplay;
import com.viddoer.attendence.Students.class_test.ClassTestNumberDisplay;

import java.util.List;

public class StudentSubjectAdapter extends RecyclerView.Adapter<StudentSubjectAdapter.SubjectViewHolder> {

    private List<StudentSubjectModel> subjectList;
    private Context context;

    public StudentSubjectAdapter(Context context, List<StudentSubjectModel> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        StudentSubjectModel subjectModel = subjectList.get(position);

        // Set the subject name to the TextView
        holder.subjectTextView.setText(subjectModel.getSubject());
        String role = subjectModel.getRole();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role.equals("rank")){

                    Intent intent = new Intent(v.getContext(), AllStudentRankDisplayActivity.class);
                    intent.putExtra("subject", subjectModel.getSubject());
                    intent.putExtra("subject_code", subjectModel.getSubject_code());
                    intent.putExtra("branch_code", subjectModel.getBranch_code());
                    intent.putExtra("roll", subjectModel.getRoll_no());
                    v.getContext().startActivity(intent);


                } else if (role.equals("class_test")) {
                    Intent intent = new Intent(v.getContext(), ClassTestNumberDisplay.class);
                    intent.putExtra("subject", subjectModel.getSubject());
                    intent.putExtra("subject_code", subjectModel.getSubject_code());
                    intent.putExtra("branch_code", subjectModel.getBranch_code());
                    intent.putExtra("registration", subjectModel.getRegistration());
                    v.getContext().startActivity(intent);

                } else {

                    Intent intent = new Intent(v.getContext(), StudentSubjectDisplay.class);
                    intent.putExtra("subject", subjectModel.getSubject());
                    intent.putExtra("subject_code", subjectModel.getSubject_code());
                    intent.putExtra("branch_code", subjectModel.getBranch_code());
                    intent.putExtra("roll", subjectModel.getRoll_no());
                    v.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView subjectTextView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.mathematics); // Change to your card view ID
            subjectTextView = itemView.findViewById(R.id.textViewSubject); // Add an ID to your TextView in the layout
        }
    }
}

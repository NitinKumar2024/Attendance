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

import com.viddoer.attendence.AllStudentDisplay;
import com.viddoer.attendence.Models.FacultyAllSubjectModel;
import com.viddoer.attendence.Models.StudentSubjectModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentSubjectDisplay;

import java.util.List;

public class FacultyAllSubjectAdapter extends RecyclerView.Adapter<FacultyAllSubjectAdapter.SubjectViewHolder> {

    private final List<FacultyAllSubjectModel> subjectList;
    private Context context;

    public FacultyAllSubjectAdapter(Context context, List<FacultyAllSubjectModel> subjectList) {
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
        FacultyAllSubjectModel subjectModel = subjectList.get(position);

        // Set the subject name to the TextView
        holder.subjectTextView.setText(subjectModel.getSubject_name());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AllStudentDisplay.class);
                intent.putExtra("subject", subjectModel.getSubject_name());
                intent.putExtra("subject_code", subjectModel.getSubject_code());
                intent.putExtra("branch", subjectModel.getBranch());
                intent.putExtra("semester", subjectModel.getSemester());
                v.getContext().startActivity(intent);
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

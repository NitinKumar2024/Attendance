package com.viddoer.attendence.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Faculties.StudentDeatailsBottomSheet;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.BottomSheetFragment;
import com.viddoer.attendence.Students.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.AttendenceViewHolder> {

    List<AttendenceModel> attendanceModels;
    List<String> all_student = new ArrayList<>();

    Context context;

    public AttendenceAdapter(List<AttendenceModel> attendanceModels, Context context) {
        this.attendanceModels = attendanceModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_attendence, parent, false);
        return new AttendenceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AttendenceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String names = attendanceModels.get(position).getName();
        String roll = attendanceModels.get(position).getRoll_no();
        String email = attendanceModels.get(position).getEmail();
        String subject = attendanceModels.get(position).getSubject();
        String subject_name = attendanceModels.get(position).getSubject_name();
        holder.name.setText(names + " " + "(" + roll + ")");

        holder.sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentDeatailsBottomSheet bottomSheetFragment = new StudentDeatailsBottomSheet();
                bottomSheetFragment.setStudentList(attendanceModels.get(position).getNumber(), email, subject, names, roll, subject_name);
                bottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());

            }
        });

        CheckBox checkBoxStudent = holder.itemView.findViewById(R.id.checkBoxPresent);
        CheckBox checkBoxAbsent = holder.itemView.findViewById(R.id.checkBoxAbsent);

        checkBoxStudent.setTag(position);
        checkBoxAbsent.setTag(position);

        checkBoxStudent.setOnCheckedChangeListener(null);
        checkBoxAbsent.setOnCheckedChangeListener(null);

        // Set checkbox status based on all_student list
        String value = names + "," + roll + ",Present," + attendanceModels.get(position).getSubject();
        checkBoxStudent.setChecked(all_student.contains(value));

        value = names + "," + roll + ",Absent," + attendanceModels.get(position).getSubject();
        checkBoxAbsent.setChecked(all_student.contains(value));

        checkBoxStudent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = (int) buttonView.getTag();
            String value1 = attendanceModels.get(pos).getName() + "," + attendanceModels.get(pos).getRoll_no() + ",Present," + attendanceModels.get(pos).getSubject();
            if (isChecked) {
                all_student.add(value1);
                checkBoxAbsent.setChecked(false);
            } else {
                all_student.remove(value1);
            }
        });

        checkBoxAbsent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = (int) buttonView.getTag();
            String value1 = attendanceModels.get(pos).getName() + "," + attendanceModels.get(pos).getRoll_no() + ",Absent," + attendanceModels.get(pos).getSubject();
            if (isChecked) {
                all_student.add(value1);
                checkBoxStudent.setChecked(false);
            } else {
                all_student.remove(value1);
            }
        });
        int size = attendanceModels.size();
        int sizes = all_student.size();


        if (position == attendanceModels.size() - 1) {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(v -> {

                if (sizes == size){
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.setStudentList(new ArrayList<>(all_student), attendanceModels.get(position).getSubject());
                    bottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());

                }
                else {
                    Toast.makeText(context, "Mark all attendance", Toast.LENGTH_SHORT).show();
                }


            });
        } else {
            holder.button.setVisibility(View.GONE);
        }
    }





    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    public static class AttendenceViewHolder extends RecyclerView.ViewHolder {

        TextView name;  // Add other views here if needed
        Button button;
        CheckBox presentBox, absentBox;
        ImageView sheet;

        public AttendenceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkboxStudent);
            button = itemView.findViewById(R.id.buttonMarkAttendance);
            sheet = itemView.findViewById(R.id.icon_rename);
            // Initialize other views (e.g., roll_no) here if needed
            presentBox = itemView.findViewById(R.id.checkBoxPresent);
            absentBox = itemView.findViewById(R.id.checkBoxAbsent);
        }
    }
}

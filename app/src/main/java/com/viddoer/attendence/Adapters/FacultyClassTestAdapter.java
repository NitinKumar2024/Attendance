package com.viddoer.attendence.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.Faculties.Class_Test.ClassTestBottomSheet;
import com.viddoer.attendence.Faculties.StudentDeatailsBottomSheet;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FacultyClassTestAdapter extends RecyclerView.Adapter<FacultyClassTestAdapter.AttendenceViewHolder> {

    List<AttendenceModel> attendanceModels;
    Set<String> class_test = new HashSet<>();
    Map<Integer, Boolean> checkedMap = new HashMap<>(); // Maintain checked state

    Context context;

    public FacultyClassTestAdapter(List<AttendenceModel> attendanceModels, Context context) {
        this.attendanceModels = attendanceModels;
        this.context = context;
        // Initialize checked state
        for (int i = 0; i < attendanceModels.size(); i++) {
            checkedMap.put(i, false); // Initially, all checkboxes are unchecked
        }
    }

    @NonNull
    @Override
    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_class_test, parent, false);
        return new AttendenceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AttendenceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String names = attendanceModels.get(position).getName();
        String roll = attendanceModels.get(position).getRoll_no().substring(7, 10);
        String email = attendanceModels.get(position).getEmail();
        String subject = attendanceModels.get(position).getSubject();
        String subject_name = attendanceModels.get(position).getSubject_name();
        String subject_code = attendanceModels.get(position).getSubject_name();
        String registration = attendanceModels.get(position).getRegistration();
        holder.editText.setText(attendanceModels.get(position).getInputValue());


        holder.name.setText(names + " " + "(" + roll + ")");


        // Get the current AttendenceModel
        AttendenceModel currentModel = attendanceModels.get(position);

        // Set tag for the EditText
        holder.editText.setTag(currentModel);

        holder.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // When the EditText loses focus, update the attendanceModels list
                if (position != RecyclerView.NO_POSITION) {
                    String newValue = holder.editText.getText().toString().trim();

                    // If EditText is empty, set default value to "Absent"
                    if (newValue.isEmpty()) {
                        newValue = "Absent";
                        holder.editText.setText(newValue); // Update EditText with default value
                    }

                    attendanceModels.get(position).setInputValue(newValue);

                    // Clear the class_test set before adding updated values
                    class_test.clear();

                    // Iterate through attendanceModels to add updated values to class_test
                    for (AttendenceModel model : attendanceModels) {
                        String inputValue = model.getInputValue();
                        if (inputValue != null && !inputValue.isEmpty()) {
                            String presentKey = model.getName() + "," + model.getRoll_no() + "," + inputValue + "," + model.getSubject();
                            class_test.add(presentKey);
                        }
                    }
                }
            }
        });



        holder.sheet.setOnClickListener(v -> {
            StudentDeatailsBottomSheet bottomSheetFragment = new StudentDeatailsBottomSheet();
            bottomSheetFragment.setStudentList(attendanceModels.get(position).getNumber(), email, subject, names, roll, subject_name, subject_code, registration);
            bottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());

        });




        if (position == attendanceModels.size() - 1) {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(v -> {
                // Remove focus from the EditText to ensure its value is updated
                holder.editText.clearFocus();

                // Check if attendance for all students is marked
                boolean allAttendanceMarked = isAllAttendanceMarked();

                if (allAttendanceMarked) {
                    ClassTestBottomSheet bottomSheetFragment = new ClassTestBottomSheet();
                    bottomSheetFragment.setStudentList(new ArrayList<>(class_test), attendanceModels.get(position).getSubject_name(), attendanceModels.get(position).getClass_test_number());
                    bottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());
                } else {
                    Toast.makeText(context, "Mark all attendance", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.button.setVisibility(View.GONE);
        }

    }
    private boolean isAllAttendanceMarked() {
        // Iterate through the attendance status of each student
        for (AttendenceModel model : attendanceModels) {
            // Check if the input value is null
            String presentKey = model.getName() + "," + model.getRoll_no() + "," + model.getInputValue() + "," + model.getSubject();

            if (model.getInputValue() == null || model.getInputValue().isEmpty()) {

                class_test.remove(presentKey);
//                return false; // If input value is not provided, return false
            }


            // Add the attendance status to the class_test set
            class_test.add(presentKey);
        }

        // Check if the class_test set contains all attendance statuses
        return class_test.size() == attendanceModels.size();
    }






    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    public static class AttendenceViewHolder extends RecyclerView.ViewHolder {

        TextView name;  // Add other views here if needed
        Button button;

        ImageView sheet;
        EditText editText;

        public AttendenceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkboxStudent);
            button = itemView.findViewById(R.id.buttonMarkAttendance);
            sheet = itemView.findViewById(R.id.icon_rename);
            // Initialize other views (e.g., roll_no) here if needed

            editText = itemView.findViewById(R.id.input_number_EditText);
        }
    }
}
package com.viddoer.attendence.Faculties;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viddoer.attendence.Faculties.Class_Test.ClassTestViewDownload;
import com.viddoer.attendence.Faculties.Class_Test.FacultyClassTest;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentFrontDashboard.AllStudentRankDisplayActivity;
import com.viddoer.attendence.Students.class_test.ClassTestNumberDisplay;


public class FacultySubjectWiseDashboard extends Fragment {

    private String branch;
    private String semester;
    private String subject, subject_code;
    CardView cardView1, cardView2, cardView3, cardView4;
    TextView textView1, textView3;

    public void setData(String branch, String semester, String subject, String subject_code) {
        this.branch = branch;
        this.semester = semester;
        this.subject = subject;
        this.subject_code = subject_code;
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_student_front, container, false);


        // Rest of your fragment code...

        cardView1 = view.findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView3 = view.findViewById(R.id.class_test);
        cardView4 = view.findViewById(R.id.cardView4);
        textView1 = view.findViewById(R.id.textView1);
        textView3 = view.findViewById(R.id.textView3);

        cardView4.setVisibility(View.GONE);
        textView1.setText("Class Test");
        textView3.setText("Download Class Test");

        cardView2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllStudentRankDisplayActivity.class);
            intent.putExtra("branch_code", branch);
            intent.putExtra("subject_code", subject_code);
            intent.putExtra("subject", subject);
            startActivity(intent);
        });
        cardView1.setOnClickListener(v -> {

            chooseExportAlert();

        });

        cardView3.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), ClassTestViewDownload.class);
            intent.putExtra("branch_code", branch);
            intent.putExtra("subject_code", subject_code);
            intent.putExtra("subject", subject);


            v.getContext().startActivity(intent);
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void chooseExportAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_dialog_export_options, null);

        // Set the custom view to the dialog
        builder.setView(view);

        // Find views in the custom layout
        Button test1 = view.findViewById(R.id.btn_pdf);
        Button test2 = view.findViewById(R.id.btn_excel);
        Button test3 = view.findViewById(R.id.btn2);
        Button test4 = view.findViewById(R.id.btn3);
        TextView textView = view.findViewById(R.id.dialog_title);
        textView.setText("Choose Class Test Number");
        test3.setVisibility(View.VISIBLE);
        test4.setVisibility(View.VISIBLE);
        test1.setText("Class Test 1");
        test2.setText("Class Test 2");

        // Set onClickListeners for the buttons
        test1.setOnClickListener(v -> {
            // Handle PDF export
            // You can add your PDF export logic here
            pushActivity("1");


        });

        test2.setOnClickListener(v -> {
            // Handle Excel export
            // You can add your Excel export logic here
            pushActivity("2");
        });
        test3.setOnClickListener(v -> {
            // Handle Excel export
            // You can add your Excel export logic here
            pushActivity("3");
        });
        test4.setOnClickListener(v -> {
            // Handle Excel export
            // You can add your Excel export logic here
            pushActivity("4");
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void pushActivity(String class_test_number){
        Intent intent = new Intent(getActivity(), FacultyClassTest.class);
        intent.putExtra("branch_code", branch);
        intent.putExtra("subject_code", subject_code);
        intent.putExtra("subject", subject);
        intent.putExtra("semester", semester);
        intent.putExtra("class_test", class_test_number);
        startActivity(intent);
    }
}
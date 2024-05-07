package com.viddoer.attendence.Principle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viddoer.attendence.R;

public class FacultyProfile extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_profile);

        TextView faculty_name = findViewById(R.id.textViewName);
        TextView faculty_number = findViewById(R.id.text_phone);
        TextView faculty_email = findViewById(R.id.email);
        TextView reg = findViewById(R.id.textViewRegNumber);
        reg.setVisibility(View.GONE);


        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        String email = getIntent().getStringExtra("email");



        faculty_name.setText(name);
        faculty_number.setText("Phone: " + number);
        faculty_email.setText("Email: " + email);



    }

}
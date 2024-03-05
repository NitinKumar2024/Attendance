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
        setContentView(R.layout.activity_faculty_profile);

        TextView faculty_name = findViewById(R.id.textViewName);
        TextView faculty_number = findViewById(R.id.textViewPhoneNumber);
        TextView faculty_subject = findViewById(R.id.textViewSubject);
        TextView faculty_password = findViewById(R.id.textViewPassword);
        Button button = findViewById(R.id.buttonEditProfile);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        String subject = getIntent().getStringExtra("subject");
        String password = getIntent().getStringExtra("password");
        String id = getIntent().getStringExtra("id");

        faculty_name.setText(name);
        faculty_number.setText("Phone: " + number);
        faculty_subject.setText(subject);
        faculty_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faculty_password.setText(password);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the id from the intent
                String id = getIntent().getStringExtra("id");

                String name = getIntent().getStringExtra("name");
                String number = getIntent().getStringExtra("number");
                String subject = getIntent().getStringExtra("subject");
                String password = getIntent().getStringExtra("password");

                showEditProfileBottomSheet(name, number, subject, password, id);
            }
        });
    }

    private void showEditProfileBottomSheet(String name, String number, String subject, String password, String id) {
        EditProfileFragment bottomSheetFragment = EditProfileFragment.newInstance(name, number, subject, password, id);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

}
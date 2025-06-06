package com.viddoer.attendence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.viddoer.attendence.Authentication.Login;
import com.viddoer.attendence.Authentication.TeacherLogin;
import com.viddoer.attendence.Students.StudentFrontDashboard.StudentFrontActivity;

import java.util.Objects;

public class WhoAreYou extends AppCompatActivity {

    private Button student_btn, faculty_btn, principle_btn, others_btn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);


        this.getSupportActionBar().hide();

        student_btn = findViewById(R.id.studentButton);
        faculty_btn = findViewById(R.id.facultyButton);
        principle_btn = findViewById(R.id.PrincipleButton);
        others_btn = findViewById(R.id.OthersButton);



        SharedPreferences sharedPreferences = getSharedPreferences("student_login", Context.MODE_PRIVATE);
        String Email = sharedPreferences.getString("email", null);

        String semester = sharedPreferences.getString("semester", null);
        String Registration = sharedPreferences.getString("registration", null);
        String Phone = sharedPreferences.getString("phone", null);
        String name = sharedPreferences.getString("name", null);


        if (Email!=null){
            Intent intent = new Intent(WhoAreYou.this, StudentFrontActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", Email);
            intent.putExtra("semester", semester);
            intent.putExtra("registration", Registration);
            intent.putExtra("phone", Phone);
            startActivity(intent);
            finish();


        }

        student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRoleSelection("Student");

            }
        });
        faculty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRoleSelection("Faculty");
            }
        });
        principle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRoleSelection("Principle");
            }
        });
        others_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WhoAreYou.this, "Currently we are not allowing any unknown person.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleRoleSelection(String selectedRole) {
        // Add your logic here based on the selected role
        // For example, you can start a new activity or perform other actions
        if (Objects.equals(selectedRole, "Faculty")){
            Intent intent = new Intent(WhoAreYou.this, TeacherLogin.class);
            intent.putExtra("role", "faculty");
            startActivity(intent);
        } else if (Objects.equals(selectedRole,"Student")) {

            Intent intent = new Intent(WhoAreYou.this, Login.class);
            startActivity(intent);
            finish();

        }
        else if (Objects.equals(selectedRole,"Principle")) {

            Intent intent = new Intent(WhoAreYou.this, TeacherLogin.class);
            intent.putExtra("role", "principle");
            startActivity(intent);


        }

    }
}


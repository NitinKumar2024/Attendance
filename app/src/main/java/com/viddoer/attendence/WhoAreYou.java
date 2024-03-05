package com.viddoer.attendence;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viddoer.attendence.Authentication.Login;
import com.viddoer.attendence.Authentication.SignUp;
import com.viddoer.attendence.Authentication.TeacherLogin;
import com.viddoer.attendence.Principle.PrincipleDashboard;
import com.viddoer.attendence.ui.notifications.NotificationsFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
        String password = sharedPreferences.getString("password", null);
        String semester = sharedPreferences.getString("semester", null);
        String Registration = sharedPreferences.getString("registration", null);
        String Phone = sharedPreferences.getString("Phone", null);
        String name = sharedPreferences.getString("name", null);


        if (Email!=null && password != null){
            Intent intent = new Intent(WhoAreYou.this, StudentDashBoard.class);
            intent.putExtra("name", name);
            intent.putExtra("email", Email);
            intent.putExtra("semester", semester);
            intent.putExtra("registration", Registration);
            intent.putExtra("phone", Phone);
            intent.putExtra("password", password);
           // startActivity(intent);


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

        }
        else if (Objects.equals(selectedRole,"Principle")) {

            Intent intent = new Intent(WhoAreYou.this, TeacherLogin.class);
            intent.putExtra("role", "principle");
            startActivity(intent);


        }

    }
}


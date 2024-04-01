package com.viddoer.attendence.Students.StudentFrontDashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.viddoer.attendence.R;
import com.viddoer.attendence.StudentDashBoard;
import com.viddoer.attendence.WhoAreYou;

public class StudentFrontActivity extends AppCompatActivity {
    String name, email, semester, registration, phone, password, branch, roll;

    CardView cardView1, cardView2, cardView3, cardView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_front);

        this.getSupportActionBar().hide();

        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        cardView4 = findViewById(R.id.cardView4);

        // Lock the screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        semester = getIntent().getStringExtra("semester");
        registration = getIntent().getStringExtra("registration");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentFrontActivity.this, StudentDashBoard.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("semester", semester);
                intent.putExtra("registration", registration);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentFrontActivity.this, StudentChooseSubjectActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("semester", semester);
                intent.putExtra("registration", registration);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                intent.putExtra("role", "rank");
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();


            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout!");
        builder.setMessage("Do You Want to Logout of Edu Mark!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button

                SharedPreferences sharedPreferences = getSharedPreferences("student_login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(StudentFrontActivity.this, "Logout Successfully...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StudentFrontActivity.this, WhoAreYou.class));
                finish();


                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
package com.viddoer.attendence;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.viddoer.attendence.Adapters.StudentFragmentAdapter;

public class StudentDashBoard extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash_board);
        this.getSupportActionBar().hide();


        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String semester = getIntent().getStringExtra("semester");
        String registration = getIntent().getStringExtra("registration");
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new StudentFragmentAdapter(getSupportFragmentManager(), name, email, registration, phone, semester, password));
        tabLayout.setupWithViewPager(viewPager);
    }
}
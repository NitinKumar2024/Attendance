package com.viddoer.attendence;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.viddoer.attendence.Adapters.TabAttendanceAdapter;
import com.viddoer.attendence.Models.AttendenceModel;

import java.util.ArrayList;
import java.util.List;

public class AllStudentDisplay extends AppCompatActivity {
    ProgressDialog progressDialog;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student_display);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        this.getSupportActionBar().hide();

        this.getSupportActionBar().hide();
        List<AttendenceModel> contactList = new ArrayList<>();
        String branch = getIntent().getStringExtra("branch");
        String semester = getIntent().getStringExtra("semester");
        String subject = getIntent().getStringExtra("subject");
        String subject_code = getIntent().getStringExtra("subject_code");
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new TabAttendanceAdapter(getSupportFragmentManager(), branch, semester, subject, subject_code));
        tabLayout.setupWithViewPager(viewPager);
        // Set the current tab to the second position (Remark Attendance)
        viewPager.setCurrentItem(1); // Index 1 corresponds to the second tab


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllStudentDisplay.this));

    }}

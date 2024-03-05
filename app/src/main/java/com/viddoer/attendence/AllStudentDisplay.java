package com.viddoer.attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viddoer.attendence.Adapters.AttendenceAdapter;
import com.viddoer.attendence.Adapters.TabAttendanceAdapter;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.Students.BottomSheetFragment;

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
        // Lock the screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Student Details").child(branch + "/" + semester);

//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
//                        // Get the values for each child
//                        String email = studentSnapshot.child("User Name").getValue(String.class);
//                        String roll_no = studentSnapshot.child("Roll_no").getValue(String.class);
//
//                        if (email != null) {
//                            contactList.add(new AttendenceModel(email, roll_no, subject));
//                        } else {
//                            Toast.makeText(AllStudentDisplay.this, "Null Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    progressDialog.dismiss();
//                    // Set up the RecyclerView adapter after retrieving all data
//                    AttendenceAdapter adapter = new AttendenceAdapter(contactList, getApplicationContext());
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Toast.makeText(AllStudentDisplay.this, "No data available", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle database read error here
//                Toast.makeText(AllStudentDisplay.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });


//
    }}

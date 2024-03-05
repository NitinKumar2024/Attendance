package com.viddoer.attendence.Principle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viddoer.attendence.Adapters.AttendenceAdapter;
import com.viddoer.attendence.Adapters.PrincipleTabAdapter;
import com.viddoer.attendence.Adapters.TabAttendanceAdapter;
import com.viddoer.attendence.Authentication.SignUp;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.WhoAreYou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipleDashboard extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ProgressBar progressBar;
    String url = "https://viddoer.com/attendance/principle_dashboard.php";
    List<FacultyItem> facultyList = new ArrayList<>();

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principle_dashboard);
        this.getSupportActionBar().hide();
        // Lock the screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new PrincipleTabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        // Set the current tab to the second position (Remark Attendance)
        viewPager.setCurrentItem(1); // Index 1 corresponds to the second tab

//        // Initialize Firebase
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        progressBar = findViewById(R.id.threeDotSpinner);
//        // Replace "your-database-url" with your Firebase Realtime Database URL
//        databaseReference = firebaseDatabase.getReference();
//        // Permission is not granted, request the permission
//        ActivityCompat.requestPermissions(PrincipleDashboard.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//
//
//
//        FloatingActionButton floatingActionButton = findViewById(R.id.add_note);
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PrincipleDashboard.this, TeacherDetails.class);
//                startActivity(intent);
//            }
//        });

    }

//    private void retrieveAIStudents(String branch, String semester) {
//        progressBar.setVisibility(View.VISIBLE);
//
//        RequestQueue requestQueue = Volley.newRequestQueue(PrincipleDashboard.this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            parseJSONResponse(jsonArray);
//                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
//                            FacultyAdapter facultyAdapter = new FacultyAdapter(facultyList, PrincipleDashboard.this);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(PrincipleDashboard.this));
//                            recyclerView.setAdapter(facultyAdapter);
//                            progressBar.setVisibility(View.GONE);
//                        } catch (JSONException e) {
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(PrincipleDashboard.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(PrincipleDashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                // Add parameters if needed
//                return params;
//            }
//        };
//
//        requestQueue.add(stringRequest);
//    }
//
//    private void parseJSONResponse(JSONArray response) {
//        try {
//            for (int i = 0; i < response.length(); i++) {
//                JSONObject studentObject = response.getJSONObject(i);
//                String name = studentObject.getString("username"); // Adjust key according to PHP response
//                String email = studentObject.getString("email"); // Adjust key according to PHP response
//                String number = studentObject.getString("number"); // Adjust key according to PHP response
//                String subject = studentObject.getString("subject"); // Adjust key according to PHP response
//                String password = studentObject.getString("password"); // Adjust key according to PHP response
//
//                facultyList.add(new FacultyItem(name, subject, number, email, password));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


//    private void retrieveData() {
//        progressBar.setVisibility(View.VISIBLE);
//        databaseReference.child("faculties").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<FacultyItem> facultyList = new ArrayList<>();
//
//                for (DataSnapshot facultySnapshot : dataSnapshot.getChildren()) {
//                    String name = facultySnapshot.child("name").getValue(String.class);
//                    String subject = facultySnapshot.child("subject").getValue(String.class);
//                    String number = facultySnapshot.child("number").getValue(String.class);
//                    String password = facultySnapshot.child("password").getValue(String.class);
//
//                    facultyList.add(new FacultyItem(name, subject, number, password, facultySnapshot.getKey()));
//                }
//
//                RecyclerView recyclerView = findViewById(R.id.recyclerView);
//                FacultyAdapter facultyAdapter = new FacultyAdapter(facultyList, PrincipleDashboard.this);
//                recyclerView.setLayoutManager(new LinearLayoutManager(PrincipleDashboard.this));
//                recyclerView.setAdapter(facultyAdapter);
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }

}
package com.viddoer.attendence.Faculties.Class_Test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viddoer.attendence.Adapters.FacultyClassTestAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyClassTest extends AppCompatActivity {

    List<AttendenceModel> contactList = new ArrayList<>();
    private String branch;
    private String semester;
    private String subject, subject_code, class_test_number;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    // Define your PHP script URL
    private static final String PHP_SCRIPT_URL = ApiUrls.RemarkAttendence_PHP_SCRIPT_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student_rand_display);
        this.getSupportActionBar().hide();

        semester = getIntent().getStringExtra("semester");
        subject = getIntent().getStringExtra("subject");
        subject_code = getIntent().getStringExtra("subject_code");
        branch = getIntent().getStringExtra("branch_code");
        class_test_number = getIntent().getStringExtra("class_test");

        progressBar = findViewById(R.id.threeDotSpinner);


        progressBar.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(subject + " ( " + class_test_number + " )");


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveAIStudents(branch, semester);

    }

    private void retrieveAIStudents(String branch, String semester) {

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        String shared_name = "teacher_login";

        SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
        String college_code = sharedPreferencest.getString("college_code", null);

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("branch", branch);
            studentObject.put("semester", semester);
            studentObject.put("college_code", college_code);


            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        //   String name_s = "Mathematics";
        try {
            jsonObject.put("students", jsonArray);

            //  jsonObject.put("subject", name_s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        try {
                            // Trim the response string to remove any leading/trailing whitespaces
                            response = response.trim();

                            // Check if the response is not null and not empty
                            if (!TextUtils.isEmpty(response)) {
                                // Try to create a JSONArray from the response string
                                JSONArray jsonArray = new JSONArray(response);
                                Toast.makeText(FacultyClassTest.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                FacultyClassTestAdapter adapter = new FacultyClassTestAdapter(contactList, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Handle the case where the response is empty
                                Toast.makeText(FacultyClassTest.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(FacultyClassTest.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        Toast.makeText(FacultyClassTest.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a HashMap to store parameters
                Map<String, String> params = new HashMap<>();
                params.put("students", jsonObject.toString()); // Add student JSON object as a parameter
                return params;
            }
        };

// Add the request to the request queue
        requestQueue.add(stringRequest);

    }

    private void parseJSONResponse(JSONArray response) {

        // Parse the JSON response and populate studentList
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String name = studentObject.getString("username");
                String rollNo = studentObject.getString("Reg");
                String branch = studentObject.getString("branch");
                String email = studentObject.getString("email");
                String number = studentObject.getString("number");
                String semester = studentObject.getString("semester");
                // Add the parsed data to the studentList
                String complete_subject = branch + subject_code;

                contactList.add(new AttendenceModel(name, rollNo, complete_subject, branch, email, number, semester, subject_code, class_test_number, rollNo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
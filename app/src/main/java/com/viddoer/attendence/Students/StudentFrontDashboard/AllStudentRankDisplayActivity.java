package com.viddoer.attendence.Students.StudentFrontDashboard;

import android.annotation.SuppressLint;
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
import com.viddoer.attendence.Adapters.StudentListRankAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Models.StudentListRankModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.VolleyRequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllStudentRankDisplayActivity extends AppCompatActivity {

    List<StudentListRankModel> contactList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String branch_code, subject, subject_code, roll, complete_subject;
    // Define your PHP script URL
    private static final String PHP_SCRIPT_URL = ApiUrls.AllStudentRandDisplayActivity_url;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student_rand_display);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.threeDotSpinner);

        branch_code = getIntent().getStringExtra("branch_code");
        subject_code = getIntent().getStringExtra("subject_code");
        roll = getIntent().getStringExtra("roll");
        subject = getIntent().getStringExtra("subject");
        String best_subject = getIntent().getStringExtra("complete_subject");
        complete_subject = branch_code + subject_code;


        progressBar.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(subject);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveAIStudents(complete_subject);
       // retrieveRank(complete_subject);

    }



    private void retrieveAIStudents(String subject) {

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("subject", subject_code);



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
//                                TextView textView = findViewById(R.id.textViewTitle);
//                                textView.setText(response);
//                                textView.setTextIsSelectable(true);
                             //   Toast.makeText(AllStudentRankDisplayActivity.this, response, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(response);
                                Toast.makeText(AllStudentRankDisplayActivity.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                StudentListRankAdapter adapter = new StudentListRankAdapter(getApplicationContext(), contactList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Handle the case where the response is empty
                                Toast.makeText(AllStudentRankDisplayActivity.this, "Empty response received", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(AllStudentRankDisplayActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        Toast.makeText(AllStudentRankDisplayActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

        // Create a Set to store unique dates
        Set<String> uniqueDates = new HashSet<>();

        // Parse the JSON response and populate studentList
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String username = studentObject.getString("Username");
                String name = studentObject.getString("username");
                String rollNo = studentObject.getString("Roll");
                String user_statusAbsent = studentObject.getString("AbsentDays");
                String user_statusPresent = studentObject.getString("PresentDays");

                int num_absent = Integer.parseInt(user_statusAbsent);
                int num_present = Integer.parseInt(user_statusPresent);
                int total_days = num_present + num_absent;
                float percentage = calculatePercentage(num_present, total_days);
                contactList.add(new StudentListRankModel(rollNo, username, "Rank: " + String.valueOf(i + 1), (int) percentage));


                // Check if the date is already encountered
                if (!uniqueDates.contains(username)) {
                    // Add the date to the set to mark it as encountered
                    uniqueDates.add(username);



                } else {
                    // Handle repetition of date here if needed
                    // For example, log a warning or skip adding to contactList
                    // Add the parsed data to the studentList
                //    contactList.add(new AdvancedAttendanceModel(name, rollNo, status, null));
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private float calculatePercentage(int presentCount, int totalCount) {
        if (totalCount == 0) {
            return 0;
        }
        return ((float) presentCount / totalCount) * 100;
    }
}
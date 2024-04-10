package com.viddoer.attendence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
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
import com.viddoer.attendence.Adapters.FacultyAllSubjectAdapter;
import com.viddoer.attendence.Models.FacultyAllSubjectModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class SelectSubject extends AppCompatActivity {

    List<FacultyAllSubjectModel> subjectList; // Declare but don't initialize here
    String branch, semester;
    FacultyAllSubjectAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private static final String PHP_SCRIPT_URL = ApiUrls.SelectSubject_PHP_SCRIPT_URL;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        progressBar = findViewById(R.id.threeDotSpinner);


        branch = getIntent().getStringExtra("branch");
        semester = getIntent().getStringExtra("semester");
        SharedPreferences sharedPreferencest = getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
        String Emails = sharedPreferencest.getString("email", null);
        subjectList = new ArrayList<>();


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Set up LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        retrieveAIStudents(branch, semester, Emails);

//        // Initialize subjectList if null
//        if (subjectList == null) {
//            subjectList = new ArrayList<>();
//            loadSubjectsFromSharedPreferences(); // Load subjects from SharedPreferences
//        }

        // Set up the adapter




    }

    private void retrieveAIStudents(String branch, String semester, String email) {
        progressBar.setVisibility(View.VISIBLE);

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("branch_code", branch);
            studentObject.put("semester", semester);
            studentObject.put("email", email);


            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(SelectSubject.this);

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
                             //  Toast.makeText(SelectSubject.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                // Handle the case where the response is empty
                                Toast.makeText(SelectSubject.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(SelectSubject.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectSubject.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                String subjectName = studentObject.getString("subject");
                String subjectCode = studentObject.getString("subject_code");
                String branch_code = studentObject.getString("branch_code");

                // Add the parsed data to the studentList
                // Add the new subject to the list in memory
              //  Toast.makeText(this, subjectName, Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(subjectName) || TextUtils.isEmpty(subjectCode) || TextUtils.isEmpty(branch_code)){
                    Toast.makeText(this, "No Subject Assigned", Toast.LENGTH_SHORT).show();
                }
                else {

                    subjectList.add(new FacultyAllSubjectModel(branch_code, semester, subjectName, subjectCode));
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FacultyAllSubjectAdapter(this, subjectList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Notify adapter that data set has changed
    }


//    private void loadSubjectsFromSharedPreferences() {
//        // Retrieve subjects from SharedPreferences as a list
//        SharedPreferences sharedPreferences = getSharedPreferences("subjectList", Context.MODE_PRIVATE);
//        String subjectListString = sharedPreferences.getString("subjects", "");
//        List<String> subjectStringList = Arrays.asList(subjectListString.split(","));
//
//        // Display each subject
//        for (String subject : subjectStringList) {
//            String[] parts = subject.split(":");
//            if (parts.length >= 4) { // Ensure the subject string contains all necessary parts
//                String subjectName = parts[0];
//                String subjectCode = parts[1];
//                String branchs = parts[2];
//                String semesters = parts[3];
//
//                if (branch.equals(branchs) && semester.equals(semesters)) {
//                    subjectList.add(new FacultyAllSubjectModel(branch, semester, subjectName, subjectCode));
//                }
//            }
//        }
//    }
//
//    private void showAddSubjectDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.alert_input_subject, null);
//        builder.setView(dialogView);
//
//        final EditText subjectNameEditText = dialogView.findViewById(R.id.subject_name);
//        final EditText subjectCodeEditText = dialogView.findViewById(R.id.subject_code);
//
//        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String subjectName = subjectNameEditText.getText().toString();
//                String subjectCode = subjectCodeEditText.getText().toString();
//
//                if (TextUtils.isEmpty(subjectName) || TextUtils.isEmpty(subjectCode)){
//                    Toast.makeText(SelectSubject.this, "Fill Details", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    // Save subjectName and subjectCode to SharedPreferences as a list
//                    SharedPreferences sharedPreferences = getSharedPreferences("subjectList", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                    // Retrieve the existing list of subjects
//                    String subjectListString = sharedPreferences.getString("subjects", "");
//                    List<String> subjectStringList = new ArrayList<>(Arrays.asList(subjectListString.split(",")));
//
//                    // Add the new subject to the list
//                    String newSubject = subjectName + ":" + subjectCode + ":" + branch + ":" +semester;
//                    subjectStringList.add(newSubject);
//
//                    // Join the list into a comma-separated string and save it back to SharedPreferences
//                    editor.putString("subjects", TextUtils.join(",", subjectStringList));
//                    editor.apply();
//
//                    // Add the new subject to the list in memory
//                    subjectList.add(new FacultyAllSubjectModel(branch, semester, subjectName, subjectCode));
//                    adapter.notifyDataSetChanged(); // Notify adapter that data set has changed
//                }
//
//                // Process the subjectName and subjectCode as needed
//                Toast.makeText(SelectSubject.this, "Subject Name: " + subjectName + "\nSubject Code: " + subjectCode, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
}

package com.viddoer.attendence.Principle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrincipalAssignTeacher extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteFaculty;
    private AutoCompleteTextView autoCompleteSubject;
    private Button buttonAssignSubject;
    ProgressDialog progressDialog;
    ArrayList<String> all_subject = new ArrayList<>();
    ArrayList<String> all_faculty = new ArrayList<>();
    private static final String PHP_SCRIPT_URL = ApiUrls.PrincipalAssignTeacher_PHP_SCRIPT_URL;
    private static final String PHP_SCRIPT_URL2 = ApiUrls.PrincipalAssignTeacher_PHP_SCRIPT_URL2;
    private static final String PHP_SCRIPT_URL3 = ApiUrls.PrincipalAssignTeacher_PHP_SCRIPT_URL3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_assign_teacher);

        // Lock the screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoCompleteFaculty = findViewById(R.id.autoCompleteFaculty);
        autoCompleteSubject = findViewById(R.id.autoCompleteSubject);
        buttonAssignSubject = findViewById(R.id.buttonAssignSubject);
        progressDialog = new ProgressDialog(PrincipalAssignTeacher.this);
        progressDialog.setMessage("Loading...");


        fetch_all_subject("jg");
        retrieve_faculty("jhfg");


        // Set up ArrayAdapter for faculties
        ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, all_faculty);
        autoCompleteFaculty.setAdapter(facultyAdapter);

        // Set up ArrayAdapter for subjects
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, all_subject);
        autoCompleteSubject.setAdapter(subjectAdapter);

        // Set up onClickListener for the "Assign Subject" button
        buttonAssignSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFaculty = autoCompleteFaculty.getText().toString().trim();
                String selectedSubject = autoCompleteSubject.getText().toString().trim();

                if (all_subject.contains(selectedSubject) && all_faculty.contains(selectedFaculty)) {
                    // Valid subject selection
                    if (!TextUtils.isEmpty(selectedFaculty) || !TextUtils.isEmpty(selectedSubject)) {
                        String[] parts = selectedFaculty.split(",");
                        String first_name = parts[0];
                        String second_email = parts[1];
                        String[] part = selectedSubject.split(",");
                        String first_subject = part[0];
                        String second_subject_code = part[1];
                        String third_branch = part[2];
                        String fourth_branch_code = part[3];
                        String fifth_semester = part[4];
                        insert_faculty_with_php(first_name, second_email, first_subject, second_subject_code, third_branch, fourth_branch_code, fifth_semester);
                      //  Toast.makeText(PrincipalAssignTeacher.this,  selectedFaculty + ", Subject: " + selectedSubject, Toast.LENGTH_SHORT).show();
                    } else {
                        // Faculty is not selected
                        Toast.makeText(PrincipalAssignTeacher.this, "Please select a faculty.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Invalid subject selection
                    Toast.makeText(PrincipalAssignTeacher.this, "Please select a valid subject.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insert_faculty_with_php(String firstName, String secondEmail, String firstSubject, String secondSubjectCode, String thirdBranch, String fourthBranchCode, String fifth_semester) {

        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("username", firstName);
            studentObject.put("email", secondEmail);
            studentObject.put("subject", firstSubject);
            studentObject.put("subject_code", secondSubjectCode);
            studentObject.put("branch", thirdBranch);
            studentObject.put("branch_code", fourthBranchCode);
            studentObject.put("semester", fifth_semester);
          //  Toast.makeText(this, fifth_semester, Toast.LENGTH_SHORT).show();

            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(PrincipalAssignTeacher.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {
                            // Try to create a JSONArray from the response string
                            autoCompleteSubject.setText("");
                            Toast.makeText(PrincipalAssignTeacher.this, "Successfully Assigned.", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        } else {
                            // Handle the case where the response is empty
                            progressDialog.dismiss();
                            Toast.makeText(PrincipalAssignTeacher.this, "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(PrincipalAssignTeacher.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void fetch_all_subject(String branch) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("branch", branch);

            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(PrincipalAssignTeacher.this);

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
                              //  Toast.makeText(PrincipalAssignTeacher.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                progressDialog.dismiss();
                            } else {
                                // Handle the case where the response is empty
                                progressDialog.dismiss();
                                Toast.makeText(PrincipalAssignTeacher.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // Handle JSON parsing error
                            Toast.makeText(PrincipalAssignTeacher.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(PrincipalAssignTeacher.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        // Parse the JSON response and populate the all_subject list
        try {
            all_subject.clear(); // Clear the list before adding new subjects
            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String subject = studentObject.getString("Subject");
                String subject_code = studentObject.getString("Subject_Code");
                String branch = studentObject.getString("Branch");
                String branch_code = studentObject.getString("Branch_Code");
                String semester = studentObject.getString("semester");
                all_subject.add(subject + "," + subject_code + "," + branch + "," + branch_code + "," + semester);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void retrieve_faculty(String branch) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("branch", branch);

            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(PrincipalAssignTeacher.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL2,
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
                             //   Toast.makeText(PrincipalAssignTeacher.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse_faculty(jsonArray);
                                progressDialog.dismiss();
                            } else {
                                // Handle the case where the response is empty
                                progressDialog.dismiss();
                                Toast.makeText(PrincipalAssignTeacher.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // Handle JSON parsing error
                            Toast.makeText(PrincipalAssignTeacher.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(PrincipalAssignTeacher.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void parseJSONResponse_faculty(JSONArray response) {
        // Parse the JSON response and populate the all_subject list
        try {
            all_faculty.clear(); // Clear the list before adding new subjects
            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String subject = studentObject.getString("username");
                String email = studentObject.getString("email");
                all_faculty.add(subject + "," + email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

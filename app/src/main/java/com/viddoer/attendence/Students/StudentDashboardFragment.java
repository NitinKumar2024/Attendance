package com.viddoer.attendence.Students;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.viddoer.attendence.Adapters.StudentSubjectAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Models.StudentSubjectModel;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentDashboardFragment extends Fragment {
    private String name, email, registration, phone, semester, password, branch, roll;
    private static final String PHP_SCRIPT_URL = ApiUrls.StudentDashboardFragment_PHP_SCRIPT_URL;
    ProgressBar progressBar;
    List<StudentSubjectModel> subjectList = new ArrayList<>();
    RecyclerView recyclerView;

    public void setData(String name, String email, String registration, String phone, String semester, String password) {
        this.name = name;
        this.email = email;
        this.registration = registration;
        this.phone = phone;
        this.semester = semester;
        this.password = password;
    }
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_dashboard, container, false);

        progressBar = view.findViewById(R.id.threeDotSpinner);

        branch = registration.substring(3, 5);
        roll = registration.toString();
        // Assuming you have a list of FacultySubjectModel

        // Add more subjects as needed

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // Set up LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Set up the adapter
        StudentSubjectAdapter adapter = new StudentSubjectAdapter(getActivity(), subjectList);
        recyclerView.setAdapter(adapter);

        fetch_all_subject(branch, semester);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.refresh);
        floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetch_all_subject(branch, semester);
            }
        });






        return view;
    }

    private void fetch_all_subject(String branch, String semester) {
        progressBar.setVisibility(View.VISIBLE);

        String shared_name = "student_login";
        SharedPreferences sharedPreferencest = getActivity().getSharedPreferences(shared_name, Context.MODE_PRIVATE);
        String college_code = sharedPreferencest.getString("registration", null).substring(0, 2);

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("branch", branch);
            studentObject.put("semester", semester);
            studentObject.put("college_code", college_code);

            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }

        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Handle the case where the response is empty
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            // Handle JSON parsing error
                            Toast.makeText(getActivity(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        System.out.println(error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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

            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String subject = studentObject.getString("Subject");
                String subject_code = studentObject.getString("Subject_Code");
                String branch = studentObject.getString("Branch");
                String branch_code = studentObject.getString("Branch_Code");
                String semester = studentObject.getString("semester");
                subjectList.add(new StudentSubjectModel(subject_code, branch_code, roll, subject, "report", registration));

            }



            // Set up LinearLayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
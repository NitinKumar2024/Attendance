package com.viddoer.attendence.Faculties;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.viddoer.attendence.Adapters.AttendenceAdapter;
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


public class RemarkAttendence extends Fragment {

    List<AttendenceModel> contactList = new ArrayList<>();
    private String branch;
    private String semester;
    private String subject, subject_code;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    // Define your PHP script URL
    private static final String PHP_SCRIPT_URL = ApiUrls.RemarkAttendence_PHP_SCRIPT_URL;

    public void setData(String branch, String semester, String subject, String subject_code) {
        this.branch = branch;
        this.semester = semester;
        this.subject = subject;
        this.subject_code = subject_code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remark_attendence, container, false);

        progressBar = view.findViewById(R.id.threeDotSpinner);


        progressBar.setVisibility(View.VISIBLE);
        TextView textView = view.findViewById(R.id.textViewTitle);
        textView.setText(subject);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        retrieveAIStudents(branch, semester);


        return view;
    }

    private void retrieveAIStudents(String branch, String semester) {

        String shared_name = "teacher_login";
        SharedPreferences sharedPreferencest = getActivity().getSharedPreferences(shared_name, Context.MODE_PRIVATE);
        String college_code = sharedPreferencest.getString("college_code", null);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                response -> {
                    // Handle response from server
                    try {
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {
                            // Try to create a JSONArray from the response string
                            JSONArray jsonArray1 = new JSONArray(response);
                            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                            // If successful, parse the JSON array
                            parseJSONResponse(jsonArray1);
                            AttendenceAdapter adapter = new AttendenceAdapter(contactList, requireActivity().getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Handle the case where the response is empty
                            Toast.makeText(getActivity(), "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Handle JSON parsing error
                        Toast.makeText(getActivity(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    System.out.println(error.toString());
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
                String rollNo = studentObject.getString("Reg").substring(7, 10);
                String reg = studentObject.getString("Reg");
                String branch = studentObject.getString("branch");
                String email = studentObject.getString("email");
                String number = studentObject.getString("number");
                String semester = studentObject.getString("semester");
                // Add the parsed data to the studentList
                String complete_subject = branch + subject_code;

                contactList.add(new AttendenceModel(name, rollNo, complete_subject, branch, email, number, semester, subject, subject_code, reg));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

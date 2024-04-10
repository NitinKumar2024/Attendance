package com.viddoer.attendence.Students.class_test;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassTestNumberDisplay extends AppCompatActivity {

    String branch_code, subject, subject_code, registration;

    // Declare TextView variables at the class level
    private TextView titleTextView;
    private TextView classTest1NumberTextView;
    private TextView classTest1PercentageTextView;
    private TextView classTest2NumberTextView;
    private TextView classTest2PercentageTextView;
    private TextView classTest3NumberTextView;
    private TextView classTest3PercentageTextView;
    private TextView classTest4NumberTextView;
    private TextView classTest4PercentageTextView;
    private static final String PHP_SCRIPT_URL = ApiUrls.ClassTestNumberDisplay_url;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_test_number_display);

        branch_code = getIntent().getStringExtra("branch_code");
        subject_code = getIntent().getStringExtra("subject_code");
        registration = getIntent().getStringExtra("registration");
        subject = getIntent().getStringExtra("subject");

        this.getSupportActionBar().hide();
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Fetching Class Test Number... ");
        progressBar.setCancelable(false);




        // Initialize TextViews by finding them by their IDs
        titleTextView = findViewById(R.id.title);
        classTest1NumberTextView = findViewById(R.id.classTest1Number);
        classTest1PercentageTextView = findViewById(R.id.classTest1Percentage);
        classTest2NumberTextView = findViewById(R.id.classTest2Number);
        classTest2PercentageTextView = findViewById(R.id.classTest2Percentage);
        classTest3NumberTextView = findViewById(R.id.classTest3Number);
        classTest3PercentageTextView = findViewById(R.id.classTest3Percentage);
        classTest4NumberTextView = findViewById(R.id.classTest4Number);
        classTest4PercentageTextView = findViewById(R.id.classTest4Percentage);

        titleTextView.setText(subject);
        progressBar.show();

        retrieveAIStudents(subject_code, registration);

    }

    private void retrieveAIStudents(String subject_code, String registration) {

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("subject_code", subject_code);
            studentObject.put("registration", registration);



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
                              //  Toast.makeText(ClassTestNumberDisplay.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);

                                progressBar.dismiss();
                            } else {
                                // Handle the case where the response is empty
                                progressBar.dismiss();
                                Toast.makeText(ClassTestNumberDisplay.this, "Empty response received", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(ClassTestNumberDisplay.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.dismiss();
                        Toast.makeText(ClassTestNumberDisplay.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("SetTextI18n")
    private void parseJSONResponse(JSONArray response) {

        // Create a Set to store unique dates
        Set<String> uniqueDates = new HashSet<>();

        // Parse the JSON response and populate studentList
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String class_test1 = studentObject.getString("ClassTest1");
                String class_test2 = studentObject.getString("ClassTest2");
                String class_test3 = studentObject.getString("ClassTest3");
                String class_test4 = studentObject.getString("ClassTest4");


                classTest1NumberTextView.setText(class_test1);
                classTest2NumberTextView.setText(class_test2);
                classTest3NumberTextView.setText(class_test3);
                classTest4NumberTextView.setText(class_test4);

                if (!class_test1.equals("Absent") && !class_test1.isEmpty() && !class_test1.equals("null")) {
                    classTest1PercentageTextView.setText(String.valueOf(percentage(Integer.parseInt(class_test1))) + "%");
                }

                if (!class_test2.equals("Absent") && !class_test2.isEmpty() && !class_test2.equals("null")) {
                    classTest2PercentageTextView.setText(String.valueOf(percentage(Integer.parseInt(class_test2)))+ "%");
                }

                if (!class_test3.equals("Absent") && !class_test3.isEmpty() && !class_test3.equals("null")) {
                    classTest3PercentageTextView.setText(String.valueOf(percentage(Integer.parseInt(class_test3)))+ "%");
                }

                if (!class_test4.equals("Absent") && !class_test4.isEmpty() && !class_test4.equals("null")) {
                    classTest4PercentageTextView.setText(String.valueOf(percentage(Integer.parseInt(class_test4)))+ "%");
                }





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int percentage(int number){
        return (number * 100) / 20;
    }


}
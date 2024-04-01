package com.viddoer.attendence.Students;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentSubjectDisplay extends AppCompatActivity {

    private CalendarView calendarView;
    private ArrayList<String> absentDates;
    private ArrayList<String> presentDates;
    private ArrayList<String> totalDays;
    ProgressBar progressBar;
    private static final String PHP_SCRIPT_URL = ApiUrls.StudentSubjectDisplay_PHP_SCRIPT_URL;
    String branch_code, subject, subject_code, roll, complete_subject;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_display);
        this.getSupportActionBar().hide();
        progressBar = findViewById(R.id.threeDotSpinner);

        presentDates = new ArrayList<>();
        absentDates = new ArrayList<>();
        totalDays = new ArrayList<>();



        branch_code = getIntent().getStringExtra("branch_code");
        subject_code = getIntent().getStringExtra("subject_code");
        roll = getIntent().getStringExtra("roll");
        subject = getIntent().getStringExtra("subject");
        String best_subject = getIntent().getStringExtra("complete_subject");
        complete_subject = branch_code + subject_code;
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(subject);

        if (best_subject!=null){
            student_attendance_filter(best_subject, roll);

        }
        else {
            student_attendance_filter(complete_subject, roll);
        }





    }
    private void plot(){
        CalendarView calendarView = findViewById(R.id.calendarView);


        int presentCount = countUniqueDates(presentDates);
        int absentCount = countUniqueDates(absentDates);
        int totalCount = countUniqueDates(totalDays);

        float presentPercentage = calculatePercentage(presentCount, totalCount);


        // Display the counts and percentage
        TextView presentCountTextView = findViewById(R.id.presentCountTextView);
        TextView absentCountTextView = findViewById(R.id.absentCountTextView);
        TextView totalCountTextView = findViewById(R.id.totalCountTextView);
        TextView percentageTextView = findViewById(R.id.percentageTextView);

        presentCountTextView.setText("Present Days: " + presentCount);
        absentCountTextView.setText("Absent Days: " + absentCount);
        totalCountTextView.setText("Total Days: " + totalCount);
        percentageTextView.setText("Percentage: " + String.format("%.2f", presentPercentage) + "%");

        // Customize chart appearance and labels if necessary
        // Add axis labels, legend, and other styling options

        PieChart pieChart = findViewById(R.id.pieChart);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(absentCount, "Absent"));
        entries.add(new PieEntry(presentCount, "Present"));

        PieDataSet dataSet = new PieDataSet(entries, "Attendance");

// Set custom colors for each entry
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);    // Color for "Absent"
        colors.add(Color.GREEN);  // Color for "Present"
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

// Customize the chart as needed
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleRadius(30f);

        pieChart.invalidate(); // Refresh the chart

        ImageView studentStatusImageView = findViewById(R.id.bottomImageView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

                // Perform actions with the selected date
               if (presentDates.contains(selectedDate)){
                   studentStatusImageView.setImageResource(R.drawable.ic_present);

               } else if (absentDates.contains(selectedDate)) {
                   studentStatusImageView.setImageResource(R.drawable.ic_absent);

               }
               else {
                   studentStatusImageView.setImageResource(R.drawable.ic_holiday);
               }

            }
        });



    }

    private int countUniqueDates(List<String> dates) {
        Set<String> uniqueDates = new HashSet<>(dates);
        return uniqueDates.size();
    }

    private float calculatePercentage(int presentCount, int totalCount) {
        if (totalCount == 0) {
            return 0;
        }
        return ((float) presentCount / totalCount) * 100;
    }


    private void student_attendance_filter(String subject, String roll) {
        progressBar.setVisibility(View.VISIBLE);

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("subject", subject);
            studentObject.put("roll", roll);

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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSubjectDisplay.this);

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
                                Toast.makeText(StudentSubjectDisplay.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            // Handle JSON parsing error
                            Toast.makeText(StudentSubjectDisplay.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        System.out.println(error.toString());
                        Toast.makeText(StudentSubjectDisplay.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                String date = studentObject.getString("Date");
             //   String username = studentObject.getString("username");
             //   String Roll = studentObject.getString("Roll");
                String user_status = studentObject.getString("user_status");
                totalDays.add(date);


                if (user_status.equals("Present")){
                    presentDates.add(date);
                  //  Toast.makeText(this, user_status, Toast.LENGTH_SHORT).show();
                } else if (user_status.equals("Absent")) {

                    absentDates.add(date);


                }
                else {
                    Toast.makeText(this, "Your Attendance is not Valid.", Toast.LENGTH_SHORT).show();
                }


                //  all_subject.add(subject + "," + subject_code + "," + branch + "," + branch_code + "," + semester);
            }
            plot();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

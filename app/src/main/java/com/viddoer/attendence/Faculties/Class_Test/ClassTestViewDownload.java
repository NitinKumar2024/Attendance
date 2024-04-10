package com.viddoer.attendence.Faculties.Class_Test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.viddoer.attendence.Adapters.StudentListRankAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Faculties.AllAttendanceViewDownload;
import com.viddoer.attendence.Models.AdvancedAttendanceModel;
import com.viddoer.attendence.Models.ClassTestHelper;
import com.viddoer.attendence.Models.StudentListRankModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentFrontDashboard.AllStudentRankDisplayActivity;

import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ClassTestViewDownload extends AppCompatActivity {

    List<StudentListRankModel> contactList = new ArrayList<>();
    List<ClassTestHelper> contactList2 = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String branch_code, subject, subject_code, roll, complete_subject;
    // Define your PHP script URL
    private static final String PHP_SCRIPT_URL = ApiUrls.ClassTestViewDownload_url;
    FloatingActionButton floatingActionButton;

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
        floatingActionButton = findViewById(R.id.add_note);
        String best_subject = getIntent().getStringExtra("complete_subject");
        complete_subject = "1554422";


        progressBar.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(subject);
        floatingActionButton.setVisibility(View.VISIBLE);

        floatingActionButton.setOnClickListener(v -> {

            excel();

        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveAIStudents(complete_subject);
    }

    private void excel(){
        progressBar.setVisibility(View.VISIBLE);
        // Create a new Workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a Sheet
            Sheet sheet = workbook.createSheet("Attendance");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Roll No");
            headerRow.createCell(3).setCellValue("ClassTest1");
            headerRow.createCell(4).setCellValue("ClassTest2");
            headerRow.createCell(5).setCellValue("ClassTest3");
            headerRow.createCell(6).setCellValue("ClassTest4");

            // Populate data
            int rowNum = 1;
            for (ClassTestHelper attendance : contactList2) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(attendance.getDate());
                row.createCell(1).setCellValue(attendance.getName());
                row.createCell(2).setCellValue(attendance.getRegistration());
                row.createCell(3).setCellValue(attendance.getClassTest1());
                row.createCell(4).setCellValue(attendance.getClassTest2());
                row.createCell(5).setCellValue(attendance.getClassTest3());
                row.createCell(6).setCellValue(attendance.getClassTest4());
            }
            // Directory where the PDF will be saved
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EduMark/EXCEL");
            pdfDir.mkdirs(); // Ensure the directory exists

            // File path for the PDF
            // Generate a unique filename based on the current timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            String pdfFileName = subject + "class_test_" + timestamp + ".xlsx";
            File pdfFile = new File(pdfDir, pdfFileName);
            String pdfPath = pdfFile.getAbsolutePath();

            // Write the workbook content to an OutputStream
            try (FileOutputStream fileOut = new FileOutputStream(pdfFile)) {
                workbook.write(fileOut);
                progressBar.setVisibility(View.GONE);

                // Open the PDF with an intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(ClassTestViewDownload.this, getPackageName() + ".fileprovider", pdfFile);
                intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
//
                Toast.makeText(this, "Successfully Created", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void retrieveAIStudents(String subject) {

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("subject", subject);



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
                                TextView textView = findViewById(R.id.textViewTitle);
                             //   textView.setText(response);
                                Toast.makeText(ClassTestViewDownload.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                StudentListRankAdapter adapter = new StudentListRankAdapter(getApplicationContext(), contactList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Handle the case where the response is empty
                                Toast.makeText(ClassTestViewDownload.this, "Empty response received", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(ClassTestViewDownload.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        Toast.makeText(ClassTestViewDownload.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                String username = studentObject.getString("Name");
                String rollNo = studentObject.getString("Reg");
                String Date = studentObject.getString("Date");
                String ClassTest1 = studentObject.getString("ClassTest1");
                String ClassTest2 = studentObject.getString("ClassTest2");
                String ClassTest3 = studentObject.getString("ClassTest3");
                String ClassTest4 = studentObject.getString("ClassTest4");





                contactList.add(new StudentListRankModel(ClassTest1, username, "Rank: " + String.valueOf(i + 1), 22));
                contactList2.add(new ClassTestHelper(Date, username, rollNo, ClassTest1, ClassTest2, ClassTest3, ClassTest4));




            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
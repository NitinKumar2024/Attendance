package com.viddoer.attendence.Faculties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.viddoer.attendence.Adapters.AdvanceAdapter;
import com.viddoer.attendence.Adapters.AttendenceAdapter;
import com.viddoer.attendence.Models.AdvancedAttendanceModel;
import com.viddoer.attendence.Models.AttendenceModel;
import com.viddoer.attendence.R;

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
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AllAttendanceViewDownload extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressBar progressBar;
    String subject;
    List<AdvancedAttendanceModel> contactList = new ArrayList<>();
    private static final String PHP_SCRIPT_URL = "https://viddoer.com/attendance/gpbarh/view_download.php";
    String PHP_SCRIPT_URL_Range = "https://viddoer.com/attendance/gpbarh/view_download_range.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attendance_view_download);


        recyclerView = findViewById(R.id.recyclerViewDownloadHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllAttendanceViewDownload.this));
        progressBar = findViewById(R.id.threeDotSpinner);
        getSupportActionBar().hide();
        FloatingActionButton floatingActionButton = findViewById(R.id.add_note);

        String singleDate = getIntent().getStringExtra("date");
        String pairDate1 = getIntent().getStringExtra("date1");
        String pairDate2 = getIntent().getStringExtra("date2");
        subject = getIntent().getStringExtra("subject");

        if (TextUtils.isEmpty(singleDate)){
            // Perform task which is fetch all range data between pairDate1 to pairDate2
            retrieveAIStudents_range(pairDate2, subject);

        } else if (TextUtils.isEmpty(pairDate1) && TextUtils.isEmpty(pairDate2)) {

            // Perform task which is fetch all particular date data
            retrieveAIStudents(subject, singleDate);



        }
//        // Assuming 'adapter' is defined outside the OnClickListener scope
//        final AdvanceAdapter adapter = new AdvanceAdapter(getApplicationContext(), contactList);
//        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Create a new PDF document
//                PdfDocument document = new PdfDocument();
//
//                // Create a page info with the page size and margins
//                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(995, 842, 1).create();
//
//                // Start a page
//                PdfDocument.Page page = document.startPage(pageInfo);
//
//                // Get the canvas of the page
//                Canvas canvas = page.getCanvas();
//
//                // Set up the paint for drawing
//                Paint paint = new Paint();
//                paint.setColor(Color.BLACK);
//                paint.setTextSize(12);
//
//                // Define the starting position for the content
//                int x = 50;
//                int y = 50;
//
//                // Iterate through the adapter data and draw each item on the page
//                for (int i = 0; i < recyclerView.getChildCount(); i++) {
//                    View itemView = recyclerView.getChildAt(i);
//                    recyclerView.getLayoutManager().measureChildWithMargins(itemView, 0, 0);
//                    int height = itemView.getMeasuredHeight();
//                    Bitmap bitmap = Bitmap.createBitmap(itemView.getWidth(), height, Bitmap.Config.ARGB_8888);
//                    Canvas itemCanvas = new Canvas(bitmap);
//                    itemView.draw(itemCanvas);
//                    if (y + height > pageInfo.getPageHeight() - 50) {
//                        document.finishPage(page); // Finish current page
//                        page = document.startPage(pageInfo); // Start a new page
//                        canvas = page.getCanvas(); // Get canvas of new page
//                        y = 50; // Reset y position
//                    }
//                    canvas.drawBitmap(bitmap, x, y, paint);
//                    y += height;
//                }
//
//                // Finish the page
//                document.finishPage(page);
//
//                // Save the document
//                try {
//                    // Directory where the PDF will be saved
//                    File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EduMark/PDF");
//                    pdfDir.mkdirs(); // Ensure the directory exists
//
//                    // File path for the PDF
//                    // Generate a unique filename based on the current timestamp
//                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//
//                    String pdfFileName = subject + timestamp + ".pdf";
//                    File pdfFile = new File(pdfDir, pdfFileName);
//                    String pdfPath = pdfFile.getAbsolutePath();
//
//                    // Write the document to the file
//                    document.writeTo(new FileOutputStream(pdfFile));
//
//                    // Open the PDF with an intent
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    Uri uri = FileProvider.getUriForFile(AllAttendanceViewDownload.this, getPackageName() + ".fileprovider", pdfFile);
//                    intent.setDataAndType(uri, "application/pdf");
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(intent);
//
//                    Toast.makeText(getApplicationContext(), "PDF created successfully!", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } finally {
//                    // Close the document
//                    document.close();
//                }
                excel();
            }
        });

    }


    private void retrieveAIStudents_range(String branch, String semester) {
        progressBar.setVisibility(View.VISIBLE);
        String singleDate = getIntent().getStringExtra("date");
        String pairDate1 = getIntent().getStringExtra("date1");
        String pairDate2 = getIntent().getStringExtra("date2");
        String subject = getIntent().getStringExtra("subject");

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("name", subject);
            studentObject.put("date1", pairDate1);
            studentObject.put("date2", pairDate2);


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
        RequestQueue requestQueue = Volley.newRequestQueue(AllAttendanceViewDownload.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL_Range,
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
                                Toast.makeText(AllAttendanceViewDownload.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                // Update RecyclerView adapter
                                AdvanceAdapter adapter = new AdvanceAdapter(getApplicationContext(), contactList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                // Handle the case where the response is empty
                                Toast.makeText(AllAttendanceViewDownload.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AllAttendanceViewDownload.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AllAttendanceViewDownload.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void retrieveAIStudents(String date, String subject_name) {
        progressBar.setVisibility(View.VISIBLE);
        String singleDate = getIntent().getStringExtra("date");
        String pairDate1 = getIntent().getStringExtra("date1");
        String pairDate2 = getIntent().getStringExtra("date2");
        String subject = getIntent().getStringExtra("subject");

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("name", subject);
            studentObject.put("date", singleDate);


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
            progressBar.setVisibility(View.GONE);
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(AllAttendanceViewDownload.this);

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
                                Toast.makeText(AllAttendanceViewDownload.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                // Update RecyclerView adapter
                                AdvanceAdapter adapter = new AdvanceAdapter(getApplicationContext(), contactList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Handle the case where the response is empty
                                Toast.makeText(AllAttendanceViewDownload.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            // Handle JSON parsing error
                            Toast.makeText(AllAttendanceViewDownload.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AllAttendanceViewDownload.this, error.toString(), Toast.LENGTH_SHORT).show();
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
            headerRow.createCell(3).setCellValue("Status");

            // Populate data
            int rowNum = 1;
            for (AdvancedAttendanceModel attendance : contactList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(attendance.getDate());
                row.createCell(1).setCellValue(attendance.getName());
                row.createCell(2).setCellValue(attendance.getRoll());
                row.createCell(3).setCellValue(attendance.getStatus());
            }
            // Directory where the PDF will be saved
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EduMark/PDF");
            pdfDir.mkdirs(); // Ensure the directory exists

            // File path for the PDF
            // Generate a unique filename based on the current timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            String pdfFileName = subject + timestamp + ".xlsx";
            File pdfFile = new File(pdfDir, pdfFileName);
            String pdfPath = pdfFile.getAbsolutePath();

            // Write the workbook content to an OutputStream
            try (FileOutputStream fileOut = new FileOutputStream(pdfFile)) {
                workbook.write(fileOut);
                progressBar.setVisibility(View.GONE);

                    // Open the PDF with an intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(AllAttendanceViewDownload.this, getPackageName() + ".fileprovider", pdfFile);
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

    private void parseJSONResponse(JSONArray response) {
        // Parse the JSON response and populate studentList
        try {
            // Create a Set to store unique dates
            Set<String> uniqueDates = new HashSet<>();

            for (int i = 0; i < response.length(); i++) {
                JSONObject studentObject = response.getJSONObject(i);
                String name = studentObject.getString("username");
                String rollNo = studentObject.getString("Roll");
                String status = studentObject.getString("user_status");
                String date = studentObject.getString("Date");

                // Check if the date is already encountered
                if (!uniqueDates.contains(date)) {
                    // Add the date to the set to mark it as encountered
                    uniqueDates.add(date);
                    contactList.add(new AdvancedAttendanceModel(name, rollNo, status, date));


                } else {
                    // Handle repetition of date here if needed
                    // For example, log a warning or skip adding to contactList
                    // Add the parsed data to the studentList
                    contactList.add(new AdvancedAttendanceModel(name, rollNo, status, null));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void retrieveAIStudents(String subject, String date) {
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Create a JSONObject to hold the parameters
//        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("subject", subject);
//            jsonParams.put("date", date);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Create a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(AllAttendanceViewDownload.this);
//
//        // Create a JsonObjectRequest to send POST request to the PHP script URL
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PHP_SCRIPT_URL, jsonParams,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Handle the JSON response from the PHP script
//                        parseJSONResponse(response);
//                        // Update RecyclerView adapter
//                        AdvanceAdapter adapter = new AdvanceAdapter(getApplicationContext(), contactList);
//                        recyclerView.setAdapter(adapter);
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors
//                        Log.e("Error", error.toString());
//                        // Example: Show error message
//                        Toast.makeText(AllAttendanceViewDownload.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//
//        // Add the request to the request queue
//        requestQueue.add(jsonObjectRequest);
//    }

//    private void parseJSONResponse(JSONObject response) {
//        // Parse the JSON response and populate studentList
//        try {
//            JSONArray jsonArray = response.getJSONArray("students");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject studentObject = jsonArray.getJSONObject(i);
//                String name = studentObject.getString("username");
//                String rollNo = studentObject.getString("Roll");
//                String status = studentObject.getString("user_status");
//                String date = studentObject.getString("Date");
//                // Add the parsed data to the studentList
//                contactList.add(new AdvancedAttendanceModel(name, rollNo, status, date));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
package com.viddoer.attendence.Faculties;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.viddoer.attendence.Adapters.AdvanceAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.FeedbackBottomSheet;
import com.viddoer.attendence.Models.AdvancedAttendanceModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class AllAttendanceViewDownload extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressBar progressBar;
    String subject, subject_name;
    List<AdvancedAttendanceModel> contactList = new ArrayList<>();
    private static final String PHP_SCRIPT_URL = ApiUrls.AllAttendanceViewDownload_PHP_SCRIPT_URL;
    String PHP_SCRIPT_URL_Range = ApiUrls.AllAttendanceViewDownload_HP_SCRIPT_URL_Range;
    String PHP_SCRIPT_URL_PDF = ApiUrls.AllAttendanceViewDownload_pdf_url;
    String singleDate, pairDate1, pairDate2;

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

        singleDate = getIntent().getStringExtra("date");
        pairDate1 = getIntent().getStringExtra("date1");
        pairDate2 = getIntent().getStringExtra("date2");
        subject = getIntent().getStringExtra("subject");
        subject_name = getIntent().getStringExtra("subject_name");

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
                chooseExportAlert();
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
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EduMark/EXCEL");
            pdfDir.mkdirs(); // Ensure the directory exists

            // File path for the PDF
            // Generate a unique filename based on the current timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            String pdfFileName = subject_name + timestamp + ".xlsx";
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
    private void feedback(){

        FeedbackBottomSheet bottomSheetFragment = new FeedbackBottomSheet();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    private void chooseExportAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_dialog_export_options, null);

        // Set the custom view to the dialog
        builder.setView(view);

        // Find views in the custom layout
        Button pdfButton = view.findViewById(R.id.btn_pdf);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button excelButton = view.findViewById(R.id.btn_excel);

        // Set onClickListeners for the buttons
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle PDF export
                // You can add your PDF export logic here
                if (singleDate!=null){
                    export_pdf_date(singleDate, subject_name);
                }

            }
        });

        excelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Excel export
                // You can add your Excel export logic here
                showAlert();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Provide Feedback!");
        builder.setMessage("Do You Want to Provide Feedback!");
        builder.setPositiveButton("Feedback", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button
                feedback();

                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excel();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void export_pdf_date(String date, String subject_name) {
        progressBar.setVisibility(View.VISIBLE);
        String singleDate = getIntent().getStringExtra("date");
        String subject = getIntent().getStringExtra("subject");

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("name", subject);
            studentObject.put("date", singleDate);
            studentObject.put("file", subject_name);

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
        RequestQueue requestQueue = Volley.newRequestQueue(AllAttendanceViewDownload.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL_PDF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                // Convert JSON response to JSONObject
                                JSONObject jsonResponse = new JSONObject(response);

                                // Get the file path from the response
                                String filePath = jsonResponse.getString("filePath");

                                // Directory where the PDF will be saved
                                File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EduMark/PDF");
                                pdfDir.mkdirs(); // Ensure the directory exists

                                // Create a file to save the PDF
                                String url = "https://viddoer.com/attendance/gpbarh/View&Delete/";
                                String fileName = url + filePath;
                                // Generate a unique filename based on the current timestamp
                                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

                                String pdfFileName = subject_name + timestamp + ".pdf";
                                File pdfFile = new File(pdfDir, pdfFileName);

                                // Download the PDF file from the server
                                downloadFile(fileName, pdfFile);

                                // Notify user about successful PDF export
                                Toast.makeText(AllAttendanceViewDownload.this, "PDF file exported successfully", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AllAttendanceViewDownload.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle the case where the response is empty
                            Toast.makeText(AllAttendanceViewDownload.this, "Empty response received", Toast.LENGTH_SHORT).show();
                        }


                        progressBar.setVisibility(View.GONE);
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

    private void downloadFile(String fileUrl, File outputFile) {
        // Use AsyncHttpClient to download the file
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(fileUrl, new FileAsyncHttpResponseHandler(outputFile) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // File download success
                // Notify user about successful PDF export
                Toast.makeText(AllAttendanceViewDownload.this, "PDF file exported successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                // File download failure
                Toast.makeText(AllAttendanceViewDownload.this, "Error downloading PDF file", Toast.LENGTH_SHORT).show();
            }
        });
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
}
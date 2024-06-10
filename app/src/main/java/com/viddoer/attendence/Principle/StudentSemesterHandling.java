package com.viddoer.attendence.Principle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.viddoer.attendence.Adapters.StudentAddAdapter;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Models.StudentAddModel;
import com.viddoer.attendence.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class StudentSemesterHandling extends AppCompatActivity {

    private static final int PICK_EXCEL_FILE = 123;
    AsyncHttpClient client;
    RecyclerView recyclerView;
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+";
    String semester;
    Button button;
    private List<StudentAddModel> studentList;
    String branch_code;
    ProgressBar progressBar;
    String url = ApiUrls.StudentSemesterHandling_url;
    private static final String PHP_SCRIPT_URL = ApiUrls.StudentSemesterHandling_PHP_SCRIPT_URL;
    private static final String PHP_SCRIPT_URL_delete = ApiUrls.StudentSemesterHandling_PHP_SCRIPT_URL_delete;
    // Define a method to generate a random password
    public static String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();


        // Add characters from different character sets to the password
        for (int i = 0; i < length; i++) {
            // Choose a random character set
            String charSet = getRandomCharSet(random);
            // Choose a random character from the selected character set
            int randomIndex = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(randomIndex);
            // Append the random character to the password
            password.append(randomChar);
        }

        return password.toString();
    }

    // Define a method to choose a random character set
    private static String getRandomCharSet(SecureRandom random) {
        int randomInt = random.nextInt(4); // 0: lowercase, 1: uppercase, 2: digits, 3: special characters
        switch (randomInt) {
            case 0:
                return LOWERCASE_CHARS;
            case 1:
                return UPPERCASE_CHARS;
            case 2:
                return DIGITS;
            default:
                return SPECIAL_CHARS;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_semester_handling);

        this.getSupportActionBar().hide();

        branch_code = getIntent().getStringExtra("branch");
        String who = getIntent().getStringExtra("who");


        String branch;
        if (branch_code!=null){
            branch = branch_code.substring(branch_code.length() - 2);
        }
        semester = getIntent().getStringExtra("semester");
        String action = getIntent().getStringExtra("action");
        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.threeDotSpinner);
        TextView textView = findViewById(R.id.semester);
        textView.setText("Semester " + semester);
        // Initialize your studentList with data
        studentList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentSemesterHandling.this));

        if (action.equals("upload")) {
            openFilePicker();


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   upload_php();
                }
            });
        }
        else if (action.equals("view")) {
            if (branch_code!=null){
                branch = branch_code.substring(branch_code.length() - 2);
                view_php(branch, semester);
            }



        }
        if (who!=null){

            openFilePicker();
            button.setVisibility(View.VISIBLE);
            button.setText("Upload Faculty");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // How to send studentlist in intent



                }
            });

        }
    }



    private void view_php(String branch, String semester) {
        progressBar.setVisibility(View.VISIBLE);
        String shared_name = "principal_login";

        SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSemesterHandling.this);

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
                                Toast.makeText(StudentSemesterHandling.this, "success", Toast.LENGTH_SHORT).show();

                                // If successful, parse the JSON array
                                parseJSONResponse(jsonArray);
                                // Pass the studentList to your RecyclerView adapter
                                StudentAddAdapter adapter = new StudentAddAdapter(studentList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                                button.setVisibility(View.VISIBLE);
                                button.setText("Delete");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlert(branch, semester);
                                    }
                                });
                            } else {
                                // Handle the case where the response is empty
                                Toast.makeText(StudentSemesterHandling.this, "Empty response received", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            Toast.makeText(StudentSemesterHandling.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        Toast.makeText(StudentSemesterHandling.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void showAlert(String branch, String semester) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Student Data");
        builder.setMessage("Are You Sure Want to Delete All Student Data!");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button
                deletePhp(branch, semester);
                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePhp(String branch, String semester) {

        progressBar.setVisibility(View.VISIBLE);

        String shared_name = "principal_login";
        SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSemesterHandling.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {
                            // Try to create a JSONArray from the response string
                            Toast.makeText(StudentSemesterHandling.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();


                            progressBar.setVisibility(View.GONE);

                        } else {
                            // Handle the case where the response is empty
                            Toast.makeText(StudentSemesterHandling.this, "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(StudentSemesterHandling.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                String name = studentObject.getString("username");
                String rollNo = studentObject.getString("Reg");
                String phone = studentObject.getString("number");
                String email = studentObject.getString("email");


                // Create a StudentAddModel object and add it to the list
                StudentAddModel student = new StudentAddModel(rollNo, name, phone, email);

                studentList.add(student);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void upload_php() {

        progressBar.setVisibility(View.VISIBLE);

        String shared_name = "principal_login";
        SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
        String college_code = sharedPreferencest.getString("college_code", null);

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();
        for (StudentAddModel student : studentList) {
            JSONObject studentObject = new JSONObject();
            try {

                String branch = student.getRegNo().substring(3, 5);
                int passwordLength = 8; // Set the desired length of the password
                String randomPassword = generateRandomPassword(passwordLength);
                studentObject.put("semester", semester);
                studentObject.put("branch", branch);
                studentObject.put("username", student.getName());
                studentObject.put("registration", student.getRegNo());
                studentObject.put("email", student.getEmail());
                studentObject.put("number", student.getPhone());
                studentObject.put("password", randomPassword);
                studentObject.put("college_code", college_code);
                jsonArray.put(studentObject);
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSemesterHandling.this);

        // Create a StringRequest to send POST data to the server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // You can parse response here if server sends any
                        Toast.makeText(StudentSemesterHandling.this, response, Toast.LENGTH_SHORT).show();

                        if (response.contains("success")) {
                            Toast.makeText(StudentSemesterHandling.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }

                        progressBar.setVisibility(View.GONE); // Dismiss the progress dialog after receiving response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error

                        finish();
                        Toast.makeText(StudentSemesterHandling.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Dismiss the progress dialog if there's an error
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Specify the MIME type for Excel files

        startActivityForResult(intent, PICK_EXCEL_FILE);
    }

// In your Fragment class



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri excelFileUri = data.getData();
                if (excelFileUri != null) {
                    uploadFileToFirebase(excelFileUri);
                    }
                } else {
                    // Handle case where URI is null
                    Toast.makeText(this, "Failed to get URI", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle case where data intent is null
                Toast.makeText(this, "Intent data is null", Toast.LENGTH_SHORT).show();
            }
        }


    private void uploadFileToFirebase(Uri fileUri) {
        // Get the file name from the URI
        progressBar.setVisibility(View.VISIBLE);
        String fileName = "gpbarh.xlsx"; // Change as needed
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to be uploaded
        StorageReference fileRef = storageRef.child(fileName);

        // Start the upload process
        UploadTask uploadTask = fileRef.putFile(fileUri);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // File upload success
                Toast.makeText(StudentSemesterHandling.this, "File uploaded in Local Database", Toast.LENGTH_SHORT).show();
                // Once uploaded, initiate download and processing
                downloadAndProcessFile(fileRef);
            } else {
                progressBar.setVisibility(View.GONE);
                // File upload failed
                Toast.makeText(StudentSemesterHandling.this, "Failed to upload file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadAndProcessFile(StorageReference fileRef) {
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            // Initiate file download and processing
            client_download(downloadUrl);
        }).addOnFailureListener(exception -> {
            progressBar.setVisibility(View.GONE);
            // Failed to get download URL
            Toast.makeText(StudentSemesterHandling.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void client_download(String url) {
        client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(StudentSemesterHandling.this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StudentSemesterHandling.this, "Failed to download Excel file", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (file != null) {
                    // Excel file is downloaded successfully, now read it
                    readExcelFile(file);
                } else {
                    Toast.makeText(StudentSemesterHandling.this, "Downloaded file is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readExcelFile(File file) {



        try {
            // Creating a Workbook from the file
            Workbook workbook = WorkbookFactory.create(file);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Get the iterator to iterate over rows
            Iterator<Row> rowIterator = sheet.iterator();

// Skip the first row
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip the first row
            }

// Iterate over remaining rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // Get the data from the specified columns
                String regNo = getCellValueAsString(row.getCell(0)); // Assuming regNo is in column C (index 2)
                String name = getCellValueAsString(row.getCell(1)); // Assuming name is in column D (index 3)
                String email = getCellValueAsString(row.getCell(2)); // Assuming email is in column F (index 5)
                String phone = getCellValueAsString(row.getCell(3)); // Assuming number is in column E (index 4)

                // Check for null or empty values before adding to the list
                if (!TextUtils.isEmpty(regNo) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone)) {
                    StudentAddModel student = new StudentAddModel(regNo, name, phone, email);
                    studentList.add(student);
                } else {
                    // Display toast message if any value is null or empty
                    Toast.makeText(this, "Null or Empty Value Exists", Toast.LENGTH_SHORT).show();
                }


            }


            // Close the workbook
            workbook.close();
//
            // Pass the studentList to your RecyclerView adapter
            StudentAddAdapter adapter = new StudentAddAdapter(studentList);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(StudentSemesterHandling.this, "Failed to read Excel file", Toast.LENGTH_SHORT).show();
        }
    }


    // Helper method to get cell value as string
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // Return empty string if cell is null
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if the cell contains a numeric value
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    // If it's a date cell, return the date string representation
                    return cell.getDateCellValue().toString();
                } else {
                    // If it's a numeric cell, format the numeric value and return as string
                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    return decimalFormat.format(cell.getNumericCellValue());
                }
                // Add cases for other cell types if needed
            default:
                // For other cell types, return the string representation
                return cell.toString();
        }
    }
}
package com.viddoer.attendence.Principle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Models.PrincipleUploadSubjectModel;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class PrincipleUploadSubject extends Fragment {
    ProgressBar progressBar;
    AsyncHttpClient client;
    private static final int PICK_EXCEL_FILE = 123;
    private List<PrincipleUploadSubjectModel> studentList;

    String url = ApiUrls.PrincipleUploadSubject_url;

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principle_upload_subject, container, false);


        // Rest of your fragment code...
        Button upload_subject = view.findViewById(R.id.subject);
        Button upload_faculty = view.findViewById(R.id.faculty);
        studentList = new ArrayList<>();
        progressBar = view.findViewById(R.id.threeDotSpinner);
        upload_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   upload_subject_php();
            }
        });

        upload_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
         //       startActivity(new Intent(getActivity(), PrincipleUploadSubjectAndFaculty.class));



            }
        });

        return view;
    }

    private void upload_subject_php(){
        progressBar.setVisibility(View.VISIBLE);

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();
        for (PrincipleUploadSubjectModel student : studentList) {
            JSONObject studentObject = new JSONObject();
            try {
                studentObject.put("subject", student.getSubject());
                studentObject.put("subject_code", student.getSubject_code());
                studentObject.put("branch", student.getBranch());
                studentObject.put("branch_code", student.getBranch_code());
                studentObject.put("semester", student.getSemester());
            ///    Toast.makeText(getActivity(), student.getSemester(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Create a StringRequest to send POST data to the server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // You can parse response here if server sends any
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                        if (response.contains("All Subject details inserted successfully")){
                            Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();


                        }

                        progressBar.setVisibility(View.GONE); // Dismiss the progress dialog after receiving response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());

                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
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
            }
        }
    }

    private void uploadFileToFirebase(Uri fileUri) {
        // Get the file name from the URI
        progressBar.setVisibility(View.VISIBLE);
        String fileName = "Subject.xlsx"; // Change as needed
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to be uploaded
        StorageReference fileRef = storageRef.child(fileName);

        // Start the upload process
        UploadTask uploadTask = fileRef.putFile(fileUri);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // File upload success
                Toast.makeText(getActivity(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                // Once uploaded, initiate download and processing
                downloadAndProcessFile(fileRef);
            } else {
                progressBar.setVisibility(View.GONE);
                // File upload failed
                Toast.makeText(getActivity(), "Failed to upload file", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void client_download(String url) {
        client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failed to download Excel file", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (file != null) {
                    // Excel file is downloaded successfully, now read it
                    readExcelFile(file);
                } else {
                    Toast.makeText(getActivity(), "Downloaded file is null", Toast.LENGTH_SHORT).show();
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
                String subject = getCellValueAsString(row.getCell(0)); // Assuming regNo is in column C (index 2)
                String subject_code = getCellValueAsString(row.getCell(1)); // Assuming name is in column D (index 3)
                String branch = getCellValueAsString(row.getCell(2)); // Assuming number is in column E (index 4)
                String branch_code = getCellValueAsString(row.getCell(3)); // Assuming email is in column F (index 5)
                String semester = getCellValueAsString(row.getCell(4)); // Assuming email is in column F (index 5)

                // Create a StudentAddModel object and add it to the list
                if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(subject_code) || TextUtils.isEmpty(branch) || TextUtils.isEmpty(branch_code) || TextUtils.isEmpty(semester)){
//                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    PrincipleUploadSubjectModel student = new PrincipleUploadSubjectModel(subject, subject_code, branch, branch_code, semester);
                    studentList.add(student);
                }

            }


            // Close the workbook
            workbook.close();

            progressBar.setVisibility(View.GONE);
            upload_subject_php();

            Toast.makeText(getActivity(), "Now Subject is Uploading in Database...", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Failed to read Excel file", Toast.LENGTH_SHORT).show();
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
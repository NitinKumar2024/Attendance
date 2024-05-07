package com.viddoer.attendence.Principle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.viddoer.attendence.Adapters.ExpandableListAdapter;
import com.viddoer.attendence.Models.StudentAddModel;
import com.viddoer.attendence.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PrincipleAddStudent extends Fragment {
    private static final int PICK_EXCEL_FILE = 123;
    ProgressDialog progressDialog;
    AsyncHttpClient client;
    RecyclerView recyclerView;
    private ExpandableListView expandableListView;
    String ai, mechanical, civil, robotics;
    private ExpandableListAdapter expandableListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principle_add_student, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");

        expandableListView = view.findViewById(R.id.expandableListView);

        ai = "AI & ML - 44";
        mechanical = "Mechanical(CAD/CAM) - 46";
        civil = "Civil(Construction) - 45";
        robotics = "Robotics - 43";
        List<String> branches = new ArrayList<>();
        branches.add(ai);
        branches.add(mechanical);
        branches.add(civil);
        branches.add(robotics);

        HashMap<String, List<String>> semesters = new HashMap<>();
        List<String> branch1Semesters = new ArrayList<>();
        branch1Semesters.add("1");
        branch1Semesters.add("2");
        branch1Semesters.add("3");
        branch1Semesters.add("4");
        branch1Semesters.add("5");
        branch1Semesters.add("6");
        semesters.put(ai, branch1Semesters);

        List<String> branch2Semesters = new ArrayList<>();
        branch2Semesters.add("1");
        branch2Semesters.add("2");
        branch2Semesters.add("3");
        branch2Semesters.add("4");
        branch2Semesters.add("5");
        branch2Semesters.add("6");
        semesters.put(mechanical, branch2Semesters);

        List<String> branch3Semesters = new ArrayList<>();
        branch3Semesters.add("1");
        branch3Semesters.add("2");
        branch3Semesters.add("3");
        branch3Semesters.add("4");
        branch3Semesters.add("5");
        branch3Semesters.add("6");
        semesters.put(civil, branch3Semesters);

        List<String> branch4Semesters = new ArrayList<>();
        branch4Semesters.add("1");
        branch4Semesters.add("2");
        branch4Semesters.add("3");
        branch4Semesters.add("4");
        branch4Semesters.add("5");
        branch4Semesters.add("6");
        semesters.put(robotics, branch4Semesters);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(getContext(), branches, semesters);
        expandableListView.setAdapter(expandableListAdapter);

//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        // Rest of your fragment code...
//        Button button = view.findViewById(R.id.upload_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFilePicker();
//            }
//        });

        return view;
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
                    // You can use the fileUri to access the selected file
                    String filePath = excelFileUri.getPath();
                    File file = new File(filePath);
                    readExcelFile(file);
//                    uploadFileToFirebase(excelFileUri);
                }
            }
        }
    }

    private void uploadFileToFirebase(Uri fileUri) {
        // Get the file name from the URI
        String fileName = "your_file_name.xlsx"; // Change as needed
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
            // Failed to get download URL
            Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void client_download(String url) {
        client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(getContext()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(getContext(), "Failed to download Excel file", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (file != null) {
                    // Excel file is downloaded successfully, now read it
                    readExcelFile(file);
                } else {
                    Toast.makeText(getContext(), "Downloaded file is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readExcelFile(File file) {
        List<StudentAddModel> studentList = new ArrayList<>();


        try {
            // Creating a Workbook from the file
            Workbook workbook = WorkbookFactory.create(file);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over rows
            for (Row row : sheet) {
                // Get the data from the specified columns
                String regNo = getCellValueAsString(row.getCell(1)); // Assuming regNo is in column C (index 2)
                String name = getCellValueAsString(row.getCell(2)); // Assuming name is in column D (index 3)
                String phone = getCellValueAsString(row.getCell(4)); // Assuming number is in column E (index 4)
                String email = getCellValueAsString(row.getCell(5)); // Assuming email is in column F (index 5)

                // Create a StudentAddModel object and add it to the list
                StudentAddModel student = new StudentAddModel(regNo, name, phone, email);
                studentList.add(student);
            }

            // Close the workbook
            workbook.close();
//
//            // Pass the studentList to your RecyclerView adapter
//            StudentAddAdapter adapter = new StudentAddAdapter(studentList);
//            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to read Excel file", Toast.LENGTH_SHORT).show();
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
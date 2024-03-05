package com.viddoer.attendence.Principle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class PrincipleUploadSubjectAndFaculty extends AppCompatActivity {

    ProgressBar progressBar;
    private List<PrincipleUploadSubjectModel> studentList;

    String url = "https://viddoer.com/attendance/gpbarh/upload_subject.php";
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principle_upload_subject_and_faculty);
        // Rest of your fragment code...

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
        RequestQueue requestQueue = Volley.newRequestQueue(PrincipleUploadSubjectAndFaculty.this);

        // Create a StringRequest to send POST data to the server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // You can parse response here if server sends any
                        Toast.makeText(PrincipleUploadSubjectAndFaculty.this, response, Toast.LENGTH_SHORT).show();

                        if (response.contains("All Subject details inserted successfully")){
                            Toast.makeText(PrincipleUploadSubjectAndFaculty.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();


                        }

                        progressBar.setVisibility(View.GONE); // Dismiss the progress dialog after receiving response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        System.out.println(error.toString());

                        Toast.makeText(PrincipleUploadSubjectAndFaculty.this, error.toString(), Toast.LENGTH_SHORT).show();
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

}
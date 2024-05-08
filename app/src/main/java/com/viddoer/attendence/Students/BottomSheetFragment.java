package com.viddoer.attendence.Students;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Faculties.AllAttendanceViewDownload;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private List<String> student_all_List;
    private String subject, subject_name;


    public void setStudentList(List<String> student_all_List, String subject, String subject_name) {
        this.student_all_List = student_all_List;
        this.subject = subject;
        this.subject_name = subject_name;
        // Handle the received list in your BottomSheetFragment
        // You can use this list to update your UI or perform any other actions
    }

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private TextView total_student_number, present_student_number, absent_student_number;
    String url = ApiUrls.BottomSheetFragment_url;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        total_student_number = view.findViewById(R.id.total_student);
        present_student_number = view.findViewById(R.id.present_student);
        absent_student_number = view.findViewById(R.id.absent_student);
        // Initialize your studentList with data
        studentList = new ArrayList<>();
        // Assuming you're sending data to http://example.com/submit_students.php


        int present_student = 0;
        int absent_student = 0;
        int total_student = 0;

        for (String all_links : student_all_List) {
            String[] parts = all_links.split(",");
            String first_name = parts[0].replaceAll("[\\['\"]", "");
            String second_roll = parts[1];
            String third_status = parts[2].replaceAll("[\\]'\"]", "");
            studentList.add(new Student(first_name, second_roll, third_status));
            if (third_status.equals("Present")){
                present_student++;
            }
            else {
                absent_student++;
            }
        }
        // Convert integers to strings before setting them to TextViews
        present_student_number.setText("Present: " + String.valueOf(present_student));
        absent_student_number.setText("Absent: " + String.valueOf(absent_student));
        total_student_number.setText("Total: " + String.valueOf(present_student + absent_student));


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize and set up the adapter
        studentAdapter = new StudentAdapter(getActivity(), studentList);
        recyclerView.setAdapter(studentAdapter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button = view.findViewById(R.id.buttonMarkAttendance);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();



                // Convert studentList to JSON Array
                JSONArray jsonArray = new JSONArray();
                for (Student student : studentList) {
                    JSONObject studentObject = new JSONObject();
                    try {

                        studentObject.put("roll_no", student.getRoll_no());
                        studentObject.put("status", student.getStatus());
                        studentObject.put("subject", subject);
                        jsonArray.put(studentObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                }

                // Create a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                // Create a StringRequest to send POST data to the server
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle response from server
                                // You can parse response here if server sends any
                             //   Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                                if (response.contains("Student details inserted successfully")){
                                    Toast.makeText(getActivity(), "Attendance Marked", Toast.LENGTH_SHORT).show();
                                    showAlert(subject);
                                }
                                else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss(); // Dismiss the progress dialog after receiving response
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                System.out.println(error.toString());
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss(); // Dismiss the progress dialog if there's an error
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
        });

        return view;
    }

    private void showAlert(String subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("View Attendance");
        builder.setMessage("Do You Want to View Your Attendance?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button
                Date currentDate = new Date();

                // Define a date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Format the date object using the specified format
                String formattedDate = dateFormat.format(currentDate);
                Intent intent = new Intent(getContext(), AllAttendanceViewDownload.class);
                intent.putExtra("date", formattedDate);
                intent.putExtra("subject", subject);
                intent.putExtra("subject_name", subject_name);
                startActivity(intent);
                getActivity().finish();

                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }




    }


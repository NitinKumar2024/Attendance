package com.viddoer.attendence.Faculties;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentSubjectDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentDeatailsBottomSheet extends BottomSheetDialogFragment {

    String number, email, complete_subject, name, roll, subject_name;
    ProgressDialog progressDialog;
    private static final String PHP_SCRIPT_URL = ApiUrls.StudentDeatailsBottomSheet_PHP_SCRIPT_URL;

    TextView user_name, Attendance_report_textView, call_textView, email_textView;

    public void setStudentList(String number, String email, String complete_subject, String name, String roll, String subject_name) {

        this.number = number;
        this.email = email;
        this.complete_subject = complete_subject;
        this.name = name;
        this.roll = roll;
        this.subject_name = subject_name;
        // Handle the received list in your BottomSheetFragment
        // You can use this list to update your UI or perform any other actions
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_deatails_bottom_sheet, container, false);
        // Initialize views and set up any necessary listeners here

        user_name = view.findViewById(R.id.textViewTitle);
        Attendance_report_textView = view.findViewById(R.id.report);
        call_textView = view.findViewById(R.id.text_call);
        email_textView = view.findViewById(R.id.text_email);

        progressDialog = new ProgressDialog(getContext());

        user_name.setText(name);

        call_textView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // You can directly request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                // Permission has already been granted
                // Proceed with the call
                makePhoneCall(number);
            }

        });

        email_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_mail_for_low_attendance(email, name, subject_name);
            }
        });
        Attendance_report_textView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentSubjectDisplay.class);
            intent.putExtra("complete_subject", complete_subject);
            intent.putExtra("roll", roll);
            intent.putExtra("subject", subject_name);
            startActivity(intent);
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
                makePhoneCall(number);
            } else {
                // Permission denied
                // You can show a message or take appropriate action here
            }
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void send_mail_for_low_attendance(String email, String username, String subject) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("username", username);
            studentObject.put("subject", subject);

            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {
                            // Try to create a JSONArray from the response string

                              Toast.makeText(getContext(), "Email Send Successfully.", Toast.LENGTH_SHORT).show();


                            progressDialog.dismiss();
                        } else {
                            // Handle the case where the response is empty
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

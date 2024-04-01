package com.viddoer.attendence.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentSubjectDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText email;
    ProgressDialog progressDialog;
    private static final String PHP_SCRIPT_URL = ApiUrls.ForgotPassword_PHP_SCRIPT_URL;
    private static final String PHP_SCRIPT_URL2 =ApiUrls.ForgotPassword_PHP_SCRIPT_URL2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email_inputEdit);
        this.getSupportActionBar().hide();
        Button button = findViewById(R.id.submitButton);
        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("Sending mail...");

        String value = getIntent().getStringExtra("authentication");

        if (value.equals("principle")){


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    String emailAddres = email.getText().toString();
                    if (emailAddres.isEmpty()){
                        email.setError("Error");
                        progressDialog.dismiss();

                    }
                    else {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddres)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Password reset email sent successfully
                                            progressDialog.dismiss();
                                            Toast.makeText(ForgotPassword.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Password reset email sending failed
                                            progressDialog.dismiss();
                                            Toast.makeText(ForgotPassword.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }}
            });

        } else if (value.equals("student")) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emails = email.getText().toString();
                    if (emails!=null){

                        student_forgot_password(emails, "Student");
                    }
                    else {
                        Toast.makeText(ForgotPassword.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                    }
                }
            });





        } else if (value.equals("faculty")) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emails = email.getText().toString();
                    if (emails!=null){

                        faculty_forgot_password(emails, "Faculty");
                    }
                    else {
                        Toast.makeText(ForgotPassword.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }

    private void student_forgot_password(String email, String username) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("username", username);

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
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {

                            if (response.equals("Email sent successfully.")){

                                Toast.makeText(ForgotPassword.this, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this, "Email Not Exist", Toast.LENGTH_SHORT).show();
                            }


                            progressDialog.dismiss();
                        } else {
                            // Handle the case where the response is empty
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    private void faculty_forgot_password(String email, String username) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("username", username);

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
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Trim the response string to remove any leading/trailing whitespaces
                        response = response.trim();

                        // Check if the response is not null and not empty
                        if (!TextUtils.isEmpty(response)) {

                            if (response.equals("Email sent successfully.")){

                                Toast.makeText(ForgotPassword.this, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this, "Email Not Exist", Toast.LENGTH_SHORT).show();
                            }


                            progressDialog.dismiss();
                        } else {
                            // Handle the case where the response is empty
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
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
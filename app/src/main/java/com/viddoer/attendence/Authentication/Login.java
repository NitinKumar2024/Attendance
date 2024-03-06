package com.viddoer.attendence.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.viddoer.attendence.Adapters.AdvanceAdapter;
import com.viddoer.attendence.Faculties.AllAttendanceViewDownload;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.Models.AdvancedAttendanceModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.StudentDashBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {
    private TextInputEditText passwordEditText, email;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private static final String PHP_SCRIPT_URL = "https://viddoer.com/attendance/gpbarh/student_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        passwordEditText = findViewById(R.id.passwordEditText);
        email = findViewById(R.id.email_inputEdit);
        Button button = findViewById(R.id.submitButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = findViewById(R.id.forgotlog);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                intent.putExtra("authentication", "student");
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emails = email.getText().toString().trim();
                String passwords = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords)){
                    Toast.makeText(Login.this, "Fill all field", Toast.LENGTH_SHORT).show();

                }

                else {
                    php_login(emails, passwords);
                    progressDialog.show();
                }

            }
        });
    }




    private void php_login(String email, String password) {

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("password", password);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        try {
                            // Trim the response string to remove any leading/trailing whitespaces
                            response = response.trim();

                            // Check if the response contains error message
                            if (response.contains("Login not successful")) {
                                Toast.makeText(Login.this, "Login not successful. User not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                // If successful, parse the JSON array
                                JSONArray jsonArray = new JSONArray(response);
                                parseJSONResponse(jsonArray);
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // Handle JSON parsing error
                            Toast.makeText(Login.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                String name = studentObject.getString("Name");
                String semester = studentObject.getString("semester");
                String Registration = studentObject.getString("Reg");
                String Email = studentObject.getString("Email");
                String Phone = studentObject.getString("Phone");
                String password = studentObject.getString("password");
              //  Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, StudentDashBoard.class);
                intent.putExtra("name", name);
                intent.putExtra("email", Email);
                intent.putExtra("semester", semester);
                intent.putExtra("registration", Registration);
                intent.putExtra("phone", Phone);
                intent.putExtra("password", password);
                SharedPreferences sharedPreferences = getSharedPreferences("student_login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", Email);
                editor.putString("password", password);
                editor.putString("name", name);
                editor.putString("semester",semester);
                editor.putString("registration", Registration);
                editor.putString("phone", Phone);
                editor.apply();

                startActivity(intent);
                finish();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
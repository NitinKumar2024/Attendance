package com.viddoer.attendence.Authentication;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.Principle.PrincipleDashboard;
import com.viddoer.attendence.Principle.PrincipleFrontDashboard.PricipalFrontActivity;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TeacherLogin extends AppCompatActivity {

    private TextInputEditText passwordEditText, number;

    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    String shared_name;
    private static final String PHP_SCRIPT_URL = ApiUrls.TeacherLogin_PHP_SCRIPT_URL;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        auth = FirebaseAuth.getInstance();


        passwordEditText = findViewById(R.id.passwordEditText);
        number = findViewById(R.id.email_inputEdit);
        Button button = findViewById(R.id.submitButton);

        this.getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging...");


        TextView textView = findViewById(R.id.forgotlog);



        String value = getIntent().getStringExtra("role");
        if (value.equals("principle")){

            shared_name = "principal_login";

            SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
            String Emails = sharedPreferencest.getString("email", null);
            String Phones = sharedPreferencest.getString("number", null);
            String names = sharedPreferencest.getString("username", null);

            if (Emails!=null){
                Intent intent = new Intent(TeacherLogin.this, PricipalFrontActivity.class);
                intent.putExtra("username", names);
                intent.putExtra("email", Emails);
                intent.putExtra("number", Phones);
                startActivity(intent);
                finish();

            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherLogin.this, ForgotPassword.class);
                    intent.putExtra("authentication", "principle");
                    startActivity(intent);
                }
            });

        }
        else if (value.equals("faculty")){

            shared_name = "teacher_login";
            SharedPreferences sharedPreferencest = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
            String Emails = sharedPreferencest.getString("email", null);
            String Phones = sharedPreferencest.getString("number", null);
            String names = sharedPreferencest.getString("username", null);

            if (Emails!=null){
                Intent intent = new Intent(TeacherLogin.this, MainActivity.class);
                intent.putExtra("username", names);
                intent.putExtra("email", Emails);
                intent.putExtra("number", Phones);
                startActivity(intent);
                finish();

            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherLogin.this, ForgotPassword.class);
                    intent.putExtra("authentication", "faculty");
                    startActivity(intent);
                }
            });
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numbers = number.getText().toString().trim();
                String passwords = passwordEditText.getText().toString().trim();


                if (TextUtils.isEmpty(numbers) && TextUtils.isEmpty(passwords)){
                    Toast.makeText(TeacherLogin.this, "Fill all field", Toast.LENGTH_SHORT).show();
                }

                else {

                    String value = getIntent().getStringExtra("role");
                    if (Objects.equals(value, "faculty")){
                        php_login(numbers, passwords, "Faculty");
                    } else if (Objects.equals(value, "principle")) {

                        php_login(numbers, passwords, "principal");

                    }
                    else {
                        Toast.makeText(TeacherLogin.this, "Error", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
    }

    private void php_login(String email, String password, String role) {
        progressDialog.show();


        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("password", password);
            studentObject.put("role", role);


            jsonArray.put(studentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }


        // Create a JSON object to hold the student list
        JSONObject jsonObject = new JSONObject();
        //   String name_s = "Mathematics";
        try {
            jsonObject.put("students", jsonArray);


            //  jsonObject.put("subject", name_s);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherLogin.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_SCRIPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        // Handle response from server
                        try {
                            // Trim the response string to remove any leading/trailing whitespaces
                            response = response.trim();

                            // Check if the response contains error message
                            if (response.contains("Login not successful")) {
                                Toast.makeText(TeacherLogin.this, "Login not successful. User not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                // If successful, parse the JSON array
                                JSONArray jsonArray = new JSONArray(response);
                                parseJSONResponse(jsonArray);
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // Handle JSON parsing error
                            Toast.makeText(TeacherLogin.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        Toast.makeText(TeacherLogin.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                String Email = studentObject.getString("email");
                String Phone = studentObject.getString("number");
                String college_code = studentObject.getString("college_code");
                String unique_token = studentObject.getString("unique_token");

                //  Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

                if (name!=null){
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeacherLogin.this, MainActivity.class);

                    SharedPreferences sharedPreferences = getSharedPreferences(shared_name, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", Email);
                    editor.putString("number", Phone);
                    editor.putString("name", name);
                    editor.putString("college_code", college_code);
                    editor.putString("unique_token", unique_token);
                    editor.apply();
                    String value = getIntent().getStringExtra("role");
                    if (value.equals("faculty")){
                        startActivity(intent);
                    }
                    else {

                        finish();
                    }

                } else if (name == null) {
                    Toast.makeText(this, "Invalid username and password", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                }



            }
        } catch (JSONException e) {
            Toast.makeText(this, "Invalid username and password", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void php_principal_login(String email1, String password1)
    {
        progressDialog.show();

        auth.signInWithEmailAndPassword(email1, password1)
                .addOnSuccessListener(TeacherLogin.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(TeacherLogin.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TeacherLogin.this, PrincipleDashboard.class));
                        finish();
                    }
                })
                .addOnFailureListener(TeacherLogin.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(TeacherLogin.this, "Login Failed. ", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
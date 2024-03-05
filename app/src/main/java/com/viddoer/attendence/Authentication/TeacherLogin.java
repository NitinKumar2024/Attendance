package com.viddoer.attendence.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.Principle.FacultyAdapter;
import com.viddoer.attendence.Principle.FacultyItem;
import com.viddoer.attendence.Principle.PrincipleDashboard;
import com.viddoer.attendence.R;
import com.viddoer.attendence.StudentDashBoard;
import com.viddoer.attendence.WhoAreYou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeacherLogin extends AppCompatActivity {

    private TextInputEditText passwordEditText, number;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    private static final String PHP_SCRIPT_URL = "https://viddoer.com/attendance/gpbarh/faculty_login.php";


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


        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Replace "your-database-url" with your Firebase Realtime Database URL
        databaseReference = firebaseDatabase.getReference();
        String value = getIntent().getStringExtra("role");
        if (value.equals("principle")){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherLogin.this, ForgotPassword.class);
                    intent.putExtra("authentication", "principle");
                    startActivity(intent);
                }
            });
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // User is already signed in, redirect to MainActivity
                startActivity(new Intent(TeacherLogin.this, PrincipleDashboard.class));
                finish();
            }
        }
        else if (value.equals("faculty")){
            SharedPreferences sharedPreferencest = getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
            String Emails = sharedPreferencest.getString("email", null);
            String passwords = sharedPreferencest.getString("password", null);
            String Phones = sharedPreferencest.getString("number", null);
            String names = sharedPreferencest.getString("username", null);
            String subject = sharedPreferencest.getString("subject", null);

            if (Emails!=null && passwords!=null){
                Intent intent = new Intent(TeacherLogin.this, MainActivity.class);
                intent.putExtra("username", names);
                intent.putExtra("email", Emails);
                intent.putExtra("subject", subject);
                intent.putExtra("number", Phones);
                intent.putExtra("password", passwords);
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
                        php_login(numbers, passwords);
                    } else if (Objects.equals(value, "principle")) {

                        login252(numbers, passwords);

                    }
                    else {
                        Toast.makeText(TeacherLogin.this, "Error", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
    }

    private void php_login(String email, String password) {
        progressDialog.show();


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
                String password = studentObject.getString("password");
                //  Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

                if (name!=null){
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeacherLogin.this, MainActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("email", Email);
                    intent.putExtra("number", Phone);
                    intent.putExtra("password", password);

                    SharedPreferences sharedPreferences = getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", Email);
                    editor.putString("password", password);
                    editor.putString("number", Phone);
                    editor.putString("name", name);
                    editor.apply();

                    startActivity(intent);
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
    private void login252(String email1, String password1)
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
//    private void retrieveData(String numbers, String passwords) {
//        Intent intent = new Intent(TeacherLogin.this, MainActivity.class);
//        startActivity(intent);
//
//        ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(TeacherLogin.this);
//        progressDialog.setIcon(R.drawable.barh_logo);
//        progressDialog.setMessage("Logging...");
//        progressDialog.show();
////        databaseReference.child("faculties").addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
////
////
////                for (DataSnapshot facultySnapshot : dataSnapshot.getChildren()) {
////                    String number = facultySnapshot.child("number").getValue(String.class);
////                    String password = facultySnapshot.child("password").getValue(String.class);
////
////                    if (Objects.equals(numbers, number) && Objects.equals(passwords, password)){
////
////                        Intent intent = new Intent(TeacherLogin.this, MainActivity.class);
////                        startActivity(intent);
////                        Toast.makeText(TeacherLogin.this, "Successfully Login", Toast.LENGTH_SHORT).show();
////                        finish();
////                        progressDialog.dismiss();
////
////                    }
////
////                progressDialog.dismiss();
////
////
////                }
////
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                // Handle error
////
////                progressDialog.dismiss();
////                Toast.makeText(TeacherLogin.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
////            }
////        });
//    }

}
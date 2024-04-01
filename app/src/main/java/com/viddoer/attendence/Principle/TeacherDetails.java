package com.viddoer.attendence.Principle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class TeacherDetails extends AppCompatActivity {

    private TextInputEditText nameEditText, numberEditText, emailEditText;
    private Button submitButton;

    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    // Define character sets
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+";
    String url = ApiUrls.TeacherDetails_url;
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
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
        this.getSupportActionBar().hide();
        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Replace "your-database-url" with your Firebase Realtime Database URL
        databaseReference = firebaseDatabase.getReference();


        nameEditText = findViewById(R.id.input_name_EditText);
        numberEditText = findViewById(R.id.number_EditText);
        emailEditText = findViewById(R.id.email_EditText);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating...");
        FloatingActionButton floatingActionButton = findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDetails.this, PrincipalAssignTeacher.class));
            }
        });




        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(number)){
                    progressDialog.dismiss();
                    Toast.makeText(TeacherDetails.this, "Enter All Field", Toast.LENGTH_SHORT).show();
                }
                else {



                    // Convert studentList to JSON Array
                    JSONArray jsonArray = new JSONArray();

                    JSONObject studentObject = new JSONObject();
                    int passwordLength = 8; // Set the desired length of the password
                    String randomPassword = generateRandomPassword(passwordLength);
                    try {
                        studentObject.put("username", name);
                        studentObject.put("number", number);
                        studentObject.put("email", email);
                        studentObject.put("password", randomPassword);

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
                    RequestQueue requestQueue = Volley.newRequestQueue(TeacherDetails.this);

                    // Create a StringRequest to send POST data to the server
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Handle response from server
                                    // You can parse response here if server sends any
                                    Toast.makeText(TeacherDetails.this, response, Toast.LENGTH_SHORT).show();

                                    if (response.contains("Email sent successfully.")) {
                                        progressDialog.dismiss();

                                        Toast.makeText(TeacherDetails.this, "Faculty Added Successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                    else {

                                          progressDialog.dismiss(); // Dismiss the progress dialog after receiving response
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                    System.out.println(error.toString());
                                    Toast.makeText(TeacherDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void saveData() {
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading...");
        progressBar.show();
        String name = nameEditText.getText().toString();
        String number = numberEditText.getText().toString();


        // Create a unique key for each entry
        String entryKey = databaseReference.push().getKey();

        // Create a map to store data
        Map<String, Object> facultyData = new HashMap<>();
        facultyData.put("name", name);
        facultyData.put("number", number);


        // Save data to Firebase
        databaseReference.child("faculties").child(entryKey).setValue(facultyData);
        progressBar.dismiss();
        Toast.makeText(this, "Faculty Added Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TeacherDetails.this, PrincipleDashboard.class);
        startActivity(intent);
        finish();
    }

    private void retrieveData() {
        databaseReference.child("faculties").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the dataSnapshot to retrieve each faculty's data
                for (DataSnapshot facultySnapshot : dataSnapshot.getChildren()) {
                    // Retrieve values from the snapshot
                    String name = facultySnapshot.child("name").getValue(String.class);
                    String subject = facultySnapshot.child("subject").getValue(String.class);
                    String number = facultySnapshot.child("number").getValue(String.class);


                    // Display or use the retrieved values as needed
                    // For example, set them to EditText fields
                    nameEditText.setText(name);

                    numberEditText.setText(number);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}
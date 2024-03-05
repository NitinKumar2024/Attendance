package com.viddoer.attendence.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.R;
import com.viddoer.attendence.StudentDashBoard;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private TextInputEditText passwordEditText, name, email, number;
    private FirebaseAuth firebaseAuth;
    String url = "https://viddoer.com/attendance/another/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        this.getSupportActionBar().hide();



        passwordEditText = findViewById(R.id.passwordEditText);
        name = findViewById(R.id.input_name_EditText);
        email = findViewById(R.id.email_inputEdit);

        number = findViewById(R.id.number_EditText);
        Button button = findViewById(R.id.submitButton);
        Button loginButton = findViewById(R.id.loginButton);
        Spinner subjectSpinner = findViewById(R.id.subject_spinner);
        Spinner branchSpinner = findViewById(R.id.branch_spinner);


        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    // Parse the input value and check if it's within the allowed range
                    int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source.subSequence(start, end).toString());
                    if (input <= 699) {
                        return null; // Accept the input
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                }

                // Reject the input
                return "";
            }
        };
        number.setFilters(new InputFilter[]{filter});
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item here
                String selectedSubject = parentView.getItemAtPosition(position).toString();
                // Do something with the selected subject
                branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Handle the selected item here
                        String branchSubject = parentView.getItemAtPosition(position).toString();
                        // Do something with the selected subject
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String emails = email.getText().toString().trim();
                                String passwords = passwordEditText.getText().toString().trim();
                                String names = name.getText().toString().trim();

                                String numbers = number.getText().toString().trim();
                                if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords) || TextUtils.isEmpty(names) || TextUtils.isEmpty(numbers)){
                                    Toast.makeText(SignUp.this, "Fill All Field", Toast.LENGTH_SHORT).show();

                                }
                                else {

                                   // php_api(names, emails, passwords);
                                    signUpUser(emails, passwords, names, numbers,selectedSubject, branchSubject );
                                }
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing when nothing is selected
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            // User is already signed in, redirect to MainActivity
//            startActivity(new Intent(SignUp.this, StudentDashBoard.class));
//            finish();
//        }


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emails = email.getText().toString().trim();
//                String passwords = passwordEditText.getText().toString().trim();
//                String names = name.getText().toString().trim();
//
//                String numbers = number.getText().toString().trim();
//                if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords) || TextUtils.isEmpty(names) || TextUtils.isEmpty(numbers)){
//                    Toast.makeText(SignUp.this, "Fill All Field", Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    signUpUser(emails, passwords, names, numbers);
//                }
//            }
//        });



    }

    private void php_api(String username, String email, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        // Create a HashMap to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
        params.put("table", "nitin");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(SignUp.this, response.trim(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            // Override the getParams() method to pass parameters in the request
            @Override
            protected Map<String, String> getParams() {
                return params;
            }


        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void signUpUser(String emails, String passwords, String names, String numbers, String branch, String semester) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

// Create a HashMap to hold the new data
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("number", "9341378627");

// Define the reference to the same location
        DatabaseReference registerDetailRef = database.getReference().child("Student Details").child(branch + "/" + semester +"/"+ names + " " + numbers);

// Update the data at the reference location
        registerDetailRef.updateChildren(userData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Handle success
                    // You can perform any necessary actions here
                    Intent intent = new Intent(SignUp.this, StudentDashBoard.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {
                    // Handle error
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Failed to save user details", Toast.LENGTH_SHORT).show();

                }
            }
        });

        firebaseAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            if (currentUser != null) {
                                String uid = currentUser.getUid();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("User Name", names);
                                userData.put("Branch Name", branch);
                                userData.put("Semester", semester);
                                userData.put("Roll_no", numbers);
                                userData.put("Email", emails);
                                userData.put("Password", passwords);

                                DatabaseReference registerDetailRef = database.getReference().child("Student Details").child(branch + "/" + semester +"/"+ names + " " + numbers);

                                registerDetailRef.setValue(userData, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            Intent intent = new Intent(SignUp.this, StudentDashBoard.class);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Current user is null", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Failed Registered: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
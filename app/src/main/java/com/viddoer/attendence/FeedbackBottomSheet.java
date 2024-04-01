package com.viddoer.attendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedbackBottomSheet extends BottomSheetDialogFragment {

    private EditText feedbackEditText;
    private Button submitButton;
    ProgressDialog progressDialog;
    private static final String PHP_SCRIPT_URL = ApiUrls.FeedbackBottomSheet_PHP_SCRIPT_URL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_bottom_sheet, container, false);
        // Initialize views and set up any necessary listeners here

        // Find views by their IDs
        feedbackEditText = view.findViewById(R.id.edit_text_feedback);
        submitButton = view.findViewById(R.id.button_submit);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sending...");
        SharedPreferences sharedPreferencest = getActivity().getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
        String Emails = sharedPreferencest.getString("email", null);
        String passwords = sharedPreferencest.getString("password", null);
        String Phones = sharedPreferencest.getString("number", null);
        String names = sharedPreferencest.getString("name", null);

        // Set OnClickListener to the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the feedback text from the EditText
                String feedback = feedbackEditText.getText().toString();

                // Check if feedback is empty
                if (feedback.trim().isEmpty()) {
                    // If feedback is empty, show a toast message
                    Toast.makeText(getContext(), "Please enter your feedback", Toast.LENGTH_SHORT).show();
                } else {
                    // If feedback is not empty, process the feedback (e.g., send it to a server, save it locally, etc.)
                    // For demonstration, we'll just display the feedback in a toast message
                    feedback_php(Emails, names, feedback);

                    // Optionally, you can clear the EditText after submitting feedback
                    feedbackEditText.getText().clear();
                }
            }
        });
        return view;
    }

    private void feedback_php(String email, String username, String feedback) {
        progressDialog.show();

        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("email", email);
            studentObject.put("username", username);
            studentObject.put("message", feedback);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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

                                Toast.makeText(getActivity(), "Thanks For Your Feedback", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }


                            progressDialog.dismiss();
                        } else {
                            // Handle the case where the response is empty
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Empty response received", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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

package com.viddoer.attendence.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.viddoer.attendence.Adapters.BranchAdapter;
import com.viddoer.attendence.AllSemester;
import com.viddoer.attendence.ApiUrls;
import com.viddoer.attendence.Authentication.TeacherLogin;
import com.viddoer.attendence.MainActivity;
import com.viddoer.attendence.Models.BranchModel;
import com.viddoer.attendence.R;
import com.viddoer.attendence.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String PHP_SCRIPT_URL = ApiUrls.HomeFragment_url;
    private List<BranchModel> branchList;

    private FragmentHomeBinding binding;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize branch data
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        branchList = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
        String college_code = sharedPreferences.getString("college_code", null);
        php_branch(college_code);
        // Create adapter and set it to GridView
        BranchAdapter adapter = new BranchAdapter(getActivity(), branchList);
        binding.gridView.setAdapter(adapter);




        return root;
    }

    private void php_branch(String college_code) {
        progressDialog.show();


        // Convert studentList to JSON Array
        JSONArray jsonArray = new JSONArray();

        JSONObject studentObject = new JSONObject();
        try {
            studentObject.put("college_code", college_code);


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                                Toast.makeText(getContext(), "Login not successful. User not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                // If successful, parse the JSON array
                                JSONArray jsonArray = new JSONArray(response);
                                parseJSONResponse(jsonArray);
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // Handle JSON parsing error
                            Toast.makeText(getContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                String college_code = studentObject.getString("college_code");
                String branch_name = studentObject.getString("branch_name");
                String branch_code = studentObject.getString("branch_code");
                String branch_description = studentObject.getString("branch_description");
                String branch_image = studentObject.getString("branch_image");
//                Toast.makeText(getContext(), branch_code, Toast.LENGTH_SHORT).show();

             //   branchList.add(new BranchModel("https://media.istockphoto.com/id/1452604857/photo/businessman-touching-the-brain-working-of-artificial-intelligence-automation-predictive.jpg?s=612x612&w=0&k=20&c=GkAOxzduJbUKpS2-LX_l6jSKtyhdKlnPMo2ito4xpR4=", "AI & ML", "Explore the world of Artificial Intelligence and Machine Learning.", "44"));
                branchList.add(new BranchModel(branch_image, branch_name, branch_description, branch_code));


            }

            // Create adapter and set it to GridView
            BranchAdapter adapter = new BranchAdapter(getActivity(), branchList);
            binding.gridView.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Invalid username and password", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
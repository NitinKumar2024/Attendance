package com.viddoer.attendence.Students;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.viddoer.attendence.R;


public class StudentProfileFragment extends Fragment {

    private String name, email, registration, phone, semester, password;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;



    public void setData(String name, String email, String registration, String phone, String semester, String password) {
        this.name = name;
        this.email = email;
        this.registration = registration;
        this.phone = phone;
        this.semester = semester;
        this.password = password;
    }
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        imageView = view.findViewById(R.id.imageView);
        TextView names = view.findViewById(R.id.textViewName);
        TextView registrations = view.findViewById(R.id.textViewRegNumber);
        TextView email_text = view.findViewById(R.id.email);
        TextView phone_text = view.findViewById(R.id.text_phone);

        names.setText("Name: " + name);
        registrations.setText("Roll NO: "+semester+"1"+registration);
        email_text.setText("Email: "+ email);
        phone_text.setText("Phone NO: " + phone);

        applyBouncingAnimation(names);
        applyBouncingAnimation(registrations);
        applyBouncingAnimation(email_text);
        applyBouncingAnimation(phone_text);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if permission is not granted
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // Permission is already granted, open the image picker
                    openImagePicker();
                }
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        String image_uri = sharedPreferences.getString("image", null);

// Check if image_uri is not null and imageView is properly initialized before setting the image URI
        if (image_uri != null && imageView != null) {
            // Set the image URI to the imageView
          //  setProfileImage(Uri.parse(image_uri));
        }







        return view;
    }

    private void setProfileImage(Uri uri) {
        try {
            // Set the image URI to the ImageView
            imageView.setImageURI(uri);
            // Save the selected image URI to SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image", uri.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions
        }}

            private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, open the image picker
                openImagePicker();
            } else {
                // Permission has been denied, show a message to the user
                Toast.makeText(getActivity(), "Permission denied, cannot open image picker", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri uri = data.getData();
            try {
                // Set the selected image to the ImageView
                imageView.setImageURI(uri);
                // Save the selected image URI to SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("image", uri.toString());
                editor.apply();
                // Provide feedback to the user
                Toast.makeText(getActivity(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions that may occur while setting the image
                Toast.makeText(getActivity(), "Failed to set profile picture", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void applyBouncingAnimation(final View view) {
        // Create ObjectAnimator to animate translationY property
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -100f, 0f);
        animator.setDuration(1000); // Duration of the animation in milliseconds
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Infinite repeat
        animator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation
        animator.start();
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
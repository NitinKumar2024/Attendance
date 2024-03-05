package com.viddoer.attendence.Principle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.viddoer.attendence.R;


public class EditProfileFragment extends BottomSheetDialogFragment {

    private String id;
    private String name;
    private String number;
    private String subject;
    private String password;

    private EditText editTextName;
    private EditText editTextSubject;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private Button buttonEditProfile;

    public static EditProfileFragment newInstance(String name, String number, String subject, String password, String id) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("number", number);
        args.putString("subject", subject);
        args.putString("password", password);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        // Add your edit controls and logic here

        id = getArguments().getString("id");
        name = getArguments().getString("name");
        number = getArguments().getString("number");
        subject = getArguments().getString("subject");
        password = getArguments().getString("password");

        // Find references to EditText fields
        editTextName = view.findViewById(R.id.editTextName);
        editTextSubject = view.findViewById(R.id.editTextSubject);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextName.setText(name);
        editTextPassword.setText(password);
        editTextSubject.setText(subject);
        editTextPhoneNumber.setText(number);

        // Find reference to the Edit Profile Button
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);

        // Set onClickListener for the Edit Profile Button
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event of the Edit Profile Button
                onEditProfileButtonClick();
            }
        });


        return view;
    }

    private void onEditProfileButtonClick() {
        // Access the values from the EditText fields
        String name = editTextName.getText().toString();
        String subject = editTextSubject.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String password = editTextPassword.getText().toString();

        // Perform actions with the retrieved values
        // (e.g., show them, save them, etc.)
    }
}
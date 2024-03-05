package com.viddoer.attendence.Students;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viddoer.attendence.R;


public class StudentProfileFragment extends Fragment {

    private String name, email, registration, phone, semester, password;

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

        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView names = view.findViewById(R.id.textView2);
        TextView registrations = view.findViewById(R.id.textView3);

        names.setText(name);
        registrations.setText(semester+"1"+registration);





        return view;
    }

}
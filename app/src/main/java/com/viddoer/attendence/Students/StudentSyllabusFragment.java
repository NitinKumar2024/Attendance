package com.viddoer.attendence.Students;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.viddoer.attendence.R;


public class StudentSyllabusFragment extends Fragment {
    private String name, email, registration, phone, semester, password;

    public void setData(String name, String email, String registration, String phone, String semester, String password) {
        this.name = name;
        this.email = email;
        this.registration = registration;
        this.phone = phone;
        this.semester = semester;
        this.password = password;
    }
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_syllabus, container, false);







        return view;
    }

}
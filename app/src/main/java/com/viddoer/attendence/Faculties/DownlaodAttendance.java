package com.viddoer.attendence.Faculties;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viddoer.attendence.R;


public class DownlaodAttendance extends Fragment {

    private String branch;
    private String semester;
    private String subject;

    public void setData(String branch, String semester, String subject) {
        this.branch = branch;
        this.semester = semester;
        this.subject = subject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downlaod_attendance, container, false);


        // Rest of your fragment code...

        return view;
    }
}
package com.viddoer.attendence.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.viddoer.attendence.R;
import com.viddoer.attendence.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferencest = getActivity().getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
        String Emails = sharedPreferencest.getString("email", null);
        String passwords = sharedPreferencest.getString("password", null);
        String Phones = sharedPreferencest.getString("number", null);
        String names = sharedPreferencest.getString("username", null);
        String subject = sharedPreferencest.getString("subject", null);

        TextView faculty_name = binding.textViewName;
        TextView faculty_number = binding.textViewPhoneNumber;
        TextView faculty_subject = binding.textViewSubject;
        TextView faculty_password = binding.textViewPassword;
        Button button = binding.buttonEditProfile;

        faculty_name.setText(names);
        faculty_number.setText(Phones);
        faculty_subject.setText(subject);


        return root;
    }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
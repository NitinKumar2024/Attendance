package com.viddoer.attendence.ui.dashboard;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentFrontDashboard.StudentFrontActivity;
import com.viddoer.attendence.WhoAreYou;
import com.viddoer.attendence.databinding.FragmentDashboardBinding;
import com.viddoer.attendence.databinding.FragmentStudentProfileBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private FragmentStudentProfileBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentStudentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferencest = getActivity().getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
        String Emails = sharedPreferencest.getString("email", null);
        String passwords = sharedPreferencest.getString("password", null);
        String Phones = sharedPreferencest.getString("number", null);
        String names = sharedPreferencest.getString("name", null);
        String subject = sharedPreferencest.getString("subject", null);

        TextView faculty_name = binding.textViewName;
        TextView faculty_number = binding.textPhone;
        TextView faculty_email = binding.email;
        TextView reg = binding.textViewRegNumber;

        reg.setVisibility(View.GONE);


        faculty_name.setText(names);
        faculty_number.setText("Phone: " + Phones);
        faculty_email.setText("Email: " + Emails);

        applyBouncingAnimation(faculty_email);
        applyBouncingAnimation(faculty_name);
        applyBouncingAnimation(faculty_number);

        binding.buttonLogOut.setVisibility(View.VISIBLE);

        binding.buttonLogOut.setOnClickListener(v -> {
            showAlert();
        });



        return root;
    }

    private void applyBouncingAnimation(final View view) {
        // Create ObjectAnimator to animate translationY property
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -100f, 0f);
        animator.setDuration(1000); // Duration of the animation in milliseconds
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Infinite repeat
        animator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation
        animator.start();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout!");
        builder.setMessage("Do You Want to Logout of Edu Mark!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("teacher_login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getActivity(), "Logout Successfully...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), WhoAreYou.class));
                getActivity().finish();


                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
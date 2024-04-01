package com.viddoer.attendence.ui.notifications;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.viddoer.attendence.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView percentageTextView;
    private ObjectAnimator progressAnimator;


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = binding.imageUser;
        progressBar = binding.progressBar;

        // Sample attendance percentage (replace with your actual logic)
        double attendancePercentage = 70.0;

        // Set the attendance percentage text
       // percentageTextView.setText(getString(R.string.attendance_percentage, attendancePercentage));

        // Calculate progress value (0 to 100)
        int progress = (int) Math.round(attendancePercentage);

        // Animate progress from 0 to the calculated value
        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
        progressAnimator.setDuration(3000); // Animation duration
        progressAnimator.start();




        return root;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
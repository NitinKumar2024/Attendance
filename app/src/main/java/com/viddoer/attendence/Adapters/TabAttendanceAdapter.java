package com.viddoer.attendence.Adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.viddoer.attendence.Faculties.DownlaodAttendance;
import com.viddoer.attendence.Faculties.RemarkAttendence;
import com.viddoer.attendence.Faculties.ViewAttendance;

public class TabAttendanceAdapter extends FragmentPagerAdapter {

    private String branch;
    private String semester;
    private String subject, subject_code;
    public TabAttendanceAdapter(FragmentManager fm, String branch, String semester, String subject, String subject_code) {
        super(fm);
        this.branch = branch;
        this.semester = semester;
        this.subject = subject;
        this.subject_code = subject_code;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ViewAttendance viewFragment = new ViewAttendance();
                viewFragment.setData(branch, semester, subject, subject_code);
                return viewFragment;
            case 1:
                RemarkAttendence remarkFragment = new RemarkAttendence();
                remarkFragment.setData(branch, semester, subject,subject_code);
                return remarkFragment;
            case 2:
                DownlaodAttendance downloadFragment = new DownlaodAttendance();
                downloadFragment.setData(branch, semester, subject);
                return downloadFragment;
            default:
                // If position does not match any case, return the middle fragment
                RemarkAttendence defaultFragment = new RemarkAttendence();
                defaultFragment.setData(branch, semester, subject, subject_code);
                return defaultFragment;
        }


    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("TabAttendanceAdapter", "instantiateItem called for position: " + position);
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==1){
            title = "Remark Attendance";
        }
        if (position==0){
            title = "View Attendance";
        }
        if (position==2){
            title = "Download Data";
        }

        return title;
    }
}

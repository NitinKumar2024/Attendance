package com.viddoer.attendence.Adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.viddoer.attendence.Students.StudentDashboardFragment;
import com.viddoer.attendence.Students.StudentProfileFragment;
import com.viddoer.attendence.Students.StudentSyllabusFragment;

public class StudentFragmentAdapter extends FragmentPagerAdapter {

    private String name, email, registration, phone, semester, password;

    public StudentFragmentAdapter(FragmentManager fm, String name, String email, String registration, String phone, String semester, String password) {
        super(fm);
        this.name = name;
        this.email = email;
        this.registration = registration;
        this.phone = phone;
        this.semester = semester;
        this.password = password;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                StudentProfileFragment studentProfileFragment = new StudentProfileFragment();
                studentProfileFragment.setData(name, email, registration, phone, semester, password);
                return studentProfileFragment;
            case 1:
                StudentDashboardFragment studentDashboardFragment = new StudentDashboardFragment();
                studentDashboardFragment.setData(name, email, registration, phone, semester, password);
                return  studentDashboardFragment;
            case 2:
                StudentSyllabusFragment studentSyllabusFragment = new StudentSyllabusFragment();
                studentSyllabusFragment.setData(name, email, registration, phone, semester, password);
                return studentSyllabusFragment;
            default:
                return new StudentDashboardFragment();
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
        if (position==0){
            title = "Profile";
        }
        if (position==1){
            title = "Dashboard";
        }
        if (position==2){
            title = "Syllabus";
        }

        return title;
    }
}

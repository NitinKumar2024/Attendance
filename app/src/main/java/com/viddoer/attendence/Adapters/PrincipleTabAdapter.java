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
import com.viddoer.attendence.Principle.PrincipleAddFaculty;
import com.viddoer.attendence.Principle.PrincipleAddStudent;
import com.viddoer.attendence.Principle.PrincipleUploadSubject;
import com.viddoer.attendence.databinding.FragmentPrincipleAddStudentBinding;

public class PrincipleTabAdapter extends FragmentPagerAdapter {

    public PrincipleTabAdapter(FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PrincipleAddFaculty viewFragment = new PrincipleAddFaculty();

                return viewFragment;
            case 1:
                PrincipleAddStudent remarkFragment = new PrincipleAddStudent();

                return remarkFragment;

            case 2:
                PrincipleUploadSubject principleUploadSubject = new PrincipleUploadSubject();
                return principleUploadSubject;
            default:
                // If position does not match any case, return the middle fragment
                PrincipleAddFaculty defaultFragment = new PrincipleAddFaculty();
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
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==1){
            title = "Student";
        }
        if (position==0){
            title = "Faculty";
        }
        if (position==2){
            title = "Upload";
        }

        return title;
    }
}

package com.viddoer.attendence.Principle.All_Fragements;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.viddoer.attendence.Adapters.PrincipleTabAdapter;
import com.viddoer.attendence.R;


public class PrincipleHomeFragement extends Fragment {

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_principle_home_fragement, container, false);

         ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.setAdapter(new PrincipleTabAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        // Set the current tab to the second position (Remark Attendance)
        viewPager.setCurrentItem(0); // Index 1 corresponds to the second tab


        return view;
    }
}
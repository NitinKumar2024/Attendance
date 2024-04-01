package com.viddoer.attendence.Principle;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.viddoer.attendence.Adapters.PrincipleTabAdapter;
import com.viddoer.attendence.R;

public class PrincipleDashboard extends AppCompatActivity {


    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principle_dashboard);
        this.getSupportActionBar().hide();
        // Lock the screen orientation to portrait

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new PrincipleTabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        // Set the current tab to the second position (Remark Attendance)
        viewPager.setCurrentItem(0); // Index 1 corresponds to the second tab

    }



}
package com.viddoer.attendence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AllSemester extends AppCompatActivity {
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_semester);

        String branch = getIntent().getStringExtra("branch");

        CardView cardView3 = findViewById(R.id.semester3);
        CardView cardView1 = findViewById(R.id.semester1);
        CardView cardView2 = findViewById(R.id.semester2);
        CardView cardView4 = findViewById(R.id.semester4);
        CardView cardView5 = findViewById(R.id.semester5);
        CardView cardView6 = findViewById(R.id.semester6);

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navigateToSubject("3", branch);
            }
        });
        cardView1.setOnClickListener(v -> {
            navigateToSubject("1", branch);
        });
        cardView2.setOnClickListener(v -> {
            navigateToSubject("2", branch);
        });
        cardView4.setOnClickListener(v -> {
            navigateToSubject("4", branch);
        });
        cardView5.setOnClickListener(v -> {
            navigateToSubject("5", branch);
        });
        cardView6.setOnClickListener(v -> {
            navigateToSubject("6", branch);
        });
    }

    private void navigateToSubject(String semester, String branch) {
        Intent intent = new Intent(this, SelectSubject.class);
        intent.putExtra("branch", branch);
        intent.putExtra("semester", semester);
        startActivity(intent);
    }

}
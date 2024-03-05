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
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "3");
                startActivity(intent);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "1");
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "2");
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "4");
                startActivity(intent);
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "5");
                startActivity(intent);
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSemester.this, SelectSubject.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semester", "6");
                startActivity(intent);
            }
        });
    }
}
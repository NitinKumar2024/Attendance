package com.viddoer.attendence.Faculties;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.viddoer.attendence.R;


public class ViewAttendance extends Fragment {

    private String branch;
    private String semester;
    private String subject, subject_code;


    public void setData(String branch, String semester, String subject, String subject_code) {
        this.branch = branch;
        this.semester = semester;
        this.subject = subject;
        this.subject_code = subject_code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);


        CalendarView single = view.findViewById(R.id.singleDateCalendarView);
        Button single_button = view.findViewById(R.id.buttonGetTableDataForDate);

        CalendarView pair1 = view.findViewById(R.id.startDateCalendarView);
        CalendarView pair2 = view.findViewById(R.id.endDateCalendarView);
        Button pair_button = view.findViewById(R.id.buttonGetTableDataForDateRange);

        single.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                single_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Store the selected date in a variable or pass it to the next activity
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                        Intent intent = new Intent(getActivity(), AllAttendanceViewDownload.class);
                        String full_subject = branch + subject_code;
                        intent.putExtra("date", selectedDate);
                        intent.putExtra("subject", subject_code);
                        intent.putExtra("subject_name", subject);
                        startActivity(intent);

                    }
                });

            }
        });

        pair1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Store the selected date in a variable or pass it to the next activity
                String pair_calender1 = year + "-" + (month + 1) + "-" + dayOfMonth;

                pair2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        // Store the selected date in a variable or pass it to the next activity
                        String pair_calender2 = year + "-" + (month + 1) + "-" + dayOfMonth;

                        pair_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AllAttendanceViewDownload.class);
                                intent.putExtra("date1", pair_calender1);
                                intent.putExtra("date2", pair_calender2);
                                intent.putExtra("subject", subject_code);
                                intent.putExtra("subject_name", subject);
                                startActivity(intent);


                            }
                        });
                    }
                });
            }
        });





        // Rest of your fragment code...

        return view;
    }



}
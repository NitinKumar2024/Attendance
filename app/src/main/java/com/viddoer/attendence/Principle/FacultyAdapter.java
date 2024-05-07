package com.viddoer.attendence.Principle;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viddoer.attendence.R;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {

    private List<FacultyItem> facultyList;
    private Context context;

    public FacultyAdapter(List<FacultyItem> facultyList, Context context) {
        this.facultyList = facultyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacultyItem facultyItem = facultyList.get(position);

        holder.textViewFacultyName.setText(facultyItem.getFacultyName());
        holder.textViewSubject.setText("GP BARH");



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerOptions.setAdapter(adapter);
        holder.spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item here
                String selectedSubject = parent.getItemAtPosition(position).toString();

                if (selectedSubject.equals("Call")){

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + facultyItem.getNumber()));

                    // Check if the CALL_PHONE permission is granted
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted, make the call
                        holder.itemView.getContext().startActivity(callIntent);
                    } else {
                        // Permission is not granted, request the permission
                        ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }

                } else if (selectedSubject.equals("Profile")) {

                    goAhead();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void goAhead() {

                Intent intent = new Intent(holder.itemView.getContext(), FacultyProfile.class);
                intent.putExtra("name", facultyItem.getFacultyName());
                intent.putExtra("email", facultyItem.getEmail());
                intent.putExtra("number", facultyItem.getNumber());

                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return facultyList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFacultyName;
        TextView textViewSubject;
        Spinner spinnerOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFacultyName = itemView.findViewById(R.id.textViewFacultyName);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            spinnerOptions = itemView.findViewById(R.id.spinnerOptions);
        }
    }
}

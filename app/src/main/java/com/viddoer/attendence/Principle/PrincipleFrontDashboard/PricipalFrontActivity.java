package com.viddoer.attendence.Principle.PrincipleFrontDashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viddoer.attendence.Principle.PrincipleDashboard;
import com.viddoer.attendence.R;
import com.viddoer.attendence.Students.StudentFrontDashboard.StudentFrontActivity;
import com.viddoer.attendence.WhoAreYou;

public class PricipalFrontActivity extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3, cardView4;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_front);

        this.getSupportActionBar().hide();

        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.class_test);
        cardView4 = findViewById(R.id.cardView4);
        textView1 = findViewById(R.id.textView1);

        cardView2.setVisibility(View.GONE);
        cardView3.setVisibility(View.GONE);
        textView1.setText("Manage College");
        cardView1.setOnClickListener(v -> {
            Intent intent = new Intent(PricipalFrontActivity.this, PrincipleDashboard.class);
            startActivity(intent);
        });
        cardView4.setOnClickListener(v -> {
            showAlert();
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout!");
        builder.setMessage("Do You Want to Logout of Edu Mark!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to be executed when the user clicks OK button

                SharedPreferences sharedPreferences = getSharedPreferences("principal_login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(PricipalFrontActivity.this, "Logout Successfully...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PricipalFrontActivity.this, WhoAreYou.class));
                finish();


                dialog.dismiss(); // Dismiss the dialog
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
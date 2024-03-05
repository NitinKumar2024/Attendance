package com.viddoer.attendence.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viddoer.attendence.Principle.StudentSemesterHandling;
import com.viddoer.attendence.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> branches;
    private HashMap<String, List<String>> semesters;

    public ExpandableListAdapter(Context context, List<String> branches, HashMap<String, List<String>> semesters) {
        this.context = context;
        this.branches = branches;
        this.semesters = semesters;
    }

    @Override
    public int getGroupCount() {
        return branches.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return semesters.get(branches.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return branches.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return semesters.get(branches.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String branch = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }

        TextView branchTextView = convertView.findViewById(android.R.id.text1);
        branchTextView.setText(branch);

        // Set gravity to center
        branchTextView.setGravity(Gravity.CENTER);

        // Apply custom styling to branch names
        applyBranchNameStyle(branchTextView, branch);
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String semester = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, null);

        TextView semesterTextView = convertView.findViewById(R.id.semesterTitle);
        semesterTextView.setText("Semester " + semester);

        LinearLayout buttonsLayout = convertView.findViewById(R.id.buttonsLayout);
        Button button1 = convertView.findViewById(R.id.button1);
        Button button2 = convertView.findViewById(R.id.button2);

        // You can add listeners or customize the buttons here
        // For example:
        // Get the branch name
        final String branchName = (String) getGroup(groupPosition);
        final String semesterName = semester;

        // Set OnClickListener for button1
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Toast with branch name and semester name
                Intent intent = new Intent(context, StudentSemesterHandling.class);
                intent.putExtra("semester", semesterName);
                intent.putExtra("branch", branchName);
                intent.putExtra("action", "upload");
                ((Activity) context).startActivity(intent);
                Toast.makeText(context, "Branch: " + branchName + ", Semester: " + semesterName, Toast.LENGTH_SHORT).show();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button2 click
                // Show Toast with branch name and semester name
                Intent intent = new Intent(context, StudentSemesterHandling.class);
                intent.putExtra("semester", semesterName);
                intent.putExtra("branch", branchName);
                intent.putExtra("action", "view");
                ((Activity) context).startActivity(intent);
                Toast.makeText(context, "Branch: " + branchName + ", Semester: " + semesterName, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    // Method to apply custom styling to branch names
    private void applyBranchNameStyle(TextView textView, String branchName) {
        // Check for specific branch names and apply styles accordingly

        textView.setTextColor(context.getResources().getColor(R.color.primary_color));
        textView.setTextSize(28);

        // Add more conditions as needed for additional branch names
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

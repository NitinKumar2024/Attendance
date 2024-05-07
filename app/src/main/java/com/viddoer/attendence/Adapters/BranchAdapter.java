package com.viddoer.attendence.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.viddoer.attendence.AllSemester;
import com.viddoer.attendence.Models.BranchModel;
import com.viddoer.attendence.R;

import java.util.List;
import java.util.Random;

public class BranchAdapter extends BaseAdapter {

    private Context context;
    private List<BranchModel> branchList;

    public BranchAdapter(Context context, List<BranchModel> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @Override
    public int getCount() {
        return branchList.size();
    }

    @Override
    public Object getItem(int position) {
        return branchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_branch, null);

            viewHolder = new ViewHolder();
            viewHolder.branchImage = view.findViewById(R.id.branch_image);
            viewHolder.branchName = view.findViewById(R.id.branch_name);
            viewHolder.branchDescription = view.findViewById(R.id.branch_description);
            viewHolder.linearLayout = view.findViewById(R.id.linearLayout);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BranchModel branch = branchList.get(position);

        // Use Picasso to load image from URL into ImageView
        Picasso.get().load(branch.getImageUrl()).into(viewHolder.branchImage);

        // Set branch name and description
        viewHolder.branchName.setText(branch.getName());
        viewHolder.branchDescription.setText(branch.getDescription());
        // Set random background color for linearLayout
        int color = getRandomColor();
        viewHolder.linearLayout.setBackgroundColor(color);

        // Set OnClickListener for linearLayout
        viewHolder.linearLayout.setOnClickListener(v -> navigateToSemester(branch.getBranch_code()));

        return view;
    }
    // Method to generate a random color
    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    private void navigateToSemester(String branchId) {
        Intent intent = new Intent(context, AllSemester.class); // Use context instead of getActivity()
        intent.putExtra("branch", branchId);
        context.startActivity(intent); // Start activity using context
    }
    private static class ViewHolder {
        ImageView branchImage;
        TextView branchName;
        TextView branchDescription;
        LinearLayout linearLayout;
    }
}


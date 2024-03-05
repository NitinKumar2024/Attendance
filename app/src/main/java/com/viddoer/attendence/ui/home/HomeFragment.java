package com.viddoer.attendence.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.viddoer.attendence.AllSemester;
import com.viddoer.attendence.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Picasso.get().load("https://media.istockphoto.com/id/1452604857/photo/businessman-touching-the-brain-working-of-artificial-intelligence-automation-predictive.jpg?s=612x612&w=0&k=20&c=GkAOxzduJbUKpS2-LX_l6jSKtyhdKlnPMo2ito4xpR4=").into(binding.aiMlImage);
        Picasso.get().load("https://t3.ftcdn.net/jpg/06/11/32/86/360_F_611328697_hzgqLXPQQz6gyJ2BYUqSYmMQ2rukhD5g.jpg").into(binding.mechanicalImage);
        Picasso.get().load("https://img.freepik.com/free-photo/civil-engineer-construction-worker-manager-holding-digital-tablet-blueprints-talking-planing-about-construction-site-cooperation-teamwork-concept_640221-156.jpg?size=626&ext=jpg&ga=GA1.1.1412446893.1704844800&semt=ais").into(binding.civilImage);
        Picasso.get().load("https://media.licdn.com/dms/image/D5612AQE03WW0tnU2wg/article-cover_image-shrink_600_2000/0/1675014214058?e=2147483647&v=beta&t=DWFkrwWKkhp_9r7joXXFXt7l7hclw3l8YZh2q9UWmWI").into(binding.roboticsImage);
        binding.branchAiMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSemester.class);
                intent.putExtra("branch", "44");
                startActivity(intent);
            }
        });
        binding.branchCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSemester.class);
                intent.putExtra("branch", "45");
                startActivity(intent);
            }
        });
        binding.branchRobotics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSemester.class);
                intent.putExtra("branch", "43");
                startActivity(intent);
            }
        });
        binding.branchMechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSemester.class);
                intent.putExtra("branch", "46");
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
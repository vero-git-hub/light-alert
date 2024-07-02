package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lightalert.databinding.FragmentDashboardBinding;
import com.example.lightalert.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Read and display JSON data
        try {
            JSONObject scheduleJson = FileUtil.loadJSONFromAsset(requireContext(), "schedule.json");
            if (scheduleJson != null) {
                String formattedJson = scheduleJson.toString(4); // 4 is the number of spaces for indentation
                dashboardViewModel.setText(formattedJson);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.hwbluetooth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class DataFragment extends Fragment {


    private View rootView;
    private TextView tv_detail_mac;
    private TextView tv_detail_rssi;
    private TextView tv_detail_content;
    private Button BackScan;


    public DataFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_data, container, false);
        NavController navController= Navigation.findNavController(rootView);
        Button BackScan=rootView.findViewById(R.id.BackScan);
        BackScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dataFragment_to_scanFragment);

            }
        });
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Safe Arguments Received
        DataFragmentArgs args = DataFragmentArgs.fromBundle(getArguments());
        tv_detail_mac.setText(args.getMac());
        tv_detail_rssi.setText(args.getRssi());
        tv_detail_content.setText(args.getContent());
    }






}
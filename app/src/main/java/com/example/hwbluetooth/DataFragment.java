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
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DataFragment extends Fragment {


    private Button BackScan;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_data, container, false);
        Bundle bundle = getArguments();

//        if(bundle != null)
//        {
//            String address = DataFragmentArgs.fromBundle(getArguments()).getAddress();
//            String rssi = DataFragmentArgs.fromBundle(getArguments()).getRssi();
//            String timestampNanos = DataFragmentArgs.fromBundle(getArguments()).getTimestampNanos();
//            String content = DataFragmentArgs.fromBundle(getArguments()).getContent();
//            TextView a1 = rootView.findViewById(R.id.textView_Address);
//            TextView a2 = rootView.findViewById(R.id.textView_Rssi);
//            TextView a3 = rootView.findViewById(R.id.textView_Timestamp);
//            TextView a4 = rootView.findViewById(R.id.textView_Content);
//            a1.setText(address);
//            a2.setText(rssi);
//            a3.setText(timestampNanos);
//            a4.setText(content);
//        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController= Navigation.findNavController(view);
        Button BackScan=view.findViewById(R.id.BackScan);
        BackScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dataFragment_to_scanFragment);

            }
        });
    }






}
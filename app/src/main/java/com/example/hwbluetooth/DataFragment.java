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
    private TextView datamac;
    private TextView datarssi;
    private TextView datacontent;
    private Button BackScan;


    public DataFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        DataFragmentArgs args = DataFragmentArgs.fromBundle(getArguments());
        datamac.setText(args.getMac());
        datarssi.setText(args.getRssi());
        datacontent.setText(args.getContent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_data, container, false);
        Button BackScan=rootView.findViewById(R.id.BackScan);
        datamac = rootView.findViewById(R.id.MacData);
        datarssi = rootView.findViewById(R.id.RssiData);
        datacontent = rootView.findViewById(R.id.ContentData);
        BackScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(rootView);
                navController.navigate(R.id.action_dataFragment_to_scanFragment);

            }
        });
        return rootView;
    }






}
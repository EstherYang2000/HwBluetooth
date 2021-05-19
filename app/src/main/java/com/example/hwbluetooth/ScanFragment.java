package com.example.hwbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements RecyclerAdapter.onButtonClickHandler {




    private Button ScanData;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private HashMap<String, BLEdevice> BluetoothHashMap= new HashMap<>();
    private RecyclerAdapter mResultAdapter;
    private Button buttonSCAN;
    private Button Scandata;
    private RecyclerView mRecyclerView;
    private boolean isScanning=false;
    private View view;

    final String permissions[] = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scan, container, false);

        // Initialize View
        buttonSCAN = view.findViewById(R.id.buttonSCAN);
        mRecyclerView = view.findViewById(R.id.bthdata);

        // Initialize RecyclerView
        mResultAdapter = new RecyclerAdapter(BluetoothHashMap, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mResultAdapter);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isGranted = isGranted(getActivity(), permissions);

        if (!isGranted) {
            // Access Permissions Dynamically
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }


        // Initialize Bluetooth Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If Bluetooth not open or not enabled, request bluetooth action
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);
        }
        if (bluetoothAdapter != null) {
            mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            Log.e("onViewCreated: ", "Bluetooth Adapter Not NULL");
        }


        // Start or Stop Scanning
        buttonSCAN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isScanning) {
                    stopScan();
                }
                else {
                    startScan();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopScan();
    }

    private final ScanCallback startScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            ScanRecord mScanRecord = result.getScanRecord();
            String address = device.getAddress();
            byte[] content = mScanRecord.getBytes();
            int mRssi = result.getRssi();

            String contentMessage = convertContent(content);

            // Use Hashmap to ensure mac address unique
            BluetoothHashMap.put(address, new BLEdevice(contentMessage, String.valueOf(mRssi)));
            // Update Adapter with notifyDataSetChanged()
            mResultAdapter.notifyDataSetChanged();
        }
    };

    // Implementation of Interface OnButtonClickHandler.onButtonClick()


    @Override
    public void onButtonClick(String key, BLEdevice bluetoothLE) {

        stopScan();

        NavController navController = Navigation.findNavController(this.view);

        // Implementation of Safe Arguments
        ScanFragmentDirections.ActionScanFragmentToDataFragment action =
                ScanFragmentDirections.actionScanFragmentToDataFragment("MAC: " + key
                        , "RSSI: " + bluetoothLE.getRSSI()
                        , bluetoothLE.getContent());

        // Navigating to specific fragment
        navController.navigate(action);
    }

    public boolean isGranted(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public void startScan() {
        isScanning = true;
        Log.d("onClicked", "start to scan");
        buttonSCAN.setText("Stop");
        mBluetoothLeScanner.startScan(startScanCallback);
    }

    public void stopScan() {
        isScanning = false;
        Log.d("onClicked", "stop to scan");
        buttonSCAN.setText("Scan");
        mBluetoothLeScanner.stopScan(startScanCallback);
    }

    public String convertContent(byte[] content) {
        String message = "";
        for (byte b : content) {
            message += String.format("%02x ", b);
        }
        return message;
    }


}
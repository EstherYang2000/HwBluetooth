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



    //--------------------------------------------------------------------------------------------------------------------------------------//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //VIEW 初始化
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);

        buttonSCAN = view.findViewById(R.id.buttonSCAN);
        mRecyclerView = view.findViewById(R.id.bthdata);
        mResultAdapter = new RecyclerAdapter(BluetoothHashMap, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mResultAdapter);

        return view;
    }
    //-----------------------------------------------------------------------------------------------


    final String permissionscode[] = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isaccpted = isaccpted(getActivity(), permissionscode);

        if (!isaccpted) {
            ActivityCompat.requestPermissions(getActivity(), permissionscode, 1);
        }


        // Initialize Bluetooth Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//判斷啟動BLUETOOTHSCANNER
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);
        }
        if (bluetoothAdapter != null) {
            mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            Log.e("onViewCreated: ", "Bluetooth Adapter Not NULL");
        }


///點擊啟動SCAN
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
//---------------------------------------------------------------------------------------------------------------------------//
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

//---------------------------------------------------------------------------------------------------------------------------
//回傳SCAN資撩
    private final ScanCallback startScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            ScanRecord mScanRecord = result.getScanRecord();
            String address = device.getAddress();
            byte[] content = mScanRecord.getBytes();
            int mRssi = result.getRssi();

            String contentMessage = byteToString(content);
            //利用HASHMAP加入資料
            BluetoothHashMap.put(address, new BLEdevice(contentMessage,String.valueOf(mRssi)));
            //Adapter改變值
            mResultAdapter.notifyDataSetChanged();
        }
    };
//判斷是否開啟權限
 public boolean isaccpted(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissionscode) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }
//start to scan
    public void startScan() {
        isScanning = true;
        Log.d("onClicked", "start to scan");
        buttonSCAN.setText("SCANOFF");
        mBluetoothLeScanner.startScan(startScanCallback);
    }
//stop scan
    public void stopScan() {
        isScanning = false;
        Log.d("onClicked", "stop to scan");
        buttonSCAN.setText("SCANON");
        mBluetoothLeScanner.stopScan(startScanCallback);
    }

    public static String byteToString(byte[] b) {
        int length = b.length;
        String data = new String();
        for (int i = 0; i < length; i++) {
            data += Integer.toHexString((b[i] >> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }

//點擊data按鈕回傳到datalist
    @Override
    public void onButtonClick(String key, String rssi, String Content) {
        stopScan();

        NavController navController = Navigation.findNavController(this.view);

        ScanFragmentDirections.ActionScanFragmentToDataFragment action =
                ScanFragmentDirections.actionScanFragmentToDataFragment("MAC: " + key
                        , "RSSI: " + rssi
                        , Content);
        navController.navigate(action);
    }
}
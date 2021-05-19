package com.example.hwbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import java.util.HashMap;

import static androidx.navigation.Navigation.*;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements RecyclerAdapter.onButtonClickHandler {




    private Button ScanData;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private HashMap<String, BLEdevice> BluetoothHashMap;
    private RecyclerAdapter mResultAdapter;
    private Button buttonSCAN;
    private Button Scandata;
    private RecyclerView mRecyclerView;
    private boolean isScanning=false;


    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        buttonSCAN = (Button) rootView.findViewById(R.id.buttonSCAN);
        Scandata=(Button)rootView.findViewById(R.id.data);
        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.bthdata);
        mResultAdapter=new RecyclerAdapter(BluetoothHashMap,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        // Inflate the layout for this fragment
        return rootView;
    }



    private static final int Permission_Code=666;

    private final static String[]permissionWeNeed=new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    private void setupPermission(){

        boolean isaccpeted=true;
        for(String permission:permissionWeNeed){
            isaccpeted&= ActivityCompat.checkSelfPermission(getActivity(),permission)== PackageManager.PERMISSION_GRANTED;
        }


        if(!isaccpeted){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(permissionWeNeed,Permission_Code);
            }else{
                Toast.makeText(getActivity(),"no permission",Toast.LENGTH_SHORT);

            }
        }else{
        }
        initBlutooth();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case Permission_Code:{
                boolean isaccpeted=true;
                for(String permission:permissionWeNeed){
                    isaccpeted&= ActivityCompat.checkSelfPermission(getActivity(),permission)== PackageManager.PERMISSION_GRANTED;
                }


                if(!isaccpeted){
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        requestPermissions(permissionWeNeed,Permission_Code);
                    }else{
                        Toast.makeText(getActivity(),"no permission",Toast.LENGTH_SHORT);

                    }
                }else{
                    initBlutooth();
               }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initBlutooth(){
        boolean success=false;
        if(bluetoothAdapter!=null&&bluetoothAdapter.isEnabled()){
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter!=null)
                mBluetoothLeScanner =mBluetoothAdapter.getBluetoothLeScanner();
            Toast.makeText(getActivity(),"Bluetooth scan started",Toast.LENGTH_SHORT).show();
            success=true;
            Intent enableBlutoothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBlutoothIntent);


        }
        if(!success) {
            mBluetoothLeScanner=bluetoothAdapter.getBluetoothLeScanner();
            Toast.makeText(getActivity(),"Bluetooth scan cannot be turned off.",Toast.LENGTH_SHORT).show();
            Intent enableBlutoothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBlutoothIntent);
//            finishAndRemoveTask();

        }
    }

    private final ScanCallback startScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            BluetoothDevice device = result.getDevice();
            ScanRecord mScanRecord = result.getScanRecord();
            String address = device.getAddress();
            byte[] content = mScanRecord.getBytes();
            int mRssi = result.getRssi();
            String mRssi1=Integer.toString(mRssi);
            String ContentMessage=ByteArrayToHexString(content);
            if(address==null||address.trim().length()==0){

                BluetoothHashMap.put(address,new BLEdevice(ContentMessage,mRssi1));
                //public void addbth(String mac,String rssi, String content)
                mResultAdapter.notifyDataSetChanged();
                return;
            }


        }

    };

    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

//    public void startScan(){
//        isScanning=true;
//        Log.d("onClicked","start to scan");
//        buttonSCAN.setText("SCAN OFF");
//        mBluetoothLeScanner.startScan(startScanCallback);
//    }
    public void stopScan(){
        isScanning=false;
        Log.d("onClicked","stop to scan");
        buttonSCAN.setText("SCAN ON");
        mBluetoothLeScanner.stopScan(startScanCallback);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        NavController navController= Navigation.findNavController(view);
//        Button Data=view.findViewById(R.id.data);
//        Data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_scanFragment_to_dataFragment);
//            }
//        });
//
//    }

    @Override
    public void onButtonClick(String key, BLEdevice blEdevice) {
        stopScan();
//        Navigation navController=findNavController(this.view);
//        ScanFragmentDirections.ActionScanFragmentToDataFragment action
//                = ScanFragmentDirections.actionScanFragmentToDataFragment("MAC:"+key,"RSSI:"+blEdevice.getRSSI(),"Content:"+blEdevice.getContent());
//        navController.navigate(action);

    }
}
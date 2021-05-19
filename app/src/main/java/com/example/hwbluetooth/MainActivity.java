package com.example.hwbluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnectButton=(Button)findViewById(R.id.connect);

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=MainActivity.this;
                Class destinationActivity=ScanPage.class;
                Intent startbluetoothIntent=new Intent(context,destinationActivity);
                startActivity(startbluetoothIntent);
            }
        });
    }
}
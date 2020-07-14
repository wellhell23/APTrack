package com.example.aptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EmissionHistory extends AppCompatActivity {

    Button check,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission_history);


        check=findViewById(R.id.check);
        history=findViewById(R.id.history);
        final Intent intent =getIntent();
        final String devicename=intent.getStringExtra("DEVICEADD");
        Log.d("emmisssion activiity","device address"+ devicename);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(EmissionHistory.this,Emission.class);
                intent1.putExtra("DEVICEADD",devicename);
                Log.d("emmisssion activiit","device address"+ devicename);
                startActivity(intent1);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(EmissionHistory.this,History.class);
                intent1.putExtra("DEVICEADD",devicename);
                startActivity(intent1);
            }
        });

    }
}

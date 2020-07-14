package com.example.aptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Emission extends AppCompatActivity {
    TextView viewemission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission);
        viewemission=findViewById(R.id.emission);
        Intent intent=getIntent();
        String devicename=intent.getStringExtra("DEVICEADD");
        Random r = new Random();
        String value = String.valueOf(r.nextInt(400 - 200) + 200);
        viewemission.setText(value);
        ConnectToDatabase connectToDatabase=new ConnectToDatabase(devicename);
        connectToDatabase.addvalue(value);
//        Log.d("emmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","device address"+ devicename);
//     Toast.makeText(getApplicationContext(),"deviceadd"+devicename,Toast.LENGTH_SHORT).show();
    }
}

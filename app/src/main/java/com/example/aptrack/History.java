package com.example.aptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent=getIntent();
        String devicename=intent.getStringExtra("DEVICEADD");
        ConnectToDatabase connectToDatabase=new ConnectToDatabase(devicename);
        textView=findViewById(R.id.textview);
        final String string1;
        connectToDatabase.retrieveData(new Firebacecallback() {
            @Override
            public void onCallback(String string) {
                Toast.makeText(getApplicationContext(),string+"deepanshu",Toast.LENGTH_SHORT).show();
                textView.setText(string);

            }
        });



    }
}

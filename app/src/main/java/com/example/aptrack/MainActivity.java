package com.example.aptrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button connect;
    Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect=findViewById(R.id.connect);
        help=findViewById(R.id.help);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectClass();
            }
        });
    }
    void connectClass()
    {
        Intent intent=new Intent(this,Connect.class);
        startActivity(intent);
    }
}

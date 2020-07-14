package com.example.aptrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class Connect extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    ListView listBoundedDevice;
    Intent enableBluetoothIntent;
    ArrayAdapter<String> arrayAdapter;
    int REQUEST_ENABLE_BT;
    String TAG="Connect";
    Set<BluetoothDevice> bt;
    BluetoothDevice[] btdevice;
    Sendreseve sendreseve;
    Intent intent;
    String devicename;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();



            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Intent intent1=new Intent(Connect.this,EmissionHistory.class);
                intent1.putExtra("DEVICEADD",devicename);

                startActivity(intent1);


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        listBoundedDevice=findViewById(R.id.listBoundedDevice);
        enableBluetoothIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        this.registerReceiver(mReceiver, filter);


        if(bluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth does not support on this device", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!bluetoothAdapter.isEnabled())
            {
                startActivityForResult(enableBluetoothIntent,REQUEST_ENABLE_BT);
                String[] deviceName=getpaireddevice();
                arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceName);
                listBoundedDevice.setAdapter(arrayAdapter);

            }
            if(bluetoothAdapter.isEnabled())
            {
                String[] deviceName=getpaireddevice();
                arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceName);
                listBoundedDevice.setAdapter(arrayAdapter);
            }

        }
        listBoundedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClintClass clintClass=new ClintClass(btdevice[position]);
                clintClass.start();
                devicename=btdevice[position].getAddress();

            }
        });


    }








    public String[] getpaireddevice()
    {
        bt=bluetoothAdapter.getBondedDevices();
        String[] devicename=new String[bt.size()];
        btdevice=new BluetoothDevice[bt.size()];
        int index=0;
        for(BluetoothDevice device:bt)
        {
            devicename[index]=device.getName();
            btdevice[index]=device;
            System.out.println(device.getAddress());
            index++;
        }
        return devicename;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Bluetooth on", Toast.LENGTH_SHORT).show();


            else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "cancel");

                Toast.makeText(this, "request cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }




    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1) {
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connect succesful");
                sendreseve.write("on".getBytes());
                Log.d(TAG,"write a msg");
//                Intent intent1=new Intent(Connect.this,EmissionHistory.class);
//                intent1.putExtra("DEVICEADD",devicename);
//
//                startActivity(intent1);
            }
            if(msg.what==2)
                Log.d(TAG,"            not ");
            if(msg.what==3)
            {

                byte[] readbuffer= (byte[]) msg.obj;
                String tempMsg=new String(readbuffer,0,msg.arg1);
                Log.d(TAG,"msg rseved  ---"+tempMsg);

            }
            return true;
        }
    });




    private class ClintClass extends Thread{
        BluetoothDevice device;
        BluetoothSocket socket;

        public ClintClass(BluetoothDevice device1){
            device=bluetoothAdapter.getRemoteDevice(device1.getAddress());
            try {
                Log.d(TAG,"device address"+device.getAddress());
                bluetoothAdapter.cancelDiscovery();
                socket=device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());


                Log.d(TAG,"socket"+device.getUuids()[0].getUuid());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void run(){
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=1;
                handler.sendMessage(message);
                sendreseve=new Sendreseve(socket);
                sendreseve.start();
                Log.d(TAG,"sendreseve start");

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=2;
                handler.sendMessage(message);
            }
        }
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }



    private class Sendreseve extends Thread{

        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        public Sendreseve(BluetoothSocket socket){
            bluetoothSocket=socket;
            InputStream tempIN=null;
            OutputStream tempOUT=null;

            try {
                tempIN=bluetoothSocket.getInputStream();
                tempOUT=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream=tempIN;
            outputStream=tempOUT;
        }
        public void run(){
            byte[] buffer=new byte[1024];
            int bytes;
            while(true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(3,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

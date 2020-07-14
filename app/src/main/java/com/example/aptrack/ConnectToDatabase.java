package com.example.aptrack;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectToDatabase {
    FirebaseFirestore mfirebasefirestore;
    CollectionReference collectionReference;
    SimpleDateFormat simpleDateFormat;
    String devicename;


    public ConnectToDatabase(String d)
    {
        devicename=d;
        mfirebasefirestore=FirebaseFirestore.getInstance();
        collectionReference =mfirebasefirestore.collection(devicename);
        simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy");

    }
    public void addvalue(String value)
    {
        Map<String,String> map=new HashMap<>();
        map.put("ppm",value);
        mfirebasefirestore.collection(devicename)
                .document(String.valueOf(simpleDateFormat.format(new Date())))
                .set(map);
    }
    @SuppressLint("LongLogTag")
    public void retrieveData(final Firebacecallback firebacecallback) {

        final String[] string1 = {""};
//        final List<String> tokenContainer = new ArrayList<>();
        collectionReference = mfirebasefirestore.collection(devicename);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String did = documentSnapshot.getId();
                    String val = documentSnapshot.getString("ppm");
                    string1[0] += "ID" + did + "\tppm" + val + "\n";
                    Log.d("connecttto database", string1[0]);
                }
                Log.d("connecttto database", string1[0]);

                firebacecallback.onCallback(string1[0]);


            }
        });



    }





}

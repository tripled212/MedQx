package com.example.medqx.Reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medqx.Locator.MapsActivity;
import com.example.medqx.Locator.MedModel;
import com.example.medqx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Med_take extends AppCompatActivity {
    DatabaseReference medRef, userRef;
    FirebaseAuth mAuth;
    String currentUserID;
    List<String> medModel;
    TextView tv_name, tv_time, tv_medNum, tv_medType, tv_medInteraval;
    Button btnMedtake;

        int Nid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_take);

        medRef = FirebaseDatabase.getInstance().getReference("Storage");
        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_medNum = (TextView) findViewById(R.id.tv_medNum);
        tv_medType = (TextView) findViewById(R.id.tv_medType);
        tv_medInteraval = (TextView) findViewById(R.id.tv_medInteraval);
        btnMedtake = (Button) findViewById(R.id.btnMedtake);




        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

       Intent intent1 = getIntent();
      Bundle bundle = intent1.getExtras();

       if(bundle !=null){
           Nid = bundle.getInt("notificationId");
           finish();
       }

        medRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            List<String> allData = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String as = ds.getValue().toString();

                    allData.add(as);
                    //Log.i("PARSER", "Values: : " + as);
                }
                tv_name.setText("Name : " + allData.get(3));
                tv_medInteraval.setText("Interval : " + allData.get(1));
                tv_time.setText("Hrs and Minutes : " + allData.get(0));
                tv_medNum.setText("Number of Medicine: " + allData.get(4));
                tv_medType.setText("Medicine Type : " + allData.get(2));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnMedtake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                List<String> allData = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String as = ds.getValue().toString();
                        allData.add(as);
                        Log.i("PARSER", "Values: : " + as);

                    }
                    tv_name.setText("Name : " + allData.get(3));
                    tv_medInteraval.setText("Interval : " + allData.get(1));
                    tv_time.setText("Hrs and Minutes : " + allData.get(0));
                    tv_medNum.setText("Number of Medicine: " + allData.get(4));
                    tv_medType.setText("Medicine Type : " + allData.get(2));

                    String saveCurrentTime, saveCurrentDate;
                    Calendar callForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    saveCurrentDate = currentDate.format(callForDate.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = currentTime.format(callForDate.getTime());
                    int MedInt = Integer.parseInt(allData.get(4));
                    HashMap postMap=new HashMap();
                    postMap.put("Name", allData.get(3));
                    postMap.put("Number", MedInt - 1);
                    postMap.put("Hrs and Minutes", allData.get(0));
                    postMap.put("Interval", allData.get(1));
                    postMap.put("Medicine Type", allData.get(2));
                    medRef.child(currentUserID+ saveCurrentDate + saveCurrentTime).updateChildren(postMap);
                }





                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });

                startActivity(new Intent(getApplicationContext(), History_rv.class));
            }
        });
    }



}
package com.example.medqx.Reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medqx.MainActivity;
import com.example.medqx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.gotev.speech.Speech;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SimpleAlarm extends AppCompatActivity implements View.OnClickListener {

    Spinner spInterval, spMedtype;
    DatabaseReference medRef, userRef;
    FirebaseAuth mAuth;
    String currentUserID;

    private int notificationId = 1;

    public int selectedMedicalInterval = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Speech.getInstance().shutdown();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_alarm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
        medRef = FirebaseDatabase.getInstance().getReference("Storage");
        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        findViewById(R.id.btnSetAlarm).setOnClickListener(this);
        spInterval = findViewById(R.id.sp_interval);
        spMedtype = findViewById(R.id.sp_typeofMed);





        ArrayList<MedicalInterval> medicalIntervalList = new ArrayList<MedicalInterval>();
        //add medical interval
        medicalIntervalList.add(new MedicalInterval("Select Medical Interval", "0"));
        medicalIntervalList.add(new MedicalInterval("Every 2 Hour/s", "2"));
        medicalIntervalList.add(new MedicalInterval("Every 3 Hour/s", "3"));
        medicalIntervalList.add(new MedicalInterval("Every 4 Hour/s", "4"));
        medicalIntervalList.add(new MedicalInterval("Every 6 Hour/s", "6"));
        medicalIntervalList.add(new MedicalInterval("Every 8 Hour/s", "8"));
        medicalIntervalList.add(new MedicalInterval("Every 12 Hour/s", "12"));
        medicalIntervalList.add(new MedicalInterval("Every 24 Hour/s", "24"));

        Speech.init(this, getPackageName());


        // fill data in spinner
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SimpleAlarm.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.medicalInterval));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInterval.setAdapter(myAdapter);



        spInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        break;
                    case 1:
                        selectedMedicalInterval = 2;
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        selectedMedicalInterval = 3;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 3:
                        selectedMedicalInterval = 4;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 4:
                        selectedMedicalInterval = 6;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 5:
                        selectedMedicalInterval = 8;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 6:
                        selectedMedicalInterval = 12;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 7:
                        selectedMedicalInterval = 24;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                }
                // test run
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no action
            }
        });

        //String[] MedInterval = {"Every 2 Hour/s","Every 3 Hour/s", "Every 4 Hour/s","Every 6 Hour/s", "Every 8 Hour/s", "Every 12 Hour/s", "Every 24 Hour/s"};
//        String[] MedInterval =  {"2","3", "4","6", "8", "12", "24"};

//        spInterval.setAdapter(new ArrayAdapter<>(SimpleAlarm.this, android.R.layout.simple_spinner_dropdown_item, MedInterval));

//
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

        String[] MedType = {"Capsule/s", "Tablet/s", "Syrup/s"};
        spMedtype.setAdapter(new ArrayAdapter<>(SimpleAlarm.this, android.R.layout.simple_spinner_dropdown_item, MedType));
    }

    public List<LocalDateTime> getListOfDateIntervals(int increments, int selectedHour, int selectedMinutes, int hourInterval) {
        LocalDateTime nowTime = LocalDate.now().atTime(selectedHour, selectedMinutes);
        System.out.print(nowTime);
        List<LocalDateTime> listOfDatetime = new ArrayList<LocalDateTime>();
        listOfDatetime.add(nowTime);
        for (int i = 1; i < increments; i++) {
            // add hourInterval hrs
            nowTime = nowTime.plusHours(hourInterval);
            listOfDatetime.add(nowTime);
        }
        return listOfDatetime;
    }

    @Override
    public void onClick(View v) {
        /*String freq = spInterval.getSelectedItem().toString();
        int freqint = Integer.parseInt(freq);*/
        EditText etMed = findViewById(R.id.etMed);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText med_take = findViewById(R.id.med_take);
        medRef.child(currentUserID).setValue(234);


        if (med_take.getText().toString().equals("")) {

            Toast.makeText(this, "Please Enter Medicine Number!", Toast.LENGTH_SHORT).show();


        } else {

            String num = med_take.getText().toString();
            String name = etMed.getText().toString();

            int medTakeIndex = Integer.parseInt(med_take.getText().toString());
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            switch (v.getId()) {
                case R.id.btnSetAlarm:


                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    int mHour = 0;
                    Random rand = new Random();
                    int n = rand.nextInt(999999); // Gives n such that 0 <= n < 999999

                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    mHour = calendar.getTime().getHours();

                    List<LocalDateTime> dateIntervals = getListOfDateIntervals(medTakeIndex, mHour, minute, selectedMedicalInterval);
                    int i = 0;
                    for (LocalDateTime dateInterval : dateIntervals) {
                        int randomNumber = 2 * (n + i);
                        calendar.set(
                                dateInterval.getYear(),
                                dateInterval.getMonthValue() - 1,
                                dateInterval.getDayOfMonth(),
                                dateInterval.getHour(),
                                dateInterval.getMinute(),
                                0
                        );

                        long alarmStartTime = calendar.getTimeInMillis();
                        Intent intent = new Intent(SimpleAlarm.this, BroadcastReceiver.class);
                        intent.putExtra("notificationId", notificationId + randomNumber);
                        intent.putExtra("todo", etMed.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, randomNumber, intent, 0);
                        // set() schedules an alarm
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                        Log.i("PARSER", "calendar: : " + calendar);
                        Log.i("PARSER", "Date Milliseconds : " + alarmStartTime);
                        Log.i("PARSER", "Random Number : " + randomNumber);

                        i++;
                    }
                    String interval = spInterval.getSelectedItem().toString();

                    HashMap postMap=new HashMap();
                    postMap.put("Name", name);
                    postMap.put("Number", num);
                    postMap.put("Hrs and Minutes", mHour +" : "+minute);
                    postMap.put("Interval", interval);
                    postMap.put("Medicine Type", spMedtype.getSelectedItem());
                    medRef.child(currentUserID).updateChildren(postMap);

                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    break;

            }
        }

    }
}



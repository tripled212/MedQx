package com.example.medqx.Reminder;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medqx.Locator.MedModel;
import com.example.medqx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class History_rv extends AppCompatActivity {

    DatabaseReference hisRef;
    FirebaseAuth mAuth;
    List<MedModel> medModel;
    List<String> allString;
    String currentUserID;
    RecyclerView rvHistory;
    TextView tv_name, tv_time, tv_medNum, tv_medType, tv_medInteraval;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_rv);


        hisRef = FirebaseDatabase.getInstance().getReference("Storage");
        mAuth= FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        medModel = new ArrayList<>();
        allString = new ArrayList<>();

        Query query=hisRef.startAt(currentUserID);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_medNum = (TextView) findViewById(R.id.tv_medNum);
        tv_medType = (TextView) findViewById(R.id.tv_medType);
        tv_medInteraval = (TextView) findViewById(R.id.tv_medInteraval);
        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);



        query.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    MedModel stringMed = ds.getValue(MedModel.class);
                    String val  = ds.getValue().toString();
                    allString.add(val);
                /*    medModel1.getCurrentDate();
                    medModel1.getCurrentTime();*/


                    medModel.add(stringMed);

                    Log.i("PARSER", "Values all: " + medModel.add(stringMed));
                    Log.i("PARSER", "Values all VAL: " + medModel);
                }
                /*History adapter = new History(medModel, getApplicationContext());
                rvHistory.setAdapter(adapter);*/
                String last = allString.get(allString.size() - 1);
                String last1 = allString.get(allString.size() - 2);
                String last2 = allString.get(allString.size() - 3);
                String last3 = allString.get(allString.size() - 4);
                Log.i("PARSER", "Last : " + last);


                String[] AllSearchInput = last.split("\\s*,\\s*");

                String strNew = AllSearchInput[4].replaceFirst("[}{]", "");
                tv_name.setText(strNew);
                tv_time.setText(AllSearchInput[2]);
                String strNew2 = AllSearchInput[0].replaceFirst("[{}]", "");
                tv_medNum.setText(" of Medicine: "+strNew2);
                tv_medType.setText(AllSearchInput[1]);
                tv_medInteraval.setText(AllSearchInput[3]);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

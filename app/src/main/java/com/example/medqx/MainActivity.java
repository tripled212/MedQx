package com.example.medqx;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medqx.Locator.MapsActivity;
import com.example.medqx.Reminder.History_rv;
import com.example.medqx.Reminder.SimpleAlarm;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView cardAlarm;
    CardView cardLocator;
    CardView cardSettings;
    CardView cardLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        cardAlarm=findViewById(R.id.cardAlarm);
        cardLocator=findViewById(R.id.cardLocator);
        cardSettings=findViewById(R.id.cardSettings);
        cardLogout=findViewById(R.id.cardLogout);

        cardAlarm.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SimpleAlarm.class));

        });


        cardLocator.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        });

        cardSettings.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), History_rv.class));

        });

        cardLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent dashboard_Intent = new Intent(this, Login.class);
            dashboard_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(dashboard_Intent);
            finish();

        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


}
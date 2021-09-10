package com.example.medqx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class    Register extends AppCompatActivity {
    EditText fullName, email, password, confirmPassword;
    Button btnRegister;
    TextView loginHere;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 character
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                   // "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        loginHere = findViewById(R.id.loginHere);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        loginHere.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));

        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        btnRegister.setOnClickListener(v -> {
            String xEmail=email.getText().toString();
            String pw=password.getText().toString();
            String confirmPw=confirmPassword.getText().toString();

            if (TextUtils.isEmpty(xEmail)){
                Toast.makeText(Register.this, "Please Enter Email !", Toast.LENGTH_SHORT).show();
            }

            else if (TextUtils.isEmpty(pw)){
                Toast.makeText(Register.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();
            }



            else if (pw.contains(" ")){
                Toast.makeText(this, "Error Space as Password Not Acceptable", Toast.LENGTH_SHORT).show();
            }
            else if ((pw.contains("!") || pw.contains("@")|| pw.contains("#")|| pw.contains("$")|| pw.contains("%")|| pw.contains("^")|| pw.contains("(")) || pw.contains (")")
             || pw.contains("-") || pw.contains("=") || pw.contains("`") || pw.contains("~") || pw.contains("_"))
            {
                Toast.makeText(this, "Error Space as Special Character Not Acceptable", Toast.LENGTH_SHORT).show();
            }

            else if (!PASSWORD_PATTERN.matcher(pw).matches()){
                    password.setError("Atleast Combination of 1 letter and 1 number");
            }

            else if (TextUtils.isEmpty(confirmPw)){
                Toast.makeText(Register.this, "Please Confirm your Password!", Toast.LENGTH_SHORT).show();
            }

            else if (!pw.equals(confirmPw)){
                Toast.makeText(Register.this, "Password Did not match!", Toast.LENGTH_SHORT).show();
            }



            else{
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(xEmail,pw).addOnCompleteListener(task -> {


                    if (task.isSuccessful()){
                        Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent dashboard_Intent = new Intent(this, MainActivity.class);
                        dashboard_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(dashboard_Intent);
                        finish();
                    }

                    else{
                        String message=task.getException().getMessage();
                        Toast.makeText(Register.this, "Error Occurred" + message, Toast.LENGTH_SHORT).show();
                    }

                });
            }

        });
    }
}
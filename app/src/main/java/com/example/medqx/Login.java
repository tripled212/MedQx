package com.example.medqx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText loginEmail, loginPw;
    Button btnLogin;
    TextView createHere, frgtPw;
    ProgressBar progressBar2;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginEmail = findViewById(R.id.loginEmail);
        loginPw = findViewById(R.id.loginPw);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar2 = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        createHere = findViewById(R.id.createHere);
        frgtPw = findViewById(R.id.frgtPw);

        btnLogin.setOnClickListener(v -> {

            String xEmail = loginEmail.getText().toString().trim();
            String pw = loginPw.getText().toString().trim();


            if (TextUtils.isEmpty(xEmail)) {
                loginEmail.setError("Email is Required.");
                return;
            }

            if (TextUtils.isEmpty(pw)) {
                loginPw.setError("Password is Required");
                return;
            }

            if (pw.length() < 6) {
                loginPw.setError("Password Must be 8 Characters");
                return;
            }


            progressBar2.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(xEmail, pw).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);
                }

            });

        });


        /* createHere.setOnClickListener(v -> {
           startActivity(new Intent(getApplicationContext(),Register.class));
        });*/
        createHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        frgtPw.setOnClickListener(v -> {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter your Email to Receive Reset Link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {
                    Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> {
                    Toast.makeText(Login.this, "Error ! Reset Link not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {


            });

            passwordResetDialog.create().show();

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
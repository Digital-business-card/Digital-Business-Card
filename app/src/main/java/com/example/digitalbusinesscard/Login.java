package com.example.digitalbusinesscard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText LoginEmail,LoginPassword;
    Button LoginButton;
    TextView LoginText;
    ProgressBar LoginProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginEmail = findViewById(R.id.LogEmail);
        LoginPassword = findViewById(R.id.LogPassword);
        LoginButton = findViewById(R.id.LogBtn);
        LoginText = findViewById(R.id.LogText);
        LoginProgressBar = findViewById(R.id.LogProgBar);
        fAuth = FirebaseAuth.getInstance();

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();

                //for email
                if(TextUtils.isEmpty(email)){
                    LoginEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    LoginPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 8){
                    LoginPassword.setError("Password Must be =< 8 characters");
                    return;
                }
                LoginProgressBar.setVisibility(View.VISIBLE);

                //-----------------------------------------------------> Authenticate the user (Login the user)

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            LoginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

    }
}
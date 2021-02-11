package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText RegisterEmail,RegisterPassword,RegisterPasswordAgain;
    Button RegisterButton;
    TextView RegisterText;
    ProgressBar RegisterProgressBar;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterEmail = findViewById(R.id.RegEmail);
        RegisterPassword = findViewById(R.id.RegPassword);
        RegisterPasswordAgain = findViewById(R.id.RegPasswordAgain);
        RegisterButton = findViewById(R.id.RegBtn);
        RegisterText  = findViewById(R.id.RegText);
        RegisterProgressBar = findViewById(R.id.RegProgBar);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //------------------------------------------------------------------------>fot exaption
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String email = RegisterEmail.getText().toString().trim();
                String password = RegisterPassword.getText().toString().trim();

                //for email
                if(TextUtils.isEmpty(email)){
                    RegisterEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    RegisterPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 8){
                    RegisterPassword.setError("Password Must be =< 8 characters");
                    return;
                }

                RegisterProgressBar.setVisibility(View.VISIBLE);

                //---------------------------------------------- Register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            RegisterProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        RegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
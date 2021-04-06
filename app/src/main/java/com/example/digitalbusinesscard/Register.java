package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class Register extends AppCompatActivity {
    EditText RegisterEmail,RegisterPassword,RegisterPasswordAgain;
    Button RegisterButton;
    TextView RegisterText;
    ProgressBar RegisterProgressBar;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference DRef;
    Random Ran;
    String UID;
    int min,max,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Ran=new Random();

        RegisterEmail = findViewById(R.id.RegEmail);
        RegisterPassword = findViewById(R.id.RegPassword);
        RegisterPasswordAgain = findViewById(R.id.RegPasswordAgain);
        RegisterButton = findViewById(R.id.RegBtn);
        RegisterText  = findViewById(R.id.RegText);
        RegisterProgressBar = findViewById(R.id.RegProgBar);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        DRef = FirebaseDatabase.getInstance().getReference().child("users");









        //------------------------------------------------------------------------>fot exaption
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                String email = RegisterEmail.getText().toString().trim();
                String password = RegisterPassword.getText().toString().trim();
                String conformPassword = RegisterPasswordAgain.getText().toString().trim();

                //for email
                if(TextUtils.isEmpty(email) ){
                    RegisterEmail.setError("Email is Required!");
                    return;
                }
                if(!email.contains("@"))
                {
                    RegisterEmail.setError("Email must contains @ ");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    RegisterPassword.setError("Password is Required!");
                    return;
                }

                if(password.length() < 8){
                    RegisterPassword.setError("Password Must be =< 8 characters!");
                    return;
                }

                if(conformPassword.isEmpty()  ||  !conformPassword.equals(password))
                {
                    RegisterPasswordAgain.setError("password not match!");
                    return;
                }

                RegisterProgressBar.setVisibility(View.VISIBLE);

                //---------------------------------------------- Register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // For verification the users Email.
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("tag","onFailure: Email not sent" + e.getMessage());
                                }
                            });


                            String tempMin, tempMax;

                            tempMin= String.valueOf(10000);
                            tempMax=String.valueOf(99999);
                            min=Integer.parseInt(tempMin);
                            max=Integer.parseInt(tempMax);


                            if(max>min){

                                result =Ran.nextInt((max-min)+1)+min;
                                UID=String.valueOf(result);

                            }


                            HashMap RealHashMap = new HashMap();
                            RealHashMap.put("fname","new username");
                            RealHashMap.put("description", "new description");
                            RealHashMap.put("phone", "new phone");
                            RealHashMap.put("email", "new email");
                            RealHashMap.put("whatsapp", "new whatsapp");
                            RealHashMap.put("address", "new address");
                            RealHashMap.put("Uid",UID);
                            //RealHashMap.put("users",uri.toString());


                            DRef.child(user.getUid()).updateChildren(RealHashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));

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
                finish();
            }
        });
    }
}
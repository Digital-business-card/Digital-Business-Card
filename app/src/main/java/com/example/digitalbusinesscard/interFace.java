package com.example.digitalbusinesscard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class interFace extends AppCompatActivity {

    Button loginButt ;
    TextView registButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_face);
        loginButt =findViewById(R.id.loginButt);
        registButt=findViewById(R.id.RegistButt);

        registButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interFace.this, Register.class);
                startActivity(intent);
            }



        });
        loginButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interFace.this, Login.class);
                startActivity(intent);
            }



        });


    }


}
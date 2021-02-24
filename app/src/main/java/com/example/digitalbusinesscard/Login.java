package com.example.digitalbusinesscard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Login extends AppCompatActivity {

    EditText LoginEmail,LoginPassword;
    Button LoginButton;
    TextView LoginText;
    ProgressBar LoginProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
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
                            finish();
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
                finish();
            }
        });

        Button changeLang =findViewById(R.id.ChangeMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){

                showChangeLanguageDialog();

            }


        });

    }

    public void showChangeLanguageDialog() {
        final String[] listItems = {"العربية","اردو","English"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(Login.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems,-1,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i){

                if (i ==0){
                    //Choice Arabic
                    setLocale("ar");
                    recreate();
                }
                else if (i ==1){
                    //Choice Arabic
                    setLocale("ur");
                    recreate();
                }

                else if (i ==2){
                    //Choice English
                    setLocale("en");
                    recreate();
                }
                //Dismiss dialog after choice
                dialogInterface.dismiss();
            }
        });




        AlertDialog mDialog = mBuilder.create ();
        //show alert dialog
        mDialog.show();

    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        //Save data to shared preferences
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();

    }

    //Load language saved in shared preferences
    public void loadLocale(){

        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language =prefs.getString("My_lang","");

        setLocale(language);

    }
}
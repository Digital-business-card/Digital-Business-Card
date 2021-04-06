package com.example.digitalbusinesscard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Random;

public class Login extends AppCompatActivity {


    EditText LoginEmail,LoginPassword;
    CheckBox RememberCheck;
    Button LoginButton;
    TextView LoginText,ForgotPassword;
    ProgressBar LoginProgressBar;
    FirebaseAuth fAuth;
    DatabaseReference DReference;
    FirebaseUser users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);

        LoginEmail = findViewById(R.id.LogEmail);
        LoginPassword = findViewById(R.id.LogPassword);
        RememberCheck = findViewById(R.id.RemCheck);
        LoginButton = findViewById(R.id.LogBtn);

        LoginText = findViewById(R.id.LogText);
        ForgotPassword = findViewById(R.id.ForgPassword);
        LoginProgressBar = findViewById(R.id.LogProgBar);



        fAuth = FirebaseAuth.getInstance();
        users = fAuth.getCurrentUser();





        final SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        String checkBox =preferences.getString("remember", "");
        if(checkBox.equals("true")){
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
        }else if(checkBox.equals("false")){

        }



        RememberCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });





        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();

                //for email
                if(TextUtils.isEmpty(email)){
                    LoginEmail.setError("Email is Required!");
                    return;
                }

                if(!email.contains("@"))
                {
                    LoginEmail.setError("Email must contains @ ");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    LoginPassword.setError("Password is Required!");
                    return;
                }

                if(password.length() < 8){
                    LoginPassword.setError("Password Must be =< 8 characters!");
                    return;
                }
                LoginProgressBar.setVisibility(View.VISIBLE);
                //-----------------------------------------------------> Authenticate the user (Login the user)




                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final FirebaseUser user = fAuth.getCurrentUser();
                                 if (task.isSuccessful()) {


                                     // for verification --1
                                     if(user.isEmailVerified()) {
                                         Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                         startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                         finish();
                                     }else{

                                         Toast.makeText(Login.this, "Verify your email.", Toast.LENGTH_SHORT).show();
                                         user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 Toast.makeText(Login.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.d("tag","onFailure: Email not sent" + e.getMessage());
                                             }
                                         });
                                         LoginProgressBar.setVisibility(View.GONE);
                                         return;
                                     }
                                     //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                     //finish();
                                } else {
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

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetPassword = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to Receive reset Link.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String mail = resetPassword.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordResetDialog.create().show();

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
// for verification --2
/**if(user.isEmailVerified()) {
 Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
 startActivity(new Intent(getApplicationContext(), MainActivity.class));
 finish();
 }else{

 Toast.makeText(Login.this, "Verify your email.", Toast.LENGTH_SHORT).show();
 LoginProgressBar.setVisibility(View.GONE);
 return;
 }**/
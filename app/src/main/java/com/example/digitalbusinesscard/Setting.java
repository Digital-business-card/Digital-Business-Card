package com.example.digitalbusinesscard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Setting extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button changPass;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        setContentView(R.layout.activity_setting);

        drawerLayout = findViewById(R.id.drawer_layout);

        changPass=findViewById(R.id.ChangePass);
        fAuth = FirebaseAuth.getInstance();


        //ActionBar actionBar=getSupportActionBar();
        //actionBar.setTitle(getResources().getString(R.string.app_name));

        Button changeLang =findViewById(R.id.ChangeMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){

                showChangeLanguageDialog();

            }


        });



        changPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText changePassword = new EditText(view.getContext());
                AlertDialog.Builder changePasswoedDialog = new AlertDialog.Builder(view.getContext());
                changePasswoedDialog.setTitle("Change Password?");
                changePasswoedDialog.setMessage("Enter your Email to Receive change password Link.");
                changePasswoedDialog.setView(changePassword);

                changePasswoedDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String mail = changePassword.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Setting.this, "Change Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Setting.this, "Error ! Change password Link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                changePasswoedDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                changePasswoedDialog.create().show();

            }
        });


        }

    public void showChangeLanguageDialog() {
        final String[] listItems = {"العربية","اردو","English"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(Setting.this);

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
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang", language);
        editor.apply();    }



    public void ClickMenu(View view){

        MainActivity.openDrawer(drawerLayout);

    }

    public  void ClickHome(View view){

        redirectActivity(this,MainActivity.class);
    }
    public void ClickLogo(View view){

        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view){

        redirectActivity(this, Profile.class);

    }

    public void ClickSetting(View view){

        recreate();
    }

    public void ClickSupport(View view){

        redirectActivity(this,Support.class);
    }

    public  void ClickLogout(View view){

        SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
      redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }



}
package com.example.digitalbusinesscard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Setting extends AppCompatActivity {

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        drawerLayout = findViewById(R.id.drawer_layout);


    }


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

        FirebaseAuth.getInstance().signOut();
        redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }



}
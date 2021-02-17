package com.example.digitalbusinesscard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Profile extends AppCompatActivity {


    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        recreate();

    }

    public void ClickSetting(View view){

        MainActivity.redirectActivity(this,Setting.class);

    }

    public void ClickSupport(View view){

        MainActivity.redirectActivity(this, Support.class);


    }

    public  void ClickLogout(View view){

        FirebaseAuth.getInstance().signOut();
        MainActivity.redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }




}
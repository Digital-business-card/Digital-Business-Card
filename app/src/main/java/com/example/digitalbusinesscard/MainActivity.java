package com.example.digitalbusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        drawerLayout =findViewById(R.id.drawer_layout);

    }

  public void ClickMenu(View view){

        openDrawer(drawerLayout);
  }

  public  void ClickHome(View view){

      redirectActivity(this,MainActivity.class);
  }

  public static void openDrawer(DrawerLayout drawerLayout){
       drawerLayout.openDrawer(GravityCompat.START);
  }

  public void ClickLogo(View view){
        closeDrawer(drawerLayout);
  }

  public static void closeDrawer(DrawerLayout drawerLayout){

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }
  }

  public void ClickProfile(View view){
        //recreate();
      redirectActivity(this,Profile.class);

  }

  public void ClickSetting(View View){

        redirectActivity(this,Setting.class);
        
  }

        public void ClickSupport(View view){

        redirectActivity(this,Support.class);

        }

  public void ClickLogout(View view){

        Logout(this);
  }



    public  void Logout(MainActivity mainActivity) {

        FirebaseAuth.getInstance().signOut();// Logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }



    public static void redirectActivity(Activity activity, Class aClass) {

    Intent intent = new Intent(activity,aClass);

    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    activity.startActivity(intent);

    }

    protected  void onPause(){
       super.onPause();

       closeDrawer(drawerLayout);
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();// Logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
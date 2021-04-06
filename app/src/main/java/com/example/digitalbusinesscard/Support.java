package com.example.digitalbusinesscard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Support extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView navImage;
    TextView UidToolBar,Uname;
    DatabaseReference DRef;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        drawerLayout = findViewById(R.id.drawer_layout);
        navImage = findViewById(R.id.NavImage);
        UidToolBar = findViewById(R.id.UidToolBar);
        Uname = findViewById(R.id.Uname);

        DRef = FirebaseDatabase.getInstance().getReference().child("users");
        fAuth = FirebaseAuth.getInstance();

        getUserinfo();
    }

    private void getUserinfo() {
        DRef.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0)
                {
                    if (snapshot.hasChild("image"))
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String UID = snapshot.child("Uid").getValue().toString();
                        String UName = snapshot.child("fname").getValue().toString();
                        Picasso.get().load(image).into(navImage);
                        UidToolBar.setText(UID);
                        Uname.setText(UName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        MainActivity.redirectActivity(this, Profile.class);

    }

    public void ClickSetting(View view){

        MainActivity.redirectActivity(this,Setting.class);

    }

    public void ClickSupport(View view){

        recreate();

    }

    public  void ClickLogout(View view){

        SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        MainActivity.redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }
}
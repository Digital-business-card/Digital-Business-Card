package com.example.digitalbusinesscard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Profile extends AppCompatActivity {

    TextView ProfullName,Description,PhoneNumber,Email,Whatsapp,Address;
    ImageView ProfileImage,navImage;
    DrawerLayout drawerLayout;
    Button EditButton;
    TextView UidToolBar,Uname;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    StorageReference storageReference;
    DatabaseReference DRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        navImage = findViewById(R.id.NavImage);
        drawerLayout = findViewById(R.id.drawer_layout);

        ProfullName = findViewById(R.id.ProName);
        Description = findViewById(R.id.ProDescription);
        PhoneNumber = findViewById(R.id.ProPhone);
        Email = findViewById(R.id.ProEmail);
        Whatsapp = findViewById(R.id.ProWhats);
        Address = findViewById(R.id.ProAddress);
        ProfileImage = findViewById(R.id.ProImage);
        EditButton = findViewById(R.id.EditProfBtn);

        UidToolBar = findViewById(R.id.UidToolBar);
        Uname = findViewById(R.id.Uname);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        DRef= FirebaseDatabase.getInstance().getReference().child("users");


        /**StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ProfileImage);
            }
        });**/
        getUserinfo();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    ProfullName.setText(documentSnapshot.getString("fName"));
                    Description.setText(documentSnapshot.getString("description"));
                    PhoneNumber.setText(documentSnapshot.getString("phone"));
                    Email.setText(documentSnapshot.getString("email"));
                    Whatsapp.setText(documentSnapshot.getString("whatsapp"));
                    Address.setText(documentSnapshot.getString("address"));

                }else {
                    Log.d("tag","onEvent: Document is not exists");
                }
            }
        });

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(),EditProfile.class);
                i.putExtra("ProfullName",ProfullName.getText().toString());
                i.putExtra("Description",Description.getText().toString());
                i.putExtra("phoneNumber",PhoneNumber.getText().toString());
                i.putExtra("Email",Email.getText().toString());
                i.putExtra("Whatsapp",Whatsapp.getText().toString());
                i.putExtra("Address",Address.getText().toString());
                startActivity(i);

                //startActivity(new Intent(getApplicationContext(),EditProfile.class));
                // finish();

            }
        });

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
                        Picasso.get().load(image).into(ProfileImage);
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

        recreate();


    }

    public void ClickSetting(View view){

        redirectActivity(this, Setting.class);
    }

    public void ClickSupport(View view){

        redirectActivity(this,Support.class);
    }

    public  void ClickLogout(View view){

        SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        FirebaseAuth.getInstance().signOut();// Logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
        redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }





}

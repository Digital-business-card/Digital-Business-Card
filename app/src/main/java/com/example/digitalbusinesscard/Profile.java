package com.example.digitalbusinesscard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Profile extends AppCompatActivity {

    TextView ProfullName,Description,PhoneNumber,Email,Whatsapp,Address;
    ImageView ProfileImage;
    DrawerLayout drawerLayout;
    Button EditButton;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);

        ProfullName = findViewById(R.id.ProName);
        Description = findViewById(R.id.ProDescription);
        PhoneNumber = findViewById(R.id.ProPhone);
        Email = findViewById(R.id.ProEmail);
        Whatsapp = findViewById(R.id.ProWhats);
        Address = findViewById(R.id.ProAddress);
        ProfileImage = findViewById(R.id.ProImage);
        EditButton = findViewById(R.id.EditProfBtn);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ProfileImage);
            }
        });

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
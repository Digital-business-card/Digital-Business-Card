package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyFriendsProfile extends AppCompatActivity {

    DatabaseReference DRef;
    FirebaseUser user;
    FirebaseAuth fAuth;

    String username,description,email,phone,whatsapp,address;

    String userID;
    TextView Username,Description,Email,Phone,Whatsapp,Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_profile);
        userID=getIntent().getStringExtra("userKey");

        Username=findViewById(R.id.usernameFriendPro);
        Description=findViewById(R.id.descriptionFriendPro);
        Email=findViewById(R.id.emailFriendPro);
        Phone=findViewById(R.id.phoneFriendPro);
        Whatsapp=findViewById(R.id.whatsFriendPro);
        Address=findViewById(R.id.addressFriendPro);

        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        DRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(user.getUid());


        LoadFriend();

    }

    private void LoadFriend() {

        DRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    username=snapshot.child(userID).child("username").getValue().toString();
                    description=snapshot.child(userID).child("description").getValue().toString();
                    email=snapshot.child(userID).child("email").getValue().toString();
                    phone=snapshot.child(userID).child("phone").getValue().toString();
                    whatsapp=snapshot.child(userID).child("whatsapp").getValue().toString();
                    address=snapshot.child(userID).child("address").getValue().toString();

                   Username.setText(username);
                    Description.setText(description);
                    Email.setText(email);
                    Phone.setText(phone);
                   Whatsapp.setText(whatsapp);
                   Address.setText(address);
                }
                else 
                {
                    Toast.makeText(MyFriendsProfile.this, "Data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyFriendsProfile.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
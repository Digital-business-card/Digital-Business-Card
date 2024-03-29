package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendsProfile extends AppCompatActivity {

    DatabaseReference DRef;
    FirebaseUser user;
    FirebaseAuth fAuth;

    String username,description,email,phone,whatsapp,address,image;
    CircleImageView friendProfileImage;

    String userID;
    TextView Username,Description,Email,Phone,Whatsapp,Address;
    ImageView CopyName,CopyDescription,CopyEmail,CopyPhone,CopyWhatsApp,CopyAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_profile);
        userID=getIntent().getStringExtra("userKey");

        friendProfileImage=findViewById(R.id.profileImage);
        Username=findViewById(R.id.usernameFriendPro);
        Description=findViewById(R.id.descriptionFriendPro);
        Email=findViewById(R.id.emailFriendPro);
        Phone=findViewById(R.id.phoneFriendPro);
        Whatsapp=findViewById(R.id.whatsFriendPro);
        Address=findViewById(R.id.addressFriendPro);

        CopyName = findViewById(R.id.Copy1);
        CopyDescription = findViewById(R.id.Copy2);
        CopyEmail = findViewById(R.id.Copy3);
        CopyPhone = findViewById(R.id.Copy4);
        CopyWhatsApp = findViewById(R.id.Copy5);
        CopyAddress = findViewById(R.id.Copy6);

        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        DRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(user.getUid());


        LoadFriend();


        CopyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Username.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        CopyDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Description.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        CopyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Email.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        CopyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Phone.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        CopyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Whatsapp.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        CopyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Address.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MyFriendsProfile.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });

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
                    image=snapshot.child(userID).child("image").getValue().toString();

                    Picasso.get().load(image).into(friendProfileImage);
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
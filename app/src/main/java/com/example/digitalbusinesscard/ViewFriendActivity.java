package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendActivity<Private> extends AppCompatActivity {

    DatabaseReference DRef,requestRef,friendRef;
    FirebaseAuth fAuth;
    FirebaseUser user;

    String username,description,email,phone,whatsapp,address,image,Uid;
    String fusername,fdescription,femail,fphone,fwhatsapp,faddress,fimage,fUid;


    CircleImageView profileImage;
    TextView Username,Description,UserIDFriend;
    Button btnPerform,btnDecline;
    String CurrentState="nothing_happen";
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

         userID=getIntent().getStringExtra("userKey");

        DRef= FirebaseDatabase.getInstance().getReference().child("users");
        requestRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef= FirebaseDatabase.getInstance().getReference().child("Friends");
        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();

        Description=findViewById(R.id.DescriptionFriendPro);
        profileImage=findViewById(R.id.profileImage);
        Username=findViewById(R.id.usernameFriendPro);
        UserIDFriend=findViewById(R.id.UserIDFriend);
        btnPerform=findViewById(R.id.btnPerform);
        btnDecline=findViewById(R.id.btnDecline);


       LoadUser();

       btnPerform.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               PerformAction(userID);
           }
       });

       CheckUserExistance(userID);

       btnDecline.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Unfriend(userID);
           }

       });
    }

    private void Unfriend(final String userID) {
        if(CurrentState.equals("friend"))
        {
            friendRef.child(user.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        friendRef.child(userID).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(ViewFriendActivity.this, "You are UnFriend", Toast.LENGTH_SHORT).show();
                                    CurrentState="nothing_happen";
                                    btnPerform.setText("Send Friend Request");
                                    btnDecline.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }

        if (CurrentState.equals("he_sent_pending"))
        {
            HashMap hashMap=new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userID).child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ViewFriendActivity.this, "You have Decline Friend", Toast.LENGTH_SHORT).show();
                        CurrentState="he_send_decline";
                        btnPerform.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void CheckUserExistance(String userID) {
        friendRef.child(user.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    CurrentState="friend";
                    btnPerform.setText("View Profile");
                    btnDecline.setText("UnFriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friendRef.child(userID).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    CurrentState="friend";
                    btnPerform.setText("View Profile");
                    btnDecline.setText("UnFriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(user.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals("decline"))
                    {
                        CurrentState="I_sent_decline";
                        btnPerform.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if(snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="he_sent_pending";
                        btnPerform.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing_happen"))
        {
           CurrentState="nothing_happen";
           btnPerform.setText("Send Friend Request");
           btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(final String userID){

        if(CurrentState.equals("nothing_happen"))
        {
            HashMap hashMap=new HashMap();
            hashMap.put("status","pending");
            requestRef.child(user.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ViewFriendActivity.this, "You have send Friend Request.", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                    }
                    else
                    {
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        if (CurrentState.equals("I_sent_pending")|| CurrentState.equals("I_sent_decline"))
        {
            requestRef.child(user.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(ViewFriendActivity.this, "You have cancelled Friend Request.", Toast.LENGTH_SHORT).show();
                             CurrentState="nothing_happen";
                             btnPerform.setText("Send Friend Request");
                             btnDecline.setVisibility(View.GONE);
                         }
                         else
                         {
                             Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                         }
                }
            });
        }
        if (CurrentState.equals("he_sent_pending"))
        {
            requestRef.child(userID).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        HashMap hashmap=new HashMap();
                        hashmap.put("status","friend");
                        hashmap.put("username",username);
                        hashmap.put("description",description);
                        hashmap.put("email",email);
                        hashmap.put("phone",phone);
                        hashmap.put("whatsapp",whatsapp);
                        hashmap.put("address",address);
                        hashmap.put("image",image);
                        hashmap.put("Uid",Uid);
                        friendRef.child(user.getUid()).child(userID).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    HashMap hashmap=new HashMap();
                                    hashmap.put("status","friend");
                                    hashmap.put("username",fusername);
                                    hashmap.put("description",fdescription);
                                    hashmap.put("email",femail);
                                    hashmap.put("phone",fphone);
                                    hashmap.put("whatsapp",fwhatsapp);
                                    hashmap.put("address",faddress);
                                    hashmap.put("image",fimage);
                                    hashmap.put("Uid",fUid);

                                    friendRef.child(userID).child(user.getUid()).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ViewFriendActivity.this, "You Added friend", Toast.LENGTH_SHORT).show();
                                            CurrentState="friend";
                                            btnPerform.setText("View Profile");
                                            btnDecline.setText("UnFriend");
                                            btnDecline.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                            }
                        });

                    }
                }
            });
        }
        if(CurrentState.equals("friend"))
        {


            Intent intent=new Intent(ViewFriendActivity.this,MyFriendsProfile.class);
            intent.putExtra("userKey",userID);
            HashMap hashmap=new HashMap();
            hashmap.put("status","friend");
            hashmap.put("username",username);
            hashmap.put("description",description);
            hashmap.put("email",email);
            hashmap.put("phone",phone);
            hashmap.put("whatsapp",whatsapp);
            hashmap.put("address",address);
            hashmap.put("image",image);
            hashmap.put("Uid",Uid);
            friendRef.child(user.getUid()).child(userID).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        HashMap hashmap=new HashMap();
                        hashmap.put("status","friend");
                        hashmap.put("username",fusername);
                        hashmap.put("description",fdescription);
                        hashmap.put("email",femail);
                        hashmap.put("phone",fphone);
                        hashmap.put("whatsapp",fwhatsapp);
                        hashmap.put("address",faddress);
                        hashmap.put("image",fimage);
                        hashmap.put("Uid",fUid);

                        friendRef.child(userID).child(user.getUid()).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                            }
                        });

                    }
                }
            });
            startActivity(intent);
        }
    }

    private void LoadUser() {

        DRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    username=snapshot.child("fname").getValue().toString();
                    description=snapshot.child("description").getValue().toString();
                    email=snapshot.child("email").getValue().toString();
                    phone=snapshot.child("phone").getValue().toString();
                    whatsapp=snapshot.child("whatsapp").getValue().toString();
                    address=snapshot.child("address").getValue().toString();
                    image=snapshot.child("image").getValue().toString();
                    Uid=snapshot.child("Uid").getValue().toString();

                    Picasso.get().load(image).into(profileImage);
                    Username.setText(username);
                    UserIDFriend.setText(Uid);
                    Description.setText(description);




                }
                else {
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        DRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    fusername=snapshot.child("fname").getValue().toString();
                    fdescription=snapshot.child("description").getValue().toString();
                    femail=snapshot.child("email").getValue().toString();
                    fphone=snapshot.child("phone").getValue().toString();
                    fwhatsapp=snapshot.child("whatsapp").getValue().toString();
                    faddress=snapshot.child("address").getValue().toString();
                    fimage=snapshot.child("image").getValue().toString();
                    fUid=snapshot.child("Uid").getValue().toString();



                }
                else {
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
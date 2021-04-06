package com.example.digitalbusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    ImageView toolBarImage,navImage;
    String userId;
    Button  resentVerify;
    RecyclerView recyclerView;
    String userID;
    ImageView searchAll;
    EditText inputSreach;
    TextView UidToolBar,Uname;

    DatabaseReference DReference,DRef;
    FirebaseUser user;

    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends,FriendMyViewHolder>adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         userID=getIntent().getStringExtra("userKey");



        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        DReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        DRef = FirebaseDatabase.getInstance().getReference().child("users");
        user = fAuth.getCurrentUser();


        drawerLayout =findViewById(R.id.drawer_layout);
        toolBarImage = findViewById(R.id.Nav_Image);
        navImage = findViewById(R.id.NavImage);
        UidToolBar = findViewById(R.id.UidToolBar);
        Uname = findViewById(R.id.Uname);
       // resentVerify = findViewById(R.id.LogVerify);
        searchAll =findViewById(R.id.searchAll);

        inputSreach=findViewById(R.id.inputSearch);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LoadFriends("");




          /**StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(toolBarImage);
                Picasso.get().load(uri).into(navImage);
            }
        });**/
        getUserinfo();

        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Users.class));
                finish();
                /**user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag","onFailure: Email not sent" + e.getMessage());
                    }
                });**/
            }
        });

        LoadFriends("");
        inputSreach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null){
                    LoadFriends(editable.toString());
                }
                else {
                    LoadFriends("");
                }

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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadFriends(String s) {
        Query query=DReference.child(user.getUid()).orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        //Query UIDquery=DReference.child(user.getUid()).orderByChild("Uid").startAt(s).endAt(s+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        //options=new FirebaseRecyclerOptions.Builder<Friends>().setQuery(UIDquery,Friends.class).build();
        adapter=new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, final int position, @NonNull Friends model) {
                if(!user.getUid().equals(getRef(position).getKey().toString())){
                    Picasso.get().load(model.getImage()).into(holder.profile_image);
                    holder.username.setText(model.getUsername());
                    holder.UserId.setText(model.getUid());
                }
                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,ViewFriendActivity.class);
                        intent.putExtra("userKey",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend,parent,false);

                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

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
        SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
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
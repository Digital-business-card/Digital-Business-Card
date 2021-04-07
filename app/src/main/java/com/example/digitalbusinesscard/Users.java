package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.digitalbusinesscard.MainActivity.redirectActivity;

public class Users extends AppCompatActivity {


    FirebaseRecyclerOptions<GetUsers> options;
    FirebaseRecyclerAdapter<GetUsers, UsersViewHolder>adapter;


    DrawerLayout drawerLayout;
    ImageView navImage;
    TextView UidToolBar,Uname;
    DatabaseReference DRef;
    FirebaseAuth fAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    EditText inputSreach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        navImage = findViewById(R.id.NavImage);
        drawerLayout = findViewById(R.id.drawer_layout);

        inputSreach=findViewById(R.id.inputSearch);
        UidToolBar = findViewById(R.id.UidToolBar);
        Uname = findViewById(R.id.Uname);



        recyclerView=findViewById(R.id.reyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DRef= FirebaseDatabase.getInstance().getReference().child("users");
        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();

        getUserinfo();
        
        LoadUsers("");

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
                    LoadUsers(editable.toString());
                }
                else {
                    LoadUsers("");

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



    private void LoadUsers(String s) {

        Query query=DRef.orderByChild("fname").startAt(s).endAt(s+"\uf8ff");
       // Query UIDquery=DRef.orderByChild("Uid").startAt(s).endAt(s+"\uf8ff");

        //options=new FirebaseRecyclerOptions.Builder<GetUsers>().setQuery(UIDquery,GetUsers.class).build();
        options=new FirebaseRecyclerOptions.Builder<GetUsers>().setQuery(query,GetUsers.class).build();


        adapter=new FirebaseRecyclerAdapter<GetUsers, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull GetUsers model) {

                if(!user.getUid().equals(getRef(position).getKey().toString())){
                    Picasso.get().load(model.getImage()).into(holder.profile_Image);
                    holder.UserId.setText(model.getUid());
                    holder.username.setText(model.getFname());

                }
                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(Users.this,ViewFriendActivity.class);
                        intent.putExtra("userKey",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });

            }
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_users,parent,false);
                return new UsersViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }



   /** @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadUsers(s);
                return false;
            }
        });



        return true;
    }**/

    public void ClickMenu(View view){

        openDrawer(drawerLayout);

    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
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


        MainActivity.redirectActivity(this, Login.class);

    }


    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }
}
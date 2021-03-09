package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class showEvent extends AppCompatActivity {

    private List<Event> eventData;
    RecyclerView recyclerView;
    EventAdapter adapter;
    DatabaseReference reff;

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        recyclerView = findViewById(R.id.eventRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(showEvent.this, "Loggin Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(showEvent.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(showEvent.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        };

      eventData=new ArrayList<>();
        reff= FirebaseDatabase.getInstance().getReference("Event");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        Event e=npsnapshot.getValue(Event.class);
                        eventData.add(e);
                    }
                    adapter=new EventAdapter(eventData);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void ClickMenu(View view){
        // open drawer
        HomeActivity.openDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        // close drawer layout

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            // when drawer is open close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickLogo(View view)
    {
        HomeActivity.closeDrawer(drawerLayout);
    }




    public void ClickHome(View view){

        /*Intent intent= new Intent(this, HomeActivity.class);
        startActivity(intent);*/

        final String uid = mAuth.getCurrentUser().getUid(); // gives the uid of the logged in person

                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren())
                                        {
                                            Map<String, Object> data = (Map<String, Object>) snapshot.getValue();

                                            String userkey = snapshot.getKey();
                                            String type = (String) data.get("type");
                                            if(userkey == uid ) // key equals to the logged in users id
                                            {
                                                if(type.equals("admin"))
                                                {
                                                    // intent to admin homepage
                                                    Intent intent= new Intent(showEvent.this, AdminHomePage.class);
                                                    startActivity(intent);

                                                }
                                                else // type will be User
                                                {
                                                    // intent to user homepage
                                                    Intent i = new Intent(showEvent.this, HomeActivity.class);
                                                    startActivity(i);
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



    }

    public void ClickLogout(View view) {
        // close app

        AlertDialog.Builder builder = new AlertDialog.Builder(showEvent.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.addAuthStateListener(mAuthStateListener);
                mAuth.signOut();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();



    }

    @Override
    protected void onPause()
    {
        super.onPause();
        closeDrawer(drawerLayout);
    }






}

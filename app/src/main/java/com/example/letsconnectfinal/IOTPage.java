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
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IOTPage extends AppCompatActivity {


    Button btn;

    RecyclerView mRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    String domain = "IOT";
    String domain_name;

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iotpage);

        drawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(IOTPage.this, "Loggin Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(IOTPage.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(IOTPage.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        };

        Intent i = getIntent();
        //Bundle bundle = i.getExtras();
        domain_name = i.getStringExtra("domName");



        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                next_page();
            }
        });


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        //reference = firebaseDatabase.getReference("IOTImage");
        reference = firebaseDatabase.getReference(domain_name);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, ViewHolder>(

                        Member.class,
                        R.layout.image,
                        ViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Member member, int i) {

                        viewHolder.setdetails(getApplicationContext(), member.getTitle(), member.getImage());

                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        System.out.println("********************************************");
        System.out.println(firebaseRecyclerAdapter);
        System.out.println("********************************************");
    }


    public void next_page()
    {
        //Intent intent= new Intent(this, ImageUpload.class).putExtra("Domain", domain);
        Intent intent= new Intent(this, ImageUpload.class).putExtra("Domain", domain_name);
        startActivity(intent);
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
        // recreate activity
        Intent intent= new Intent(this, HomeActivity.class);
        startActivity(intent);


    }

    public void ClickLogout(View view) {
        // close app

        AlertDialog.Builder builder = new AlertDialog.Builder(IOTPage.this);
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

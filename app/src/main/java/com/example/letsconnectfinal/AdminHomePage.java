package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    CardView addEvent, addDomain, showEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        drawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(AdminHomePage.this, "Loggin Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AdminHomePage.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(AdminHomePage.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        };

        addDomain = findViewById(R.id.addDomainCard);
        addEvent = findViewById(R.id.addEventCard);
        showEvent = findViewById(R.id.viewEventCard);


        addDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this,addDomain.class);
                startActivity(intent);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this,addEvent.class);
                startActivity(intent);
            }
        });

        showEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this,showEvent.class);
                startActivity(intent);
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
        closeDrawer(drawerLayout);
    }




    public void ClickHome(View view){
        // recreate activity

        recreate();


    }

    public void ClickLogout(View view) {
        // close app

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomePage.this);
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

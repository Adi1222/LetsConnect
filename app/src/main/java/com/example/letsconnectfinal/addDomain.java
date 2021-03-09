package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class addDomain extends AppCompatActivity {

    EditText domain_name,domain_desc;
    Button addBtn;
    DatabaseReference reff;
    Domain domain;
    long maxid=0;

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_domain);

        drawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(addDomain.this, "Loggin Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(addDomain.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(addDomain.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        };



        domain_name = (EditText)findViewById(R.id.domain_name);
        domain_desc = (EditText)findViewById(R.id.domain_desc);
        addBtn = (Button)findViewById(R.id.addButton);
        domain = new Domain();
        reff = FirebaseDatabase.getInstance().getReference().child("Domain");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domain.setName(domain_name.getText().toString().trim());
                domain.setDescription(domain_desc.getText().toString().trim());
                reff.child(String.valueOf(maxid+1)).setValue(domain);

                Toast.makeText(addDomain.this,"Domain Added successfully",Toast.LENGTH_SHORT).show();
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

        Intent intent= new Intent(addDomain.this, AdminHomePage.class);
        startActivity(intent);


    }

    public void ClickLogout(View view) {
        // close app

        AlertDialog.Builder builder = new AlertDialog.Builder(addDomain.this);
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

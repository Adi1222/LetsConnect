package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addEvent extends AppCompatActivity {

    EditText title,eventDate,location,description,link;
    int year,month,day;
    Button addEventBtn;
    DatabaseReference reff2;
    Event event;
    long maxid=0;

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);




        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(addEvent.this, "Loggin Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(addEvent.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(addEvent.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        };


        title = (EditText)findViewById(R.id.title);
        location = (EditText)findViewById(R.id.location);
        link = (EditText)findViewById(R.id.link);
        eventDate = (EditText)findViewById(R.id.event_date);
        description = (EditText)findViewById(R.id.description);
        final Calendar calendar = Calendar.getInstance();
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(addEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eventDate.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year,month,day);
                datePickerDialog.show();

            }
        });

        addEventBtn = (Button)findViewById(R.id.addEventBtn);
        event = new Event();
        reff2 = FirebaseDatabase.getInstance().getReference().child("Event");
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setTitle(title.getText().toString().trim());
                event.setDate(eventDate.getText().toString().trim());
                event.setLocation(location.getText().toString().trim());
                event.setLink(link.getText().toString().trim());
                event.setDescription(description.getText().toString().trim());
                reff2.child(String.valueOf(maxid+1)).setValue(event);
                // reff2.push().setValue(event);

                Toast.makeText(addEvent.this,"Event Added successfully",Toast.LENGTH_SHORT).show();
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

        Intent intent= new Intent(addEvent.this, AdminHomePage.class);
        startActivity(intent);


    }

    public void ClickLogout(View view) {
        // close app


        AlertDialog.Builder builder = new AlertDialog.Builder(addEvent.this);
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

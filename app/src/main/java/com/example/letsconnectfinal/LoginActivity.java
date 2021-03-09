package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    EditText emailId, password;
    Button btnlogin;
    TextView tvsignin;

    //Spinner spinneroption;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        //spinneroption = (Spinner) findViewById(R.id.spinneropt);
        btnlogin = findViewById(R.id.btnlogin);
        tvsignin = findViewById(R.id.textViewSignIn);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser != null)
                {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                //String option =spinneroption.getSelectedItem().toString();

                if(email.isEmpty())
                {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty())
                {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty()))
                {
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                /* logic for admin or a normal user */
                                //String uid = mAuth.getCurrentUser().getUid(); // gives the uid of the logged in person

                                /*mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren())
                                        {
                                            Map<String, Object> data = (Map<String, Object>) snapshot.getValue();

                                            String userkey = snapshot.getKey();
                                            String type = (String) data.get("type");
                                            if(userkey == uid && type == "Admin") // key equals to the logged in users id
                                            {
                                                if(type == "Admin")
                                                {
                                                    // logic for intent to admin page
                                                }
                                                else // type will be User
                                                {
                                                    // logic for intent to user page
                                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(i);
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });*/

                                if(email.equals("admin123@gmail.com"))
                                {
                                    Intent i = new Intent(LoginActivity.this, AdminHomePage.class);
                                    startActivity(i);
                                }
                                else
                                {
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);
                                }

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}

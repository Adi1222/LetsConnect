package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity {

    EditText emailId,firstname, lastname, password;
    Button btnsignup;
    TextView tvsignup;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmailAddress);
        firstname = findViewById(R.id.editTextFirstName);
        lastname = findViewById(R.id.editTextLastName);
        password = findViewById(R.id.editTextPassword);
        btnsignup = findViewById(R.id.btnsignup);
        tvsignup = findViewById(R.id.textViewSignUp);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser != null)
                {
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Signup", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                final String fname = firstname.getText().toString();
                final String lname = lastname.getText().toString();

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
                else if(TextUtils.isEmpty(fname))
                {
                    firstname.setError("Please enter your First Name");
                    firstname.requestFocus();
                }
                else if(TextUtils.isEmpty(lname))
                {
                    lastname.setError("Please enter your last name");
                    lastname.requestFocus();
                }
                else if(email.isEmpty() || pwd.isEmpty() || fname.isEmpty() || lname.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Fields atre empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && fname.isEmpty()  && lname.isEmpty() ))
                {
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccessful! Please Try Again", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                            /*mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful())
                                        {
                                            Toast.makeText(MainActivity.this, "Login Error", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {

                                            String key = mRef.push().getKey();
                                            //String uid = mAuth.getCurrentUser().getUid();

                                            mRef.child(key).child("email").setValue(email);
                                            mRef.child(key).child("type").setValue("user");

                                            startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                        }
                                    }
                                });*/

                                onAuthSuccess(task.getResult().getUser(), fname, lname);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        tvsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user, String fname, String lname) {



        // Write new user
        writeNewUser(user.getUid(), "user", user.getEmail(), fname, lname);

        // Go to HomeActivity
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    private void writeNewUser(String userId, String type, String email, String fname, String lname) {
        User user = new User(type, email, fname, lname);

        mRef.child(userId).setValue(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}

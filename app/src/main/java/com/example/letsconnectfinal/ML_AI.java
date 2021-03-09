package com.example.letsconnectfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ML_AI extends AppCompatActivity {

    Button btn;

    RecyclerView mRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    String domain = "ML_AI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml__ai);


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
        reference = firebaseDatabase.getReference("MLImage");
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
        Intent intent= new Intent(this, ImageUpload.class).putExtra("Domain", domain);
        startActivity(intent);
    }
}

package com.example.letsconnectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewDomainsButton extends AppCompatActivity {

    Button iot, ml, wd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_domains_button);

        iot = findViewById(R.id.iot);
        ml = findViewById(R.id.ml);
        wd = findViewById(R.id.wd);

        final String dom = "ML_AI";

        iot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_pageiot(dom);
            }
        });

        ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_pageml(dom);
            }
        });

        wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_pagewd(dom);
            }
        });
    }

    public void next_pageiot(String dom)
    {
        Intent intent= new Intent(this, IOTPage.class).putExtra("dom", dom);
        startActivity(intent);
    }

    public void next_pageml(String dom)
    {
        Intent intent= new Intent(this, ML_AI.class);
        startActivity(intent);
    }

    public void next_pagewd(String dom)
    {
        Intent intent= new Intent(this, WebDev.class);
        startActivity(intent);
    }
}

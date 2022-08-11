package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Message_DefaultActivity extends AppCompatActivity {

    private ImageView home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_default);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_DefaultActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

    }
}
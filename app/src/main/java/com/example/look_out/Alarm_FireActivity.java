package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Alarm_FireActivity extends AppCompatActivity {

    private Button offButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_fire);

        offButton = findViewById(R.id.offButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Alarm_FireActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });
    }
}
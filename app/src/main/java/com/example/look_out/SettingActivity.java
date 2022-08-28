package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageButton messagedefault;
    private ImageButton settingalarm;
    private ImageButton settingdevice;
    private ImageButton watchlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        messagedefault = findViewById(R.id.messagedefault);
        messagedefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, Message_DefaultActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        settingalarm = findViewById(R.id.settingalarm);
        settingalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, Setting_AlarmActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        settingdevice = findViewById(R.id.settingdevice);
        settingdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_DeviceActivity.class);
                startActivity(intent);
            }
        });

        watchlog = findViewById(R.id.watchlog);
        watchlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_WatchLogActivity.class);
                startActivity(intent);
            }
        });
    }//end of onCreate

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}//end of class

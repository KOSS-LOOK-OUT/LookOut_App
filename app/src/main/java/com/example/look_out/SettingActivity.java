package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private ImageView home;
    private Button messagedefault;
    static Switch Sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Sound = findViewById(R.id.Sound);
        Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //토스트 메시지(무음모드인 경우에도 진동이 울립니다.)
                    Toast.makeText(SettingActivity.this, "무음모드인 경우에도 진동이 울립니다.", Toast.LENGTH_SHORT).show();
                } else{
                    //토스트 메시지(무음모드인 경우 진동이 울리지 않습니다.)
                    Toast.makeText(SettingActivity.this, "무음모드인 경우 진동이 울리지 않습니다.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
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

    }
}

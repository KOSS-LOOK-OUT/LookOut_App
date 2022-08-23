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

public class Setting_AlarmActivity extends AppCompatActivity {

    static Switch Sound;
    String shared = "file";
    Boolean sound;
    public static Context context_main;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        context_main = this;

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_AlarmActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        Sound = findViewById(R.id.Sound);
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        sound = sharedPreferences.getBoolean("switch", false);
        Sound.setChecked(sound);

        Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("switch", Sound.isChecked());
                editor.commit();

                if(isChecked){
                    //토스트 메시지(무음모드인 경우에도 진동이 울립니다.)
                    Toast.makeText(Setting_AlarmActivity.this, "무음모드인 경우에도 진동이 울립니다.", Toast.LENGTH_SHORT).show();
                } else{
                    //토스트 메시지(무음모드인 경우 진동이 울리지 않습니다.)
                    Toast.makeText(Setting_AlarmActivity.this, "무음모드인 경우 진동이 울리지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Setting_WatchLogActivity extends AppCompatActivity {
    private ImageView backButton;
    private ListView listView;
    List<String> al_log = new ArrayList<>();
    List<String> allog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_log);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_WatchLogActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allog);
        al_log = ((MainActivity)MainActivity.context_main).al_log;

        if(al_log.size() == 0){
            allog.add("보여줄 로그가 없습니다.");
        }
        else {
            if(al_log.size() > 50){
                for(int i = al_log.size() -51; i >= 0; i --){
                    al_log.remove(i);
                }
            }

            for (int i = al_log.size() - 1; i >= 0; i--) {
                allog.add(al_log.get(i));
            }
        }


        listView.setAdapter(adpater);
        listView.bringToFront();
    }//end of onCreate

}//end of class

package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> al_log = new ArrayList<>();
    ArrayList<String> allog = new ArrayList<>();
    private Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allog);
        al_log = ((MainActivity)MainActivity.context_main).al_log;

        resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al_log.clear();
                allog.clear();
                if(al_log.size() == 0){
                    allog.add("보여줄 로그가 없습니다.");
                }
                adpater.notifyDataSetChanged();
            }
        });

        if(al_log.size() == 0){
            allog.add("보여줄 로그가 없습니다.");
        }
        else {
            for (int i = al_log.size() - 1; i > al_log.size() - 51; i--) {
                allog.add(al_log.get(i));
            }
        }

        listView.setAdapter(adpater);
    }

}
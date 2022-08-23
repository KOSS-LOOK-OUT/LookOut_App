package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> al_log = new ArrayList<>();
    ArrayList<String> allog = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allog);
        al_log = ((MainActivity)MainActivity.context_main).al_log;

        for(int i =0; i<al_log.size(); i++){
            allog.add(al_log.get(i));
        }

        listView.setAdapter(adpater);
    }

}
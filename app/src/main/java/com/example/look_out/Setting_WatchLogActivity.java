package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Setting_WatchLogActivity extends AppCompatActivity {
    private ImageView backButton;
    private ListView listView;
    private Button resetButton;
    private TextView status;
    ArrayList<String> al_log = new ArrayList<>();
    ArrayList<String> allog = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";

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

        ArrayAdapter<String> adpater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allog){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.rgb(230,230,230));
                return view;
            }
        };
        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        status = (TextView)findViewById(R.id.status);

        if(al_log.size() == 0){
            status.setText("보여줄 로그가 없습니다.");
        }
        else{
            allog.clear();
            for (int i = al_log.size() - 1; i >= 0; i--) {
                allog.add(al_log.get(i));
            }
            listView.setAdapter(adpater);
        }

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allog.clear();
                al_log.clear();

                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
                status.setText("보여줄 로그가 없습니다.");

                adpater.notifyDataSetChanged();
            }
        });
    }//end of onCreate

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_WatchLogActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}//end of class

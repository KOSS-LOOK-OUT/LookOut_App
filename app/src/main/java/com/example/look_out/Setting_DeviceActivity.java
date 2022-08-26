package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Setting_DeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button addButton;
    private Button deleteButton;
    private ListView listView;
    ArrayList<String> save_device = new ArrayList<>();
    ArrayList<String> savedevice = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);
        save_device.clear();

        save_device = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_DeviceActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_DeviceActivity.this, Setting_AddDeviceActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adpater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, savedevice){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.rgb(230,230,230));
                return view;
            }
        };

        try {
            save_device = ((Setting_AddDeviceActivity) Setting_AddDeviceActivity.context_main).save_device;
        }
        catch(Exception e){
        }

        savedevice.clear();
        for(int i = 0; i < save_device.size(); i++){
            if(!savedevice.contains(save_device.get(i))){
                savedevice.add("디바이스 키 : " + save_device.get(i) + "state : true");
            }
        }

        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, save_device);
        listView.setAdapter(adpater);


        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int count = adpater.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        savedevice.remove(i);
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(save_device.get(i) + "/state");
                        ref.setValue(false);
                        save_device.remove(i);
                    }
                }
                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, save_device);
                // 모든 선택 상태 초기화.
                listView.clearChoices() ;

                adpater.notifyDataSetChanged();
            }
        }) ;

    }//end of onCreate

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

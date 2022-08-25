package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Setting_AddDeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button sendButton;
    private EditText deviceAddEdit;
    public static Context context_main;
    String key;
    ArrayList<String> device_key = new ArrayList<>();
    ArrayList<String> save_device = new ArrayList<>();
    ArrayList<String> savedevice = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device_add);

        context_main = this;

        save_device = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);

        device_key = ((MainActivity)MainActivity.context_main).device_key;

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_AddDeviceActivity.this, Setting_DeviceActivity.class);
                startActivity(intent);
            }
        });

        deviceAddEdit = (EditText) findViewById(R.id.deviceAddEdit);

        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = deviceAddEdit.getText().toString();

                if (device_key.contains(key)) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(key + "/state");
                    DatabaseReference ref2 = database.getReference(key + "/id");
                    ref.setValue(true);
                    Toast.makeText(getApplicationContext(), "디바이스 추가에 성공 했습니다!", Toast.LENGTH_LONG).show();
                    save_device.add("디바이스 키 : " + key + " state : true");
                    setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, save_device);
                }
                else{
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 확인해 주세요.", Toast.LENGTH_LONG).show();
                    key = "";
                }

            }
        });

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

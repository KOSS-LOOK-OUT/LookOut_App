package com.example.look_out;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Setting_AddDeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button sendButton;
    private EditText deviceAddEdit;
    private EditText deviceAddId;
    public static Context context_main;
    String key;
    String nickname;
    ArrayList<String> device_key = new ArrayList<>();
    ArrayList<String> device_uuid = new ArrayList<>();
    ArrayList<String> device_nickname = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";
    private static final String SETTINGS_PLAYER_JSON3 = "settings_item_json3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device_add);

        context_main = this;

        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);
        device_nickname = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3);
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
        deviceAddId = (EditText) findViewById(R.id.deviceAddId);

        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = deviceAddEdit.getText().toString();
                nickname = deviceAddId.getText().toString();

                if (device_nickname.contains(nickname)){
                    Toast.makeText(getApplicationContext(), "중복된 이름입니다!", Toast.LENGTH_SHORT).show();
                    deviceAddId.setText("");
                    deviceAddEdit.setText("");
                }
                else if (device_key.contains(key)) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(key);
                    ref.child("/state").setValue(true);
                    Toast.makeText(getApplicationContext(), "디바이스 추가에 성공 했습니다!", Toast.LENGTH_SHORT).show();
                    device_nickname.add(nickname);
                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(!snapshot.getValue().toString().equals("true")){
                                device_uuid.add(snapshot.getValue().toString());
                            }
                            System.out.println(device_uuid);
                            setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, device_uuid);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3, device_nickname);
                    deviceAddEdit.setText("");
                    deviceAddId.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 확인해 주세요.", Toast.LENGTH_LONG).show();
                    key = "";
                    deviceAddEdit.setText("");
                }
            }
        });
    }//end of onCreate

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_AddDeviceActivity.this, SettingActivity.class);
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
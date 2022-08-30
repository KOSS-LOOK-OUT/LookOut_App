package com.example.look_out;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
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
    private ImageView home;
    private Button sendButton;
    private EditText deviceAddEdit;
    private EditText deviceAddId;

    /**
     * 다른 액티비티에서 이 액티비티의 변수와 정보들을 참조하기 위해 만든 변수
     */
    public static Context context_main;
    String key;
    String nickname;
    ArrayList<String> device_key = new ArrayList<>();
    ArrayList<String> device_uuid = new ArrayList<>();
    ArrayList<String> device_nickname = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";
    private static final String SETTINGS_PLAYER_JSON3 = "settings_item_json3";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device_add);

        context_main = this;

        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);
        device_nickname = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3);
        device_key = ((MainActivity)MainActivity.context_main).device_key;

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_AddDeviceActivity.this, MainActivity.class);
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
                } else if(nickname.equals("")){
                    Toast.makeText(getApplicationContext(), "디바이스 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    deviceAddId.setText("");
                    deviceAddEdit.setText("");
                } else if (device_key.contains(key)) {
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
                                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, device_uuid);
                            }
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
                    deviceAddEdit.setText("");
                    deviceAddId.setText("");
                }
            }
        });
    }//end of onCreate

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_AddDeviceActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * ArrayList를 Json으로 변환하여 SharedPreferences에 String을 저장한다.
     * @param context 애플리케이션의 현재 상태
     * @param key SharedPreference를 보낼 key 값
     * @param values String 형태로 저장할 ArrayList
     */
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

    /**
     * SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환한다.
     * @param context 애플리케이션의 현재 상태
     * @param key SharedPreference를 받아올 key 값
     */
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

    /**
     * 이전버튼 무력화를 위한 함수
     * @param keycode 들어오는 키값(ex) 이전버튼, 숫자 키패드 등)
     * @param event 키를 누를시 실행 할 이벤트
     * @return true만 리턴하여 이전 버튼이 먹히지 않도록 한다
     */
    public boolean onKeyDown(int keycode, KeyEvent event) {
        return true;
    }

}//end of class
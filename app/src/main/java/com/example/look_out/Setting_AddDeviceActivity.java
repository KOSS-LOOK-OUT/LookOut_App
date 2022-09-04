package com.example.look_out;
/**
 * @filename MessageActivity.java
 * @author 김지윤
 * @author 이채영
 * @version 2.0
 * 비상 상황에 문자 전송을 하기 위한 창을 나타내는 클래스
 * 사용 방법:
 * 디바이스 이름과 인증번호를 입력한 후, 추가 버튼을 누르면 디바이스가 추가된다.
 * 존재하지 않는 인증번호를 입력하거나, 이름을 입력하지 않은 상태로 추가 버튼을 누를 시 사용자에게 연결할 수 없다는 안내 창과 함께 연결에 실패하게 된다.
 */
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
     * 디바이스의 인증번호
     */
    String key;

    /**
     * 사용자가 입력한 디바이스 이름
     */
    String nickname;

    /**
     * 디바이스 키를 모아둔 리스트
     */
    ArrayList<String> device_key = new ArrayList<>();

    /**
     * 디바이스의 uuid를 모아둔 리스트
     */
    ArrayList<String> device_uuid = new ArrayList<>();

    /**
     * 디바이스의 이름을 모아둔 리스트
     */
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

        /**
         * SharedPreferences의 키 값 SETTINGS_PLAYER_JSON2에 접근해 Json형식의 String을 읽어와 다시 ArrayList로 변환해 device_uuid에 저장한다.
         */
        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);

        /**
         * SharedPreferences의 키 값 SETTINGS_PLAYER_JSON3에 접근해 Json형식의 String을 읽어와 다시 ArrayList로 변환해 device_nickname에 저장한다.
         */
        device_nickname = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3);

        /**
         * MainActivity의 device_key 값을 가져온다.
         */
        device_key = ((MainActivity)MainActivity.context_main).device_key;

        /**
         * 메인 화면으로 넘어가기 위한 함수
         */
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
            /**
             * 전송버튼을 누르면 발생하는 이벤트
             * @param view 사용자가 클릭한 위젯이 들어가는 변수
             */
            @Override
            public void onClick(View view) {
                key = deviceAddEdit.getText().toString();
                nickname = deviceAddId.getText().toString();

                /**
                 * 사용자가 입력한 인증번호와 닉네임 값에 따라 알맞은 이벤트를 발생시킨다.
                 */
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

                        /**
                         * 데이터 베이스에 값이 추가될 때 실행되는 함수
                         * @param snapshot 하위 요소의 업데이트된 데이터 포함
                         * @param previousChildName 이전 자식 노드의 이름
                         */
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
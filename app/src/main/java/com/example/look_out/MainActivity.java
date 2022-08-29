package com.example.look_out;

/**
 *  @filename MainActivity.java
 *
 *  LookOut 앱이 실행되면 가장 먼저 실행됩니다.
 *  기본 화면이며 가장 많은 Activity들과 상호작용 합니다.
 *  디바이스가 등록되어 있으면 중앙의 로그의 애니메이션이 실행된다.
 *  디바이스가 등록되어 있지 않으면 애니메이션이 실행이 되지 않는다.
 *
 *  사용방법:
 *  설정 아이콘을 누르면 설정창으로 넘어갈 수 있다.
 *  뒤로 가기 버튼을 누르면 첫번째는 토스트메세지가 뜨고 한번 더 누르면 앱을 종료할 수 있다.
 *
 *  @author 김언지
 *  @author 김지윤
 *  @author 김준영
 *  @author 이채영
 *  @version 1.1
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Android 3.0(API Level 11) 버전부터 지원되는 ActionBar를 해당 버전 이하의 버전에도 적용할 수 있도록 지원해주는 AppCompatActivity를 확장해서 사용
 */

public class MainActivity extends AppCompatActivity {

    /**
     * 다른 Activity에서 MainActivity의 시스템 요소와 자원에 접근하기 위한 변수
     */
    public static Context context_main;
    private final long finishtimed = 1500;
    private long presstime = 0;
    private ImageView setting;
    private final int MY_PERMISSIONS_REQUEST = 1000;
    private ImageView iconCircle;
    private TextView statusMessage;
    String value;
    ArrayList<String> device_key = new ArrayList<>();
    ArrayList<String> al_log = new ArrayList<>();
    ArrayList<String> device_uuid = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * 화면 정의하는 용도로 많이 사용한다.
     *
     * @param savedInstanceState 비 영구적 동적 데이터
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /**
         * super호출을 통해 백그라운드에서 실행할 수 있게 해줌.
         */
        super.onCreate(savedInstanceState);


        /**
         * MainActivity.java와 activity_main.xml을 연결해줌.
         */
        setContentView(R.layout.activity_main);

        /**
         * 다른 Activity에서 context변수를 통해 MainActivity에 접근이 가능하게 함.
         */
        context_main = this;

        /**
         * SharedPreferences에 키값 SETTINGS_PLAYER_JSON2에 접근해서 Json 형식의 String을 읽어와 다시 ArrayList로 변환해 device_uuid에 저장함.
         */
        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);

        /**
         * SharedPreferences에 키값 SETTINGS_PLAYER_JSON에 접근해서 Json 형식의 String을 읽어와 다시 ArrayList로 변환해 al_log에 저장함.
         */
        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);


        /**
         * al_log의 크기가 50이 넘는다면 가장 최근에 저장된 50개 빼고 나머지 데이터들을 삭제함.
         * ArrayList의 데이터(al_log)를 Json형식으로 변환하여 1개의 String으로 만든 후 이를 SharedPreferences에 키 값 SETTINGS_PLAYER_JSON으로 저장함.
         */
        if(al_log.size() > 50) {
            for (int i = al_log.size() - 51; i >= 0; i--) {
                al_log.remove(i);
            }
            setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
        }

        /**
         * 데이터베이스에서 데이터를 읽어오기위해 DatabaseReference 인스턴스를 생성
         * 하위 이벤트를 수신 대기하는 addchildEventlistener 사용
         * onChildAdded()로 항목 목록을 검색하거나 항목 목록에 대한 추가를 수신 대기
         * onChildChanged()로 목록의 항목에 대한 변경사항을 수신 대기
         * 현재 연결되어 있는 firebase에 있는 모든 키값들을 가져와 device_key에 add
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                device_key.add(snapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                device_key.add(snapshot.getKey());
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

        /**
         * 등록되어 있는 디바이스의 id 값을 device_uuid 리스트를 통해 하나씩 get해와서 firebase에 접근함.
         * uuid/content에 있는 항목이 변경될 때마다 getValue()를 통해 value값을 가져옴.
         * 가져온 value값과 동록된 키워드인 "불이야", "도둑이야", "조심해"를 equals를 통해 비교함.
         * 일치한다면 현재시간과 value값을 로그로 저장하고 AlarmActivity를 실행함.
         */
        for(int i = 0; i<device_uuid.size(); i++){
            DatabaseReference ref2 = database.getReference(device_uuid.get(i) + "/content");
            ref2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                    value = dataSnapshot.getValue(String.class);

                    if ("불이야".equals(value)) {
                        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                        al_log.add(getTime() + " 불이야");
                        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
                        startActivity(intent);

                    } else if ("도둑이야".equals(value)) {
                        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                        al_log.add(getTime() + " 도둑이야");
                        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
                        startActivity(intent);

                    } else if ("조심해".equals(value)) {
                        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                        al_log.add(getTime() + " 조심해");
                        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
                        startActivity(intent);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }

        int permissionCall = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        int permissionSMS = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);

        if (permissionCall == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)|| ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);

            }
        }

        /**
         * activity_main.xml파일에서 id가 setting으로 설정된 View를 가져옴.
         * setting이 눌릴 때, MainActivity에서 SettingActivity로 이동.
         */
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        /**
         * activity_main.xml파일에서 id가 iconCircle과 statusMessage으로 설정된 View를 가져옴.
         * rotate_anim.xml 불러와 animation을 정의한다
         * 연결된 디바이스가 없으면 "연결된 디바이스가 없습니다." 문구를 띄윤다.
         * 연결된 디바이스가 한 개라도 있으면 애니메이션을 iconCircle을 돌아가게끔 애니메이션을 set 해주고 "위험한 소리를 감지하고 있습니다.."라는 문구를 띄워준다.
         */

        iconCircle = (ImageView)findViewById(R.id.iconCircle);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);

        statusMessage = (TextView) findViewById(R.id.statusMessage);

        if(device_uuid.isEmpty()){
            statusMessage.setText("연결된 디바이스가 없습니다.");
        } else{
            iconCircle.setAnimation(animation);
            statusMessage.setText("위험한 소리를 감지하고 있습니다..");
        }
    }//end of onCreate

    public void onRequestPermissionResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean check_result = true;
        //모든 퍼미션을 허용했는지 체크
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                check_result = false;
                break;
            }
        }
    }

    //뒤로가기 두 번 눌러서 종료하기
    public void onBackPressed () {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimed >= intervalTime) {
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 알림 로그에 저장하기 위해서 현재 시간을 "yyyy-MM-dd hh:mm:ss"로 표시하는 메소드
     */
    private String getTime () {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
    }


    /**
     * ArrayList를 Json으로 변환하여 SharedPreferences에 String을 저장하는 코드
     *
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
     * SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환하는 코드
     *
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
}//end of class


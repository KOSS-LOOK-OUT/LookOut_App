package com.example.look_out;

/**
 * @filename MainActivity.java
 * @author 이채영
 * @author 김지윤
 * @author 김언지
 * @version 1.1
 * 앱이 실행되면 처음으로 나오는 클래스
 * 사용 방법:
 * 이전 버튼을 한 번 누르면 한번 더 이전버튼을 누르면 앱이 종료된다는 메세지가 뜨고 1.5초안에 한 번 더 누르면 앱이 종료된다.
 * 설정 imageView를 누르면 설정창으로 넘어간다.
 * 등록해놓은 디바이스가 있다면, 중앙의 로그의 애니메이션이 실행되고 소리를 감지 중이라는 text가 뜨고 없다면, 연결된 디바이스가 없다는 text가 뜬다.
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

public class MainActivity extends AppCompatActivity {

    public static Context context_main;
    private final long finishtimed = 1500;
    private long presstime = 0;
    private ImageView setting;
    private final int MY_PERMISSIONS_REQUEST = 1000;
    private ImageView iconCircle;
    private TextView statusMessage;

    /**
     * 감지된 소리의 내용(불이야, 조심해, 도둑이야)
     */
    String value;

    /**
     * firebase에 등록된 인증번호와 키 값들의 리스트
     */
    ArrayList<String> device_key = new ArrayList<>();

    /**
     * 알림 기록을 저장하기 위한 리스트
     */
    ArrayList<String> al_log = new ArrayList<>();

    /**
     * 등록된 디바이스들의 uuid를 저장하기 위한 리스트
     */
    ArrayList<String> device_uuid = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * 화면 정의하는 용도로 많이 사용한다.
     * @param savedInstanceState 비 영구적 동적 데이터
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 다른 Activity에서 context변수를 통해 MainActivity에 접근이 가능하게 한다.
         */
        context_main = this;

        /**
         * SharedPreferences에 키값 SETTINGS_PLAYER_JSON2에 접근해서 Json 형식의 String을 읽어와 다시 ArrayList로 변환해 device_uuid에 저장한다.
         */
        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);

        /**
         * SharedPreferences에 키값 SETTINGS_PLAYER_JSON에 접근해서 Json 형식의 String을 읽어와 다시 ArrayList로 변환해 al_log에 저장한다.
         */
        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);


        /**
         * al_log의 크기가 50이 넘는다면 가장 최근에 저장된 50개 빼고 나머지 데이터들을 삭제한다.
         * ArrayList의 데이터(al_log)를 Json형식으로 변환하여 1개의 String으로 만든 후 이를 SharedPreferences에 키 값 SETTINGS_PLAYER_JSON으로 저장한다.
         */
        if(al_log.size() > 50) {
            for (int i = al_log.size() - 51; i >= 0; i--) {
                al_log.remove(i);
            }
            setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
        }

        /**
         * 데이터베이스에서 데이터를 읽어오기위해 DatabaseReference 인스턴스를 생성한다.
         * 하위 이벤트를 수신 대기하는 addchildEventlistener 사용한다.
         * onChildAdded()로 항목 목록을 검색하거나 항목 목록에 대한 추가를 수신 대기한다.
         * onChildChanged()로 목록의 항목에 대한 변경사항을 수신 대기한다.
         * 현재 연결되어 있는 firebase에 있는 모든 키값들을 가져와 device_key에 add한다.
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
         * 등록되어 있는 디바이스의 id 값을 device_uuid 리스트를 통해 하나씩 get해와서 firebase에 접근한다.
         * uuid/content에 있는 항목이 변경될 때마다 getValue()를 통해 value값을 가져온다.
         * 가져온 value값과 동록된 키워드인 "불이야", "도둑이야", "조심해"를 equals를 통해 비교한다.
         * 일치한다면 현재시간과 value값을 로그로 저장하고 AlarmActivity를 실행한다.
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

        /**
         * 전화 걸기 권한 ID 가져오기
         */
        int permissionCall = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        /**
         * 문자 보내기 권한 ID 가져오기
         */
        int permissionSMS = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);

        /**
         * 전화 권한, 문자 권한 중 하나라도 허용되지 않았으면 실행
         * 사용자가 권한 요청을 명시적으로 거부하였는지 권한 요청을 처음 보거나 다시 묻지 묻지 않음으로 선택하였는지 확인한 후 전화 권한, 문자 권한 요청
         */
        if (permissionCall == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)|| ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);

            }
        }

        /**
         * activity_main.xml파일에서 id가 setting으로 설정된 View를 가져온다.
         * setting이 눌릴 때, MainActivity에서 SettingActivity로 이동한다.
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
         * activity_main.xml파일에서 id가 iconCircle과 statusMessage으로 설정된 View를 가져온다.
         * rotate_anim.xml 불러와 animation을 정의한다.
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

    /**
     * 권한 체크 이후 돌아가는 메서드로 모든 퍼미션을 허용했는지 체크한다.
     * @param requestCode 퍼미션 요청 코드로 어떤 권한 세트를 지정했는지 확인하기 위한 임의의 상수
     * @param permissions 요청한 권한 세트
     * @param grantResults 요청한 권한 세트의 허용, 거부 결과 세트로 허용, 거부 결과가 담긴 길이
     */
    public void onRequestPermissionResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean check_result = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                check_result = false;
                break;
            }
        }
    }

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
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
     * 알림 로그에 저장하기 위해서 현재 시간을 "yyyy-MM-dd hh:mm:ss"로 표시하는 메소드이다.
     */
    private String getTime () {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
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
}//end of class

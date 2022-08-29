package com.example.look_out;

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
    String value;
    ArrayList<String> device_key = new ArrayList<>();
    ArrayList<String> al_log = new ArrayList<>();
    ArrayList<String> device_uuid = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;


        device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);


        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        if(al_log.size() > 50) {
            for (int i = al_log.size() - 51; i >= 0; i--) {
                al_log.remove(i);
            }
            setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
        }

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
         */
        if (permissionCall == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
            /**
             * 사용자가 권한 요청을 명시적으로 거부하였는지 권한 요청을 처음 보거나 다시 묻지 묻지 않음으로 선택하였는지 확인한 후 전화 권한, 문자 권한 요청
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)|| ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);

            }
        }

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

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
     * 권한 체크 이후 돌아가는 메서드로 모든 퍼미션을 허용했는지 체크
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

    // 현재 시간을 "yyyy-MM-dd hh:mm:ss"로 표시하는 메소드
    private String getTime () {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
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


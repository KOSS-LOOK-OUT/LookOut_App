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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Context context_main;
    private final long finishtimed = 1500;
    private long presstime = 0;
    private ImageView setting;
    private final int MY_PERMISSIONS_REQUEST = 1000;
    private ImageView iconCircle;
    String value;
    ArrayList<String> al_log = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;

        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("device_1/content");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                value = dataSnapshot.getValue(String.class);
                System.out.println("childadded:" + value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                value = dataSnapshot.getValue(String.class);
                System.out.println(value);
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

        int permissionCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int permissionSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCall == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "전화&문자 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
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
    }

        public void onRequestPermissionResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            if (requestCode == 1000) {
                boolean check_result = true;
                // 모든 퍼미션을 허용했는지 체크
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        check_result = false;
                        break;
                    }
                }
                // 권한 체크에 동의를 하지 않으면 안드로이드 종료
                if (check_result == true) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_LONG).show();
                    finish();
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

}


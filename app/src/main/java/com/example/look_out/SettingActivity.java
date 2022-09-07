package com.example.look_out;

/**
 * @filename SettingActivity.java
 * @author 김지윤
 * @author 이채영
 * @version 2.0
 * 문자 기본값, 알림, 디바이스 설정하고 알림 기록을 볼 수 있는 클래스
 * 사용 방법:
 * 이전 버튼을 누르거나 내장된 이전 버튼을 누르면 메인창으로 돌아간다.
 * 각각의 설정에 대한 imageButton을 누르면 그에 맞는 설정창으로 이동한다.
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity {
    private ImageView backButton;
    private ImageButton messagedefault;
    private ImageButton settingalarm;
    private ImageButton settingdevice;
    private ImageButton watchlog;

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /**
         * 각각의 설정창으로 넘어가기 위한 클릭 이벤트
        */
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        messagedefault = findViewById(R.id.messagedefault);
        messagedefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, Message_DefaultActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        settingalarm = findViewById(R.id.settingalarm);
        settingalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, Setting_AlarmActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        settingdevice = findViewById(R.id.settingdevice);
        settingdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_DeviceActivity.class);
                startActivity(intent);
            }
        });

        watchlog = findViewById(R.id.watchlog);
        watchlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_WatchLogActivity.class);
                startActivity(intent);
            }
        });
    }//end of onCreate

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}//end of class

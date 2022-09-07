package com.example.look_out;
/**
 *@filename MessageActivity.java
 *@author 이채영
 *@author 김지윤
 *@version 2.0
 *알림의 진동 유무를 결정하기 위한 클래스
 *사용 방법:
 *스위치를 켜면 알림이 울릴 때 진동이 울리도록 한다.
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class Setting_AlarmActivity extends AppCompatActivity {
    private ImageView backButton;
    private TextView vibrationinform;

    /**
     * 알림 진동 설정을 위한 변수
     */
    static Switch Sound;

    /**
     * 저장할 파일 이름
     */
    String shared = "file";

    /**
     * 알림 진동 설정을 위한 변수
     */
    Boolean sound;

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_AlarmActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        /**
         * sound switch가 켜져있을 경우 진동이 울리도록 한다.
         */
        Sound = findViewById(R.id.Sound);
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        sound = sharedPreferences.getBoolean("switch", true);
        Sound.setChecked(sound);

        vibrationinform = (TextView) findViewById(R.id.vibrationinform);
        if(Sound.isChecked()){
            vibrationinform.setText("무음모드에서도 진동이 울립니다.");
        } else {
            vibrationinform.setText("무음모드에서는 진동이 울리지 않습니다.");
        }

        Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            /**
             * 스위치값에 따라 진동 유무를 AlarmAcitivty에 전송하는 함수
             * 스위치 값이 바뀔 경우 바뀐 상태를 AlarmAcitivity에 전송하고 커밋한다.
             * 또한 바뀐 스위치값에 따라 설정 정보를 수정한다.
             * @param buttonView 연결 된 스위치값
             * @param isChecked 받아온 스위치의 상태
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("switch", Sound.isChecked());
                editor.commit();
                if(isChecked){
                    vibrationinform.setText("무음모드에서도 진동이 울립니다.");
                } else{
                    vibrationinform.setText("무음모드에서는 진동이 울리지 않습니다.");
                }
            }
        });
    }

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_AlarmActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}//end of class

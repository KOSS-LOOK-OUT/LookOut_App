package com.example.look_out;

/**
 * @filename Message_DefaultActivity.java
 * @author 이채영
 * @author 김지윤
 * @author 김언지
 * @version 2.0
 * 비상 상황에 보낼 문자의 기본값을 설정할 수 있는 클래스
 * 사용 방법:
 * 이전 버튼을 누르거나 내장된 이전 버튼을 누르면 설정 창으로 돌아간다.
 * 기본값을 입력하고 저장을 누르면 비상상황에 문자를 보낼 때 자동으로 작성되어 있다.
 * 초기화를 누르면 입력했던 내용을 한번에 지울 수 있다.
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Message_DefaultActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button saveButton;
    private Button resetButton;
    private EditText et_setting;

    /**
     * 문자의 기본 값을 저장하기 위한 변수
     */
    String message = "default";

    /**
     * 저장할 파일 이름
     */
    String shared = "file";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_default);

        et_setting = (EditText)findViewById(R.id.et_setting);

        /**
         * shared는 파일이름 설정한 것이고 0은 읽고 쓰기가 가능한 모드이다.
         * 키 값을 message로 한 preferences에서 값을 get 해온다.
         * 가져온 값을 EditText칸에 set 해준다.
         */
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        message = sharedPreferences.getString("message", "");
        et_setting.setText(message);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 이전 버튼을 누르면 설정 창으로 이동한다.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Message_DefaultActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 초기화 버튼을 누르면 입력하고 있었던 값을 지우고 토스트 메세지를 보여준다.
             */
            @Override
            public void onClick(View view) {
                et_setting.setText("");
                Toast.makeText(getApplicationContext(), "초기화되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 저장 버튼을 누르면 문구를 입력했는지 안했는지 확인을 한다.
             * 문구를 입력하지 않았다면 입력하라는 토스트 메시지를 보여준다.
             * 문구를 입력했다면 editor를 preferences에 쓰겠다고 연결한다.
             * 키 값을 message로 한 preferences에 값을 put 한다.
             * commit 해서 저장하고 토스트 메세지를 보여준다.
             */
            @Override
            public void onClick(View view) {
                message = et_setting.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("message", message);
                editor.commit();
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
    }//end of onCreate

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Message_DefaultActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}//end of class

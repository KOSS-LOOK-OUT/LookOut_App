package com.example.look_out;

/**
 * @filename MainActivity.java
 * @author 이채영
 * @author 김지윤
 * @author 김언지
 * @version 2.0
 * 화면상에 감지된 소리의 알림창을 띄우며 메시지 보내기 버튼과 전화 걸기 버튼, 알림 끄기 버튼을 구현하기 위한 클래스
 * 사용 방법:
 * 특정 위험 소리가 감지되면("불이야", "도둑이야", "조심해") 해당 소리가 감지되었다는 알림창이 뜨고 사용자의 설정에 따라 진동이 온다. (기본 값: 진동 true)
 * 'ooo로 전화' 버튼을 누르면 응급 번호(119 또는 112)로 전화가 걸리며 알림이 멈춘다.
 * 'ooo로 문자' 버튼을 누르면 메세지 전송을 위한 엑티비티로 이동하며 알림이 멈춘다.
 * '알림 끄기' 버튼을 누르면 진동이 멈추며 메인 엑티비티로 창이 이동한다.
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {
    private Button callButton;
    private Button offButton;
    private Button messageButton;
    private TextView textView;

    /**
     * 알림 진동 설정을 위한 변수
     */
    Boolean sound;

    /**
     * 감지된 소리의 내용(불이야, 조심해, 도둑이야)
     */
    String value;

    /**
     * 문자를 연결할 전화번호
     */
    String phoneNo;

    /**
     * 저장할 파일 이름
     */
    String shared = "file";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @params savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        /**
         * MainActivity에서 firebase를 통해 받아온 "불이야", "도둑이야", "조심해" 키워드 값을 저장한다.
         */
        value = getIntent().getStringExtra("value");

        callButton = (Button) findViewById(R.id.callButton);
        messageButton = (Button) findViewById(R.id.messageButton);

        /**
         * "도둑이야" 소리가 감지되면 112로 연결한다.
         * "불이야", "조심해" 소리가 감지되면 119로 연결한다.
         */
        if ("도둑이야".equals(value)) {
            phoneNo = "112";
            callButton.setText("112 전화");
            messageButton.setText("112 문자");
        } else {
            phoneNo = "119";
            callButton.setText("119 전화");
            messageButton.setText("119 문자");
        }

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("\"" + value + "\" 소리가\n 감지되었습니다!");

        /**
         * sharedPreference를 사용해 사용자가 설정한 값에 따라 진동 유무를 지정한다.
         */
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        sound = sharedPreferences.getBoolean("switch", true);

        /**
         * 소리 감지시 디바이스에 울리는 진동을 설정한다.
         */
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (sound) {
            vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0); // 무한 진동
        }

        /**
         * 각각의 버튼을 누르면 진동을 멈추고 call은 전화 창으로 message는 메세지 창으로 off는 메인 창으로 이동한다.
         * @param view View에서 클릭되었을 때 정의된 이름의 함수를 호출
         */
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Context c = view.getContext();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));

                try {
                    c.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        offButton = findViewById(R.id.offButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(AlarmActivity.this, MessageActivity.class);
                intent.putExtra("phoneNo", phoneNo);
                startActivity(intent); //엑티비티 이동
            }
        });
    }//end of onCreate

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

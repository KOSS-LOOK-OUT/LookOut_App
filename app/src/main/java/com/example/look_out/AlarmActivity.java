package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 화면상에 감지된 소리의 알림창을 띄우며 메시지 보내기 버튼과 전화 걸기 버튼, 알림 끄기 버튼을 구현하기 위한 클래스
 * 사용 방법:
 * 특정 위험 소리가 감지되면("불이야", "도둑이야", "조심해") 해당 소리가 감지되었다는 알림창이 뜨며 디바이스에 진동이 울림
 * call 버튼을 누르면 응급 번호(119, 112)로 전화가 걸리며 진동이 멈춤
 * message 버튼을 누르면 응급 번호(119, 112)로 문자가 전송되는 창으로 엑티비티가 이동하며 진동이 멈춤
 * off 버튼을 누르면 진동이 멈추며 메인 엑티비티로 창이 이동함
 *
 * @author 김언지
 * @author 김지윤
 * @version 1.0
 * @see     com.example.look_out.Message_DefaultActivity
 * @see     com.example.look_out.MessageActivity
 */
public class AlarmActivity extends AppCompatActivity {
    private Button callButton;
    private Button offButton;
    private Button messageButton;
    /**
     * 소리가 감지 되었는지에 대한 여부
     */
    Boolean sound;
    /**
     * DB에서 들어온 감지된 소리
     */
    String value;
    private TextView textView;
    private ImageView imageView;

    /**
     * 엑티비티 생성 단계에서 한번 실행되는 메서드
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        value = ((MainActivity) MainActivity.context_main).value;
        callButton = (Button) findViewById(R.id.callButton);
        messageButton = (Button) findViewById(R.id.messageButton);

        /**
         * "도둑이야" 소리가 감지되면 112로 연결
         * "불이야", "조심해" 소리가 감지되면 119로 연결
         */
        if ("도둑이야".equals(value)) {
            callButton.setText("112 전화");
            messageButton.setText("112 문자");
        } else {
            callButton.setText("119 전화");
            messageButton.setText("119 문자");
        }


        textView = (TextView) findViewById(R.id.textView);
        textView.setText("\"" + value + "\" 소리가\n 감지되었습니다!");

        try {
            sound = ((Setting_AlarmActivity) Setting_AlarmActivity.context_main).sound;
        } catch (Exception e) {
            sound = false;
        }
        /**
         * 소리 감지시 디바이스에 울리는 진동
         */
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (sound) {
            vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0); // 무한 진동
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            /**
             * "도둑이야" 소리가 감지되면 112로 전화 버튼이 연결
             * "불이야", "조심해" 소리가 감지되면 119로 전화 버튼이 연결
             * @param view View에서 클릭되었을 때 정의된 이름의 함수를 호출
             */
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                /**
                 * 전화 걸리는 번호
                 */
                String phoneNo = "119";
                if("도둑이야".equals(value)){ phoneNo = "112";}
                
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
            /**
             * 소리가 감지되었을 때 알림 화면을 끄고 메인 화면으로 돌아가는 메서드
             * @param view View에서 클릭되었을 때 정의된 이름의 함수를 호출
             */
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * message 버튼을 누르면 메시지 보내는 창으로 넘어가는 메서드
             * @param view View에서 클릭되었을 때 정의된 이름의 함수를 호출
             */
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(AlarmActivity.this, MessageActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });
    }

    /**
     *
     * @param keycode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keycode, KeyEvent event) {
        return true;
    }
}

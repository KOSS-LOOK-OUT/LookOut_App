package com.example.look_out;
/**
 * @filename MessageActivity.java
 * @author 이채영
 * @version 2.0
 * 앱 실행시 시작 화면을 잠깐 보여주기 위한 클래스
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fluter);
        moveMain(1);
    }//end of onCreate

    /**
     * 특정 시간만큼 시작 화면을 보여주고 메인으로 넘어가기 위한 함수
     * @param sec 창을 보여주는 시간(sec초만큼 보여준다)
     */
    private void moveMain(int sec) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000 * sec);
    }
}//end of class

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


public class AlarmActivity extends AppCompatActivity {
    private Button callButton;
    private Button offButton;
    private Button messageButton;
    Boolean sound;
    String value;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        value = ((MainActivity) MainActivity.context_main).value;
        callButton = (Button) findViewById(R.id.callButton);
        messageButton = (Button) findViewById(R.id.messageButton);

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
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (sound) {
            vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0); // 무한 진동
        }


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
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
                startActivity(intent); //엑티비티 이동
            }
        });
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        return true;
    }
}

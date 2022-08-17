package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class Alarm_thiefActivity extends AppCompatActivity {

    private Button callButton;
    private Button offButton;
    private Button messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_thief);

        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{100,1000,100,500,100,500,100,1000}, 0); // 무한 진동

        callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                String phoneNo = "01022689761";

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
                Intent intent = new Intent(Alarm_thiefActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        messageButton = findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(Alarm_thiefActivity.this, Message_112Activity.class);
                startActivity(intent); //엑티비티 이동
            }
        });
    }
}

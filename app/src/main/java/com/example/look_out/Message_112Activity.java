package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Message_112Activity extends AppCompatActivity {

    private ImageView home;
    private EditText et_default;
    private Button sendButton_112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_112);

        String message = ((Message_DefaultActivity)Message_DefaultActivity.context_main).message;
        et_default = (EditText)findViewById(R.id.et_default);
        et_default.setText(message);

        sendButton_112 = (Button) findViewById(R.id.sendButton_119);
        sendButton_112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = "112";
                String text = message;

                sendSMS(phoneNo, text);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_112Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendSMS(String phoneNumber, String message_112) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message_112, null, null);
            Toast.makeText(getApplicationContext(), "SMS succeed!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

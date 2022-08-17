package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Message_119Activity extends AppCompatActivity {

    private ImageView home;
    private EditText et_default;
    private Button sendButton_119;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_119);

        try {
            message = ((Message_DefaultActivity)Message_DefaultActivity.context_main).message;
        } catch (Exception e) {
            message = "";
        }

        et_default = (EditText)findViewById(R.id.et_default);
        et_default.setText(message);


        sendButton_119 = (Button) findViewById(R.id.sendButton_119);
        sendButton_119.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = "01022689761";
                String text = et_default.getText().toString();

                sendSMS(phoneNo, text);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_119Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendSMS(String phoneNumber, String message_119) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message_119, null, null);
            Toast.makeText(getApplicationContext(), "SMS succeed!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

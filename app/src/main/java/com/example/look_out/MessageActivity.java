package com.example.look_out;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {

    private ImageView home;
    private EditText et_default;
    private Button sendButton;
    private TextView tv;
    String message;
    String value;
    String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        value = ((MainActivity)MainActivity.context_main).value;

        try {
            message = ((Message_DefaultActivity)Message_DefaultActivity.context_main).message;
        } catch (Exception e) {
            message = "";
        }

        if ("도둑이야".equals(value)){
            phoneNo = "112";
        }
        else {
            phoneNo = "119";
        }

        et_default = (EditText)findViewById(R.id.et_default);
        et_default.setHint(phoneNo + "에 전송할 문자를 입력해주세요.");
        et_default.setText(message);
        et_default.bringToFront();

        tv = (TextView) findViewById(R.id.tv);
        tv.setText(phoneNo +  " 문자 전송");

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setText(phoneNo + " 전송");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_default.getText().toString();
                sendSMS(phoneNo, text);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainDialog();
            }
        });
    }//end of onCreate

    public void goMainDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인").setMessage("메인 화면으로 돌아가시겠습니까?");

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setPositiveButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//end of OnClickHandler

    public boolean onKeyDown(int keycode, KeyEvent event) {
        return true;
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

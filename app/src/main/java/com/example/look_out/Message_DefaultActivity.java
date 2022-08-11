package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Message_DefaultActivity extends AppCompatActivity {

    private ImageView home;
    private Button cancelButton;
    private Button resetButton;
    EditText et_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_default);

        et_setting = (EditText)findViewById(R.id.et_setting);

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_DefaultActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_DefaultActivity.this, MainActivity.class);
                startActivity(intent); //엑티비티 이동
            }
        });

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_setting.setText("");
                Toast.makeText(getApplicationContext(), "초기화되었습니다!", Toast.LENGTH_LONG).show();
            }
        });

    }
}

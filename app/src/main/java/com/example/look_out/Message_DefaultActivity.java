package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Message_DefaultActivity extends AppCompatActivity {

    public static Context context_main;
    private ImageView backButton;
    private Button saveButton;
    private Button resetButton;
    String message = "default";
    EditText et_setting;
    String shared = "file";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_default);

        et_setting = (EditText)findViewById(R.id.et_setting);

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        message = sharedPreferences.getString("message", "");
        et_setting.setText(message);

        context_main = this;

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Message_DefaultActivity.this, SettingActivity.class);
                startActivity(intent);
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

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = et_setting.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("message", message);
                editor.commit();
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Message_DefaultActivity.this, SettingActivity.class);
        startActivity(intent);
    }

}

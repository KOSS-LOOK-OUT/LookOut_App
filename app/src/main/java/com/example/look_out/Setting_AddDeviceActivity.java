package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Setting_AddDeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button sendButton;
    private EditText deviceAddEdit;
    String key;
    ArrayList<String> device_key = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device_add);

        device_key = ((MainActivity)MainActivity.context_main).device_key;

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_AddDeviceActivity.this, Setting_DeviceActivity.class);
                startActivity(intent);
            }
        });

        deviceAddEdit = (EditText) findViewById(R.id.deviceAddEdit);

        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = deviceAddEdit.getText().toString();

                if (device_key.contains(key)) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(key + "/state");
                    ref.setValue(true);
                    Toast.makeText(getApplicationContext(), "디바이스 추가에 성공 했습니다!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 확인해 주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }//end of onCreate

}//end of class

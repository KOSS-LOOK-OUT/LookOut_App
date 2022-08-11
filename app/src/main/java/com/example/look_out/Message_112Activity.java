package com.example.look_out;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Message_112Activity extends AppCompatActivity {

    private ImageView home;
    private EditText et_default;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_112);

        String message = ((Message_DefaultActivity)Message_DefaultActivity.context_main).message;
        et_default = (EditText)findViewById(R.id.et_default);
        et_default.setText(message);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message_112Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

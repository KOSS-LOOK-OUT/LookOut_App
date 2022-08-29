package com.example.look_out;
/**
 * @filename MessageActivity.java
 * @author 이채영
 * @author 김지윤
 * @author 김언지
 * @version 1.1
 * 비상 상황에 문자 전송을 하기 위한 창을 나타내는 클래스
 * 사용 방법:
 * 이전 버튼을 누르면 정말로 홈화면으로 돌아갈 것인지 물어보는 창이 나오고 '돌아가기'를 누르면 다시 메인창으로 돌아갑니다.
 * 메세지창에는 기본값 설정 창(Message_DefaultActivity)에서 미리 입력해놓은 문구가 설정되어 있습니다.
 * 전송버튼을 누르면 지정해놓은 번호에 문자가 전송됩니다.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
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
    /**
     * 입력 될 메세지 내용
     */
    String message;

    /**
     * 감지된 소리의 내용(불이야, 조심해, 도둑이야)
     */
    String value;

    /**
     * 문자를 연결할 전화번호
     */
    String phoneNo;

    /**
     * @param savedInstanceState 비 영구적 동적 데이터
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        value = ((MainActivity)MainActivity.context_main).value;

        /**
         * @throws NullPointerException 기본값을 설정해놓지 않으면 발생한다
         */
        try {
            message = ((Message_DefaultActivity)Message_DefaultActivity.context_main).message;
        } catch (NullPointerException e) {
            message = "";
        }

        /**
         * value에 들어온 값에 따라 문자를 전송할 번호를 지정한다.
         */
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

        /**
         * sendButton을 누르면 et_default에 입력되어 있는 문장을 지정한 번호로 전송한다.
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_default.getText().toString();
                sendSMS(phoneNo, text);
            }
        });

        /**
         * home을 누르면 메인으로 돌아갈지 확인 후 돌아간다.
         */
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainDialog();
            }
        });
    }//end of onCreate

    /**
     * 메인창으로 돌아갈지 확인 후 선택에 맞게 처리.
     * 취소 버튼을 누른 경우 아무일도 일어나지 않고 다시 메세지 창으로 돌아간다.
     * 돌아가기 버튼을 누른 경우 메인 화면으로 돌아간다.
     */
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

    /**
     * 이전버튼 무력화를 위한 함수
     * @param keycode
     * @param event
     * @return true를 입력해서 이전 버튼이 먹히지 않도록 한다
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        return true;
    }

    /**
     *
     * @param phoneNumber
     * @param message_119
     */
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

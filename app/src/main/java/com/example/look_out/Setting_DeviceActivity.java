package com.example.look_out;
/**
 * @filename MessageActivity.java
 * @author 이채영
 * @author 김지윤
 * @version 2.0
 * 연결된 디바이스들을 보여주고 디바이스를 추가, 삭제 하는 클래스
 * 사용 방법:
 * 이전 버튼을 누르면 설정창으로 돌아간다.
 * '추가'를 누르면 디바이스 추가를 위한 Activity로 넘어간다.
 * 삭제하고 싶은 디바이스를 선택해 '삭제'를 누르면 정말 삭제할 것인지 묻고 삭제를 진행한다.
 * 단, 여기서 디바이스를 삭제하면 앱이 재실행된다.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class Setting_DeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button addButton;
    private Button deleteButton;
    private ListView listView;
    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";
    private static final String SETTINGS_PLAYER_JSON3 = "settings_item_json3";

    /**
     * 저장된 디바이스들을 보여주는 listView를 만들기 위한 리스트
     */
    ArrayList<String> savedevice = new ArrayList<>();

    /**
     * 연결된 디바이스를 저장하기 위한 리스트
     */
    ArrayList<String> device_uuid = new ArrayList<>();

    /**
     * listView에서 디바이스 이름을 보여주기 위한 리스트
     */
    ArrayList<String> device_nickname = new ArrayList<>();

    /**
     * listView에 추가 한 후 창에 띄우기 위한 ArrayAdapter
     */
    ArrayAdapter<String> adapter;

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);

        device_uuid = savedata.getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);
        device_nickname = savedata.getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_DeviceActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_DeviceActivity.this, Setting_AddDeviceActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, savedevice){
            /**
             * listView의 폰트 색상을 바꾸기 위해 쓰인 함수
             * @param position 각 value의 위치
             * @param convertView 실제 화면에 그려지는 아이템을 관리하는 배열
             * @param parent getView에 의해 접근될 view
             * @return 여러 설정을 거친 후 view값을 리턴한다
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.rgb(230,230,230));
                return view;
            }
        };

        savedevice.clear();
        for(int i = 0; i < device_nickname.size(); i++){
            savedevice.add("디바이스 이름: " + device_nickname.get(i));
        }
        listView.setAdapter(adapter);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                if(checkedItems.size() == 0){
                    Toast.makeText(getApplicationContext(), "삭제 할 디바이스를 선택해주세요.",Toast.LENGTH_SHORT).show();
                } else{
                    removeDeviceDialog();
                }
            }
        }) ;
    }//end of onCreate

    /**
     * 정말로 삭제할지 사용자에게 재차 확인하는 창을 띄우기 위한 함수
     * '취소' 버튼을 누르면 선택 표시를  초기화한다.
     * '삭제' 버튼을 누르면 체크한 디바이스들을 삭제한다.
     */
    public void removeDeviceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인").setMessage("선택한 디바이스를 삭제하시겠습니까? 삭제할 경우 앱이 재실행됩니다.");


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listView.clearChoices();
                adapter.notifyDataSetChanged();
            }
        });

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int count = adapter.getCount();

                /**
                 * device, uuid, nickname이 기록된 리스트에서 모두 삭제한다.
                 */
                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        savedevice.remove(i);
                        device_uuid.remove(i);
                        device_nickname.remove(i);
                    }
                }

                /**
                 * ArrayList의 데이터를 Json 형식으로 변환하여 1개의 String으로 만든 후 이를 SharedPreferences에 각각의 키 값으로 저장한다.
                 */
                savedata.setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, device_uuid);
                savedata.setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3, device_nickname);

                listView.clearChoices() ;
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "삭제되었습니다. 앱이 재실행됩니다.", Toast.LENGTH_SHORT).show();
                
                /**
                 * 삭제 후 앱을 재실행한다.
                 * 재실행 하기 전 약간의 딜레이를 주어 토스트 메세지를 띄운다.
                 */
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PackageManager packageManager = getPackageManager();
                        Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                        ComponentName componentName = intent.getComponent();
                        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                        startActivity(mainIntent);
                        System.exit(0);
                    }
                }, 200);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//end of removeDeviceDialog

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_DeviceActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}//end of class
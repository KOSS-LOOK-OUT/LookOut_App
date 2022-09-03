package com.example.look_out;
/**
 * @filename MessageActivity.java
 * @author 김지윤
 * @author 김언지
 * @author 이채영
 * @version 2.0
 * 알림창 기록을 보여주는 클래스
 * 사용 방법:
 * 이전 버튼을 누르면 설정창으로 넘어간다.
 * 초기화 버튼을 누르면 기록들이 모두 초기화된다.
 * 알림은 최대 50개까지 보이며 50개가 넘어갈 시 가장 오래된 로그부터 먼저 삭제 된다.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Setting_WatchLogActivity extends AppCompatActivity {
    private ImageView backButton;
    private ListView listView;
    private Button resetButton;
    private TextView status;
    ArrayAdapter<String> adapter;
    /**
     * 알림 기록을 저장하기 위한 리스트
     */
    ArrayList<String> al_log = new ArrayList<>();

    /**
     * 알림 기록을 리스트 뷰에 보여주기 위한 리스트
     */
    ArrayList<String> allog = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";

    /**
     * 필수 구현 요소
     * Activity가 생성될 때 실행된다.
     * @param savedInstanceState 엑티비티 이전 상태를 저장한 bundle 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_log);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_WatchLogActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allog){
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

        /**
         * SharedPreferences의 키값 SETTINGS_PLAYER_JSON에 접근해서 Json형식의 String을 읽어와 다시ArrayList로 변환해 al_log에 저장한다.
         */
        al_log = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);
        status = (TextView)findViewById(R.id.status);

        if(al_log.size() == 0){
            status.setText("보여줄 로그가 없습니다.");
        }
        else{
            allog.clear();
            for (int i = al_log.size() - 1; i >= 0; i--) {
                allog.add(al_log.get(i));
            }
            listView.setAdapter(adapter);
        }

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 로그 내용을 담은 리스트를 지운다.
             * @param view 사용자가 클릭한 위젯이 들어간다
             */
            @Override
            public void onClick(View view) {
                resetLogDialog();
            }
        });
    }//end of onCreate

    public void resetLogDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인").setMessage("알람 기록을 초기화하시겠습니까?");

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                allog.clear();
                al_log.clear();

                /**
                 * ArrayList의 데이터(al_log)를 Json형식으로 변환하여 1개의 String으로 만든 후 이를 SharedPreferences에 키 값 SETTINGS_PLAYER_JSON으로 저장한다.
                 */
                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, al_log);
                status.setText("보여줄 로그가 없습니다.");
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//end of resetLogDialog

    /**
     * 안드로이드 폰에 내장된 이전버튼을 눌렀을 경우 구조적으로 이전 activity인 창으로 넘어가게 한다.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_WatchLogActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * ArrayList를 Json으로 변환하여 SharedPreferences에 String을 저장한다.
     * @param context 애플리케이션의 현재 상태
     * @param key SharedPreference를 보낼 key 값
     * @param values String 형태로 저장할 ArrayList
     */
    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    /**
     * SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환한다.
     * @param context 애플리케이션의 현재 상태
     * @param key SharedPreference를 받아올 key 값
     */
    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}//end of class

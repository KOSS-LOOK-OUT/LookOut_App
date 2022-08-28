package com.example.look_out;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Setting_DeviceActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button addButton;
    private Button deleteButton;
    private ListView listView;
    ArrayList<String> savedevice = new ArrayList<>();
    ArrayList<String> device_uuid = new ArrayList<>();
    ArrayList<String> device_nickname = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private static final String SETTINGS_PLAYER_JSON2 = "settings_item_json2";
    private static final String SETTINGS_PLAYER_JSON3 = "settings_item_json3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);

        try {
            device_uuid = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2);
            device_nickname = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3);
        }catch(Exception e){
            e.printStackTrace();
        }

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
                    showDialog();
                }
            }
        }) ;

    }//end of onCreate

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인").setMessage("선택한 디바이스를 삭제하시겠습니까?");


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        savedevice.remove(i);
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference( device_uuid.get(i)+"/content/delete_state");
                        ref.setValue(false);
                        device_uuid.remove(i);
                        device_nickname.remove(i);
                    }
                }

                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON2, device_uuid);
                setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON3, device_nickname);
                // 모든 선택 상태 초기화.
                listView.clearChoices() ;

                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//end of OnClickHandler

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Setting_DeviceActivity.this, SettingActivity.class);
        startActivity(intent);
    }

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
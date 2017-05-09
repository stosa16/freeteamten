package at.sw2017.pocketdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

public class SettingScreen extends AppCompatActivity {


    UserSetting userSetting = null;
    DBUserSetting dbUserSetting = null;
    String saved_pin;
    String old_pin = "";
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        dbUserSetting = new DBUserSetting(SettingScreen.this);
        List<UserSetting> temp = dbUserSetting.getUserSetting(1);
        userSetting = temp.get(0);

        final EditText user_name =(EditText)findViewById(R.id.User_name_id);
        String saved_name = userSetting.getUserName();
        if(!saved_name.equals("")){
            user_name.setText(saved_name);
        }
        //Switch on/off
        final TextView tView = (TextView) findViewById(R.id.on_off);
        final Switch switch1 = (Switch) findViewById(R.id.switch1);

        if(userSetting.isPinActive().equals("1")){
            tView.setText("ON");
        }
        else{
            tView.setText("OFF");
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("switchkey", false);
        switch1.setChecked(silent);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on) {
                    userSetting.setPinActive("1");
                    dbUserSetting.update(userSetting, 1);
                    tView.setText("ON");

                } else {
                    userSetting.setPinActive("0");
                    tView.setText("OFF");
                    dbUserSetting.update(userSetting, 1);
                }
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", on);
                editor.commit();
            }
        });

        final Button save_button=(Button)findViewById(R.id.save);
        final Button cancel_button=(Button)findViewById(R.id.cancel);
        final Button edit_friends=(Button)findViewById(R.id.editFriends);
        final Button edit_categories=(Button)findViewById(R.id.editCategories);

        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                save_button.setBackgroundColor(Color.RED);
                //get user_name
                final EditText user_name =(EditText)findViewById(R.id.User_name_id);

                //get pin
                saved_pin = userSetting.getPin();
                final EditText old_pin_ = (EditText)findViewById(R.id.current_pin);
                old_pin = old_pin_.getText().toString();
                final EditText new_pin =(EditText)findViewById(R.id.new_pin1);
                final EditText repeat_pin =(EditText)findViewById(R.id.new_pin2);

                String name = user_name.getText().toString();
                String new_ = new_pin.getText().toString();
                String repeat_ = repeat_pin.getText().toString();

                boolean ret =checkInput.check_user_name(name, userSetting, user_name);
                if(ret){
                    user_name.setText(name);
                }
                checkInput.check_pin_deleted(old_pin, saved_pin, new_, repeat_, userSetting);

                ret = checkInput.check_old_pin(old_pin, saved_pin, new_, repeat_);
                if(!ret){
                    Toast.makeText(SettingScreen.this, "Current PIN is not correct",
                            Toast.LENGTH_LONG).show();
                    new_pin.setText("");
                    repeat_pin.setText("");
                    old_pin_.setText("");
                    save_button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
                    return;
                }

                boolean ret2 = checkInput.check_new_pin(new_, repeat_, userSetting);
                if(!ret2){
                    Toast.makeText(SettingScreen.this, "New PIN and Reapeated PIN are not the same",
                            Toast.LENGTH_LONG).show();
                    new_pin.setText("");
                    repeat_pin.setText("");
                    old_pin_.setText("");
                    save_button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
                    return;
                }

                dbUserSetting.update(userSetting, 1);
                Intent intent = new Intent(SettingScreen.this, StartScreen.class);
                startActivity(intent);
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancel_button.setBackgroundColor(Color.RED);
                Intent intent = new Intent(SettingScreen.this, StartScreen.class);
                startActivity(intent);
            }
        });

        edit_friends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit_friends.setBackgroundColor(Color.RED);

                //todo link to Edit friends screen
                Intent intent = new Intent(SettingScreen.this, StartScreen.class);
                startActivity(intent);
            }
        });

        edit_categories.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit_categories.setBackgroundColor(Color.RED);

                //todo link to Edit categories screen
                Intent intent = new Intent(SettingScreen.this, StartScreen.class);
                startActivity(intent);
            }
        });
    }
}

class checkInput{
    static boolean check_user_name(String name, UserSetting userSetting, EditText user_name){
        if(!name.equals("")){
            userSetting.setUserName(name);
            return true;
        }
        return false;
    }

    static void check_pin_deleted(String old_pin, String saved_pin, String new_, String repeat_, UserSetting userSetting){

        if(!old_pin.equals("")) {
            if (saved_pin.equals(old_pin)) {
                if (new_.equals("") && repeat_.equals("")) {
                    userSetting.setPin(new_);
                    return;
                }
            }
        }
    }

    static boolean check_old_pin(String old_pin, String saved_pin, String new_, String repeat_){
        if(!old_pin.equals("")) {
            if (!saved_pin.equals(old_pin)) {
                return false;
            }
        }

        if(!saved_pin.equals("")) {
            if(!new_.equals("") && !repeat_.equals("")) {
                if (old_pin.equals("") || !saved_pin.equals(old_pin)) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean check_new_pin(String new_, String repeat_, UserSetting userSetting){
        if(!new_.equals("") && !repeat_.equals("")) {
            if (!new_.equals(repeat_)) {
                return false;
            }
            userSetting.setPin(new_);
        }
        return true;
    }
}
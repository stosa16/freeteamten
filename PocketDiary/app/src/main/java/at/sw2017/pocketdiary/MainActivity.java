package at.sw2017.pocketdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.List;
import android.view.View;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

public class MainActivity extends AppCompatActivity {

    UserSetting userSetting = null;
    DBUserSetting dbUserSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("firstrun", true)) {
            DBHandler db = new DBHandler(this);
            dbUserSetting = new DBUserSetting(MainActivity.this);
            userSetting = new UserSetting("");
            userSetting.setUserName("Markus");
            userSetting.setPinActive("0");
            userSetting.setPin("");
            userSetting.setId(1);
            dbUserSetting.insert(userSetting);
            //todo: add here categories... first time

            prefs.edit().putBoolean("firstrun", false).commit();
        }

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try{
                    sleep(3000);
                    dbUserSetting = new DBUserSetting(MainActivity.this);
                    List<UserSetting> temp = dbUserSetting.getUserSetting(1);
                    userSetting = temp.get(0);

                    if(userSetting.isPinActive().equals("1") ){
                        Intent intent = new Intent(getApplicationContext(), PinScreen.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), StartScreen.class);
                        startActivity(intent);
                    }
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}

package at.sw2017.pocketdiary;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

public class PinScreen extends AppCompatActivity {

    UserSetting userSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_screen);

        final Button login_btn = (Button)findViewById(R.id.button);

        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login_btn.setBackgroundColor(Color.WHITE);
                DBUserSetting dbUserSetting = new DBUserSetting(PinScreen.this);
                List<UserSetting> temp = dbUserSetting.getUserSetting(1);
                userSetting = temp.get(0);
                //get pin
                String saved_pin = userSetting.getPin();

                final EditText entered_pin_ = (EditText)findViewById(R.id.editText);
                String entered_pin = entered_pin_.getText().toString();

                boolean ret = PinCheck.check_PIN(saved_pin, entered_pin);

                if(ret){
                    Intent intent = new Intent(PinScreen.this, StartScreen.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(PinScreen.this, "PIN is not correct",
                            Toast.LENGTH_LONG).show();
                    entered_pin_.setText("");
                    login_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
                }
            }
        });
    }
}

class PinCheck{
    static boolean check_PIN(String saved_pin, String entered_pin){
        if (!saved_pin.equals(entered_pin)) {
            return false;
        }
        return true;
    }
}

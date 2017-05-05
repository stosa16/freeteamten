package at.sw2017.pocketdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        //Switch on/off
        final TextView tView = (TextView) findViewById(R.id.on_off);
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on) {
                    //todo database insert
                    tView.setText("ON");
                } else {
                    //todo database insert
                    tView.setText("OFF");
                }
            }
        });

    }

}
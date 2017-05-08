package at.sw2017.pocketdiary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final ImageButton setting_button =(ImageButton) findViewById(R.id.settings);
        final Button create_entry_button =(Button) findViewById(R.id.create_entry);
        final Button review_button =(Button) findViewById(R.id.review);
        final Button statistic_button =(Button) findViewById(R.id.statistic);


        setting_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setting_button.setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(StartScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });

        create_entry_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                create_entry_button.setBackgroundColor(Color.WHITE);

                //todo: link to create entry screen

                Intent intent = new Intent(StartScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });

        review_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                review_button.setBackgroundColor(Color.WHITE);

                //todo: link to review screen
                Intent intent = new Intent(StartScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });

        statistic_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                statistic_button.setBackgroundColor(Color.WHITE);

                //todo: link to statistic screen
                Intent intent = new Intent(StartScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });
    }
}

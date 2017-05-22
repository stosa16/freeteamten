package at.sw2017.pocketdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final ImageButton setting_button =(ImageButton) findViewById(R.id.settings);
        final Button create_entry_button =(Button) findViewById(R.id.create_entry);
        final Button review_button =(Button) findViewById(R.id.review);
        final Button statistic_button =(Button) findViewById(R.id.statistic);
        final ImageButton picture_button = (ImageButton) findViewById(R.id.picture);

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
                Intent intent = new Intent(StartScreen.this, CreateEntryScreen.class);
                startActivity(intent);
            }
        });

        review_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                review_button.setBackgroundColor(Color.WHITE);

                //todo: link to review screen
                Intent intent = new Intent(StartScreen.this, ReviewActivity.class);
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

        picture_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                picture_button.setBackgroundColor(Color.WHITE);

                loadingPopup();
            }
        });
    }

    public void loadingPopup() {
        View menuItemView = findViewById(R.id.picture);
        PopupMenu popUpMenu = new PopupMenu(StartScreen.this, menuItemView);
        popUpMenu.getMenuInflater().inflate(R.menu.popup, popUpMenu.getMenu());
        /*popUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.came:
                        onMessengerClick(user);
                        break;

                    case R.id.:
                        onSkypeClick(user);
                        break;

                    case R.id.action_whatsapp:
                        onWhatsappClick(user);
                        break;

                    case R.id.action_email:
                        onEmailClick(user);
                        break;

                    default:
                        break;

                }
                return true;
            }
        });*/
        popUpMenu.show();
    }
}

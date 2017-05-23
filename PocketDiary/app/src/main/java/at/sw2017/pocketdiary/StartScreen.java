package at.sw2017.pocketdiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

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

                loadingPopup(v);
            }
        });
    }

    public void loadingPopup(final View v) {
        View view = findViewById(R.id.picture);
        PopupMenu menu = new PopupMenu(StartScreen.this, view);
        menu.inflate(R.menu.popup);
       // menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { });

        MenuPopupHelper menuHelper = new MenuPopupHelper(StartScreen.this, (MenuBuilder) menu.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:
                        Picture.pictureFromCamera(StartScreen.this);
                        break;

                    case R.id.action_gallery:
                        Picture.pictureFromGallery(StartScreen.this);
                        break;

                    default:

                }
                return true;
            }
        });
        menuHelper.show();
    }

}

class Picture{
    private static final int CAMERA_REQUEST = 1;
    private static final int WRITE_STORAGE_REQUEST = 2;

    static void pictureFromCamera(StartScreen activity) {
        System.out.println("camera");
        checkCameraPermissions(activity);
    }

    static void pictureFromGallery(StartScreen activity) {
        System.out.println("gallery");
        checkGalleryPermissions(activity);

    }

    static void checkGalleryPermissions(StartScreen activity){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST);
            } else {
                //initPictureButton(activity);
            }
        } else {
            //initPictureButton(activity);
        }
    }

    static void checkCameraPermissions(StartScreen activity){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
            } else {
                initPictureButton(activity);
            }
        } else {
            initPictureButton(activity);
        }
    }

    static void initPictureButton(final StartScreen activity) {
        ImageButton picture_button = (ImageButton) activity.findViewById(R.id.picture);
        picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(activity, camera_intent, CAMERA_REQUEST, Bundle.EMPTY);
            }
        });
        picture_button.performClick();
    }

}

package at.sw2017.pocketdiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBPicture;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class StartScreen extends AppCompatActivity {

    TextView badge_camera;
    Bitmap photo = null;
    private Picture entry_picture = null;
    private static final int CAMERA_REQUEST = 1;

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
                        handle_picture.pictureFromCamera(StartScreen.this);
                        break;

                    case R.id.action_gallery:
                        handle_picture.pictureFromGallery(StartScreen.this);
                        break;

                    default:

                }
                return true;
            }
        });
        menuHelper.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED || resultCode != RESULT_OK) {
            Toast.makeText(StartScreen.this, "Picture capturing aborted!",
                    Toast.LENGTH_LONG).show();
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out_stream);

            if (photo != null) {
                Calendar calendar = Calendar.getInstance();
                String pictureName = new SimpleDateFormat("yyyy-MM-dd/HHmmssSSS").format(calendar.getTime()) + ".jpeg";
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, pictureName, null);
                entry_picture = new Picture();
                entry_picture.setName(pictureName);
                entry_picture.setFilePath(path);
                DBUserSetting dbp = new DBUserSetting(this);
                long id = dbp.insertPic(entry_picture);
                entry_picture.setId((int) id);
                DBUserSetting dbUserSetting = new DBUserSetting(StartScreen.this);
                List<UserSetting> temp = dbUserSetting.getUserSetting(1);
                UserSetting userSetting = temp.get(0);

                userSetting.setFilePath(path);
                userSetting.setPicturename(pictureName);
                //dbUserSetting.update(userSetting, 1); //todo this is not working

                ImageView mImageView = (ImageView) findViewById(R.id.pictureField);
                mImageView.setImageBitmap(photo);
            }
        }
    }
}

class handle_picture{

    private static int PICK_IMAGE_REQUEST = 1;
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
                requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
                if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        activity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openGallery(activity);
                }
            } else {
                openGallery(activity);
            }
        } else {
            openGallery(activity);
        }
    }

    static void checkCameraPermissions(StartScreen activity){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
                if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        activity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    initPictureButton(activity);
                }
            } else {
                initPictureButton(activity);
            }
        } else {
            initPictureButton(activity);
        }
    }

    static void initPictureButton(final StartScreen activity) {
        Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(activity, camera_intent, CAMERA_REQUEST, Bundle.EMPTY);
    }

    static void openGallery(final StartScreen  activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(activity, intent, PICK_IMAGE_REQUEST, Bundle.EMPTY);
    }
}
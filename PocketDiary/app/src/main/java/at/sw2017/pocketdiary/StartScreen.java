package at.sw2017.pocketdiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class StartScreen extends AppCompatActivity {

    TextView badge_camera;
    Bitmap photo = null;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        DBUserSetting dbUserSetting = new DBUserSetting(StartScreen.this);
        List<UserSetting> temp = dbUserSetting.getUserSetting(1);
        UserSetting userSetting = temp.get(0);
        if (!userSetting.getFilePath().equals("")) {
            ImageView mImageView = (ImageView) findViewById(R.id.pictureField);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(userSetting.getFilePath()));
        }

        final ImageButton setting_button = (ImageButton) findViewById(R.id.settings);
        final Button create_entry_button = (Button) findViewById(R.id.create_entry);
        final Button review_button = (Button) findViewById(R.id.review);
        final Button statistic_button = (Button) findViewById(R.id.statistic);
        final ImageButton picture_button = (ImageButton) findViewById(R.id.picture);

        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_button.setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(StartScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });

        create_entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_entry_button.setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(StartScreen.this, CreateEntryScreen.class);
                intent.putExtra("entry_id", Integer.toString(0));
                startActivity(intent);
            }
        });

        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_button.setBackgroundColor(Color.WHITE);

                //todo: link to review screen
                Intent intent = new Intent(StartScreen.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        statistic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statistic_button.setBackgroundColor(Color.WHITE);

                //todo: link to statistic screen
                Intent intent = new Intent(StartScreen.this, StatisticScreenActivity.class);
                startActivity(intent);
            }
        });

        picture_button.setOnClickListener(new View.OnClickListener() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out_stream);

            if (photo != null) {
                Calendar calendar = Calendar.getInstance();
                String pictureName = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(calendar.getTime()) + ".jpg";
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, pictureName, null);

                DBUserSetting dbUserSetting = new DBUserSetting(StartScreen.this);
                List<UserSetting> temp = dbUserSetting.getUserSetting(1);
                UserSetting userSetting = temp.get(0);

                Uri test = Uri.parse(path);
                Cursor cursor = getContentResolver().query(test, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String absolute_path = cursor.getString(idx);

                userSetting.setFilePath(absolute_path);
                userSetting.setPicturename(pictureName);
                dbUserSetting.update(userSetting, 1);

                ImageView mImageView = (ImageView) findViewById(R.id.pictureField);
                mImageView.setImageBitmap(BitmapFactory.decodeFile(userSetting.getFilePath()));
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && null != data && data.getData() != null) {
            Uri imageURI = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.pictureField);

            DBUserSetting dbUserSetting = new DBUserSetting(StartScreen.this);
            List<UserSetting> temp = dbUserSetting.getUserSetting(1);
            UserSetting userSetting = temp.get(0);

            String absolute_path = "";

            Cursor cursor = getContentResolver().query(imageURI, null, null, null, null);
            cursor.moveToFirst();
            String documentID = cursor.getString(0);
            documentID = documentID.substring(documentID.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ?", new String[]{documentID}, null);

            cursor.moveToFirst();
            absolute_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

            userSetting.setFilePath(absolute_path);
            userSetting.setPicturename("Picture from Gallery");
            dbUserSetting.update(userSetting, 1);

            imageView.setImageURI(imageURI);
        } else if (resultCode == RESULT_CANCELED || resultCode != RESULT_OK) {
            Toast.makeText(StartScreen.this, "Picture capturing aborted!",
                    Toast.LENGTH_LONG).show();
        }
    }
}

class handle_picture {

    private static int PICK_IMAGE_REQUEST = 2;
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

    static void checkGalleryPermissions(StartScreen activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    static void checkCameraPermissions(StartScreen activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
                if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        activity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

    static void openGallery(final StartScreen activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(activity, intent, PICK_IMAGE_REQUEST, Bundle.EMPTY);
    }
}
package at.sw2017.pocketdiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBPicture;

public class PictureFullscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_fullscreen);

        Intent intent = getIntent();
        int picture_id = Integer.parseInt(intent.getStringExtra("picture_id"));
        DBPicture db_picture = new DBPicture(this);
        Picture picture = db_picture.getPicture(picture_id);

        ImageView image_view = (ImageView)findViewById(R.id.image_fullscreen);
        Bitmap bitmap = BitmapFactory.decodeFile(picture.getFilePath());
        image_view.setImageBitmap(bitmap);
    }
}

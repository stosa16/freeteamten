package at.sw2017.pocketdiary;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowPictureScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture_screen);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                ImageView mImageView = (ImageView) findViewById(R.id.picture_fullscreen);
                mImageView.setImageBitmap(BitmapFactory.decodeFile(extras.getString("entry_id")));
            }
        }
    }
}

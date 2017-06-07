package at.sw2017.pocketdiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Picture;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;
import static android.widget.ImageView.ScaleType.FIT_END;
import static android.widget.ImageView.ScaleType.FIT_XY;

public class ShowEntryScreen extends AppCompatActivity {

    private int entry_id;
    final int THUMBNAIL_SIZE = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entry_screen);
        Intent intent = getIntent();
        entry_id = Integer.parseInt(intent.getStringExtra("entry_id"));

        Entry entry = Helper.getEntryComplete(this, entry_id);
        TextView title = (TextView)findViewById(R.id.out_title);
        title.setText(entry.getTitle());
        TextView main_category = (TextView)findViewById(R.id.out_category);
        main_category.setText(entry.getMainCategory().getName());
        TextView sub_category = (TextView)findViewById(R.id.out_subcategory);
        sub_category.setText(entry.getSubCategory().getName());
        TextView date = (TextView)findViewById(R.id.out_date);
        if (entry.getDate() != null) {
            String date_string = new SimpleDateFormat("yyyy-MM-dd").format(entry.getDate());
            date.setText(date_string);
        } else {
            date.setText("");
        }
        TextView address = (TextView)findViewById(R.id.out_address);
        if (entry.getAddress() != null) {
            address.setText(entry.getAddress().getStreet() + ", " + entry.getAddress().getCity());
        }
        TextView description = (TextView)findViewById(R.id.out_description);
        if (entry.getDescription() != null) {
            description.setText(entry.getDescription());
        }

        if (entry.getPictures().size() > 0) {
            LinearLayout thumbnails = (LinearLayout)findViewById(R.id.thumbnails);
            int id_count = 0;
            for (final Picture picture : entry.getPictures()) {
                Bitmap bitmap = BitmapFactory.decodeFile(picture.getFilePath());
                if (bitmap == null) {
                    continue;
                }
                Float width = new Float(bitmap.getWidth());
                Float height = new Float(bitmap.getHeight());
                Float ratio = width/height;

                Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, (int)(THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);
                ImageView image_view = new ImageView(this);
                image_view.setId(id_count);
                image_view.setPadding(5, 5, 5, 5);
                image_view.setImageBitmap(thumbnail);
                image_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image_view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PictureFullscreen.class);
                        intent.putExtra("picture_id", Integer.toString(picture.getId()));
                        startActivity(intent);
                    }
                });
                thumbnails.addView(image_view);
                id_count++;
            }
        }
    }
}
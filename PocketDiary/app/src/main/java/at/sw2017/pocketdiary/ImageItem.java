package at.sw2017.pocketdiary;

import android.graphics.Bitmap;

/**
 * Created by schus on 05.06.2017.
 */

// Reference: http://stacktips.com/tutorials/android/android-gridview-example-building-image-gallery-in-android

public class ImageItem {
    private Bitmap image;
    private String title;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
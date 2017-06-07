package at.sw2017.pocketdiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterDeletePicture extends BaseAdapter {
    private Context mContext;

    public ImageAdapterDeletePicture(Context c) {
        mContext = c;
    }

    public int getCount() {
        return file_paths.length;
    }

    public String getItem(int position) {
        return file_paths[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image_view;
        if (convertView == null) {
            image_view = new ImageView(mContext);
            image_view.setLayoutParams(new GridView.LayoutParams(220, 220));
            image_view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image_view.setId(position);
            image_view.setPadding(8, 8, 8, 8);
        } else {
            image_view = (ImageView) convertView;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file_paths[position]);
        if (bitmap != null) {
            image_view.setImageBitmap(bitmap);
        } else {
            Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), android.R.drawable.ic_delete, null);
            image_view.setImageDrawable(drawable);
        }
        return image_view;
    }

    public String[] file_paths;
}
package at.sw2017.pocketdiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBFriend;

public class ShowEntryScreen extends AppCompatActivity {

    private int entry_id;
    final int THUMBNAIL_SIZE = 140;
    private static final int WRITE_STORAGE_REQUEST = 2;
    private int address_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_entry_screen);
        Intent intent = getIntent();
        entry_id = Integer.parseInt(intent.getStringExtra("entry_id"));

        Entry entry = Helper.getEntryComplete(this, entry_id);
        address_id = entry.getAddressId();
        TextView title = (TextView) findViewById(R.id.out_title);
        title.setText(entry.getTitle());
        TextView main_category = (TextView) findViewById(R.id.out_category);
        main_category.setText(entry.getMainCategory().getName());
        TextView sub_category = (TextView) findViewById(R.id.out_subcategory);
        sub_category.setText(entry.getSubCategory().getName());
        TextView date = (TextView) findViewById(R.id.out_date);
        TextView friends = (TextView)findViewById(R.id.out_friends);
        String friend_ = entry.getAllFriends();
        if(friend_ == null){
            friends.setText("");
        }
        else {
            String friendsForTextView = "";
            String[] parts = friend_.split(",");
            for (int i = 0; i < parts.length; i++) {
                Friend temp_friend;
                DBFriend dbf = new DBFriend(this);
                temp_friend = dbf.getFriend(Integer.parseInt(parts[i]));
                friendsForTextView += temp_friend.getName() + ", ";
            }
            friendsForTextView = friendsForTextView.substring(0, friendsForTextView.length() - 2);
            friends.setText(friendsForTextView);
        }
        friends.setMovementMethod(new ScrollingMovementMethod());
        if (entry.getDate() != null) {
            String date_string = new SimpleDateFormat("yyyy-MM-dd").format(entry.getDate());
            date.setText(date_string);
        } else {
            date.setText("");
        }
        TextView address = (TextView) findViewById(R.id.out_address);
        if (entry.getAddress() != null) {
            address.setText(entry.getAddress().getStreet() + ", " + entry.getAddress().getCity());
        }
        TextView description = (TextView) findViewById(R.id.out_description);
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


        final Button create_pdf =(Button)findViewById(R.id.pdf_export);

        final TextView title_pdf = (TextView) findViewById(R.id.out_title);
        final TextView category_pdf = (TextView) findViewById(R.id.out_category);
        final TextView subcategory_pdf = (TextView) findViewById(R.id.out_subcategory);
        final TextView date_pdf = (TextView) findViewById(R.id.out_date);
        final TextView location_pdf = (TextView) findViewById(R.id.out_address);
        final TextView friends_pdf = (TextView) findViewById(R.id.out_friends);
        final TextView text_pdf = (TextView) findViewById(R.id.out_description);


        create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_pdf.setBackgroundColor(Color.RED);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST);
                    }
                }

                File pdfFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/.POCKETDIARY");
                // boolean success = false;
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                }

                //Create time stamp
                Date date = new Date() ;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

                File myFile = new File(pdfFolder + timeStamp + ".pdf");

                try {
                    OutputStream output = new FileOutputStream(myFile);
                    Document document = new Document();
                    PdfWriter.getInstance(document, output);
                    document.open();

                    try {
                        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.NORMAL, BaseColor.BLACK);
                        Font red = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.NORMAL, BaseColor.RED);
                        Chunk title_start = new Chunk("Title: ", red);
                        Paragraph para1 = new Paragraph(title_start);
                        Chunk title_end = new Chunk(title_pdf.getText().toString(), font);
                        para1.add(title_end);
                        document.add(para1);

                        Chunk category_start = new Chunk("Category: ", red);
                        Paragraph para2 = new Paragraph(category_start);
                        Chunk category_end = new Chunk(category_pdf.getText().toString(), font);
                        para2.add(category_end);
                        document.add(para2);

                        Chunk subcategory_start = new Chunk("Subcategory: ", red);
                        Paragraph para3 = new Paragraph(subcategory_start);
                        Chunk subcategory_end = new Chunk(subcategory_pdf.getText().toString(), font);
                        para3.add(subcategory_end);
                        document.add(para3);

                        Chunk date_start = new Chunk("Date: ", red);
                        Paragraph para4 = new Paragraph(date_start);
                        Chunk date_end = new Chunk(date_pdf.getText().toString(), font);
                        para4.add(date_end);
                        document.add(para4);

                        Chunk location_start = new Chunk("Location: ", red);
                        Paragraph para5 = new Paragraph(location_start);
                        Chunk location_end = new Chunk(location_pdf.getText().toString(), font);
                        para5.add(location_end);
                        document.add(para5);

                        Chunk friends_start = new Chunk("Friends: ", red);
                        Paragraph para6 = new Paragraph(friends_start);
                        Chunk friends_end = new Chunk(friends_pdf.getText().toString(), font);
                        para6.add(friends_end);
                        document.add(para6);

                        Chunk text_start = new Chunk("Text: ", red);
                        Paragraph para7 = new Paragraph(text_start);
                        Chunk text_end = new Chunk(text_pdf.getText().toString(), font);
                        para7.add(text_end);
                        document.add(para7);

                        Toast.makeText(getApplicationContext(), "Pdf was created", Toast.LENGTH_SHORT).show();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    document.close();

                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);*/

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showLocation(View view) {
        Intent intent = new Intent(this, LocationScreen.class);
        intent.putExtra("address_id", Integer.toString(address_id));
        startActivity(intent);
    }
}
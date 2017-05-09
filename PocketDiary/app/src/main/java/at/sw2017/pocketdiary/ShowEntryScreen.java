package at.sw2017.pocketdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import at.sw2017.pocketdiary.business_objects.Entry;

public class ShowEntryScreen extends AppCompatActivity {

    private int entry_id;

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
        TextView description = (TextView)findViewById(R.id.out_description);
        if (entry.getDescription() != null) {
            description.setText(entry.getDescription());
        }
    }
}
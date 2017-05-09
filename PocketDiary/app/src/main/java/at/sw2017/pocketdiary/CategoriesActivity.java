package at.sw2017.pocketdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBCategory;

/**
 * Created by marku on 09.05.2017.
 */

public class CategoriesActivity extends Activity {

    private Spinner spinner_main_cat;
    private String empty_spinner_text = "Select Category";
    private int crt_parent_id = -1;
    List<String> strings_maincategories = new ArrayList<>();
    List<Category> maincategories = new ArrayList<>();
    ArrayList<Integer> db_ids = new ArrayList<>();
    ArrayAdapter adapter;
    boolean first_touch = true;

    List<Category> subcategories = new ArrayList<>();
    List<String> strings_subcategories = new ArrayList<>();

    private Button add_btn;
    private EditText input_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setMainCategorySpinner();
        setUpButton();
    }

    private void setUpButton(){
        input_txt = (EditText) findViewById(R.id.cat_input_txt);
        add_btn = (Button) findViewById(R.id.cat_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_txt.getText().toString().equals("")){
                    printToast("Please provide a valid name for a subcategory.");
                }
                else if(crt_parent_id != -1){
                    DBCategory db_cat = new DBCategory(CategoriesActivity.this);
                    Category category = new Category();
                    category.setName(input_txt.getText().toString());
                    category.setDeleted(false);
                    category.setParentId(crt_parent_id);
                    db_cat.insert(category);
                    fillListView(crt_parent_id);
                    printToast("\""+category.getName()+"\" was created successfully.");
                }
                else{
                    printToast("Please choose a main category first.");
                }
            }
        });
    }

    private void setMainCategorySpinner(){
        DBCategory dbc = new DBCategory(this);
        if (dbc.getAllCategories().size() == 0) {
            Helper.initCategories(this);
        }
        maincategories = dbc.getMainCategories();
        strings_maincategories.add(empty_spinner_text);
        for (Category temp_cat : maincategories) {
            strings_maincategories.add(temp_cat.getName());
        }

        spinner_main_cat = (Spinner) findViewById(R.id.cat_maincat_spinner);
        final ArrayAdapter<String> main_spinner = new ArrayAdapter<String>(
                this, R.layout.layout_spinner_item, strings_maincategories);

        spinner_main_cat.setAdapter(main_spinner);
        spinner_main_cat.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        Category selected_category = Helper.getCategoryByName(maincategories, spinner_main_cat.getSelectedItem().toString());
                        if (selected_category != null) {
                            crt_parent_id = selected_category.getId();
                            setLabelName(selected_category.getName());
                            fillListView(selected_category.getId());
                            first_touch = false;
                        }
                        else if(first_touch == false){
                            crt_parent_id = -1;
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            setLabelName("...");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

    private void fillListView(int parent_id){
        DBCategory dbCategory = new DBCategory(this);
        strings_subcategories = new ArrayList<>();
        subcategories = dbCategory.getSubCategories(parent_id);
        ListView review_listview = (ListView) findViewById(R.id.cat_listview);
        ArrayList<String> categories = new ArrayList<>();
        for (Category category : subcategories){
                categories.add(category.getName());
                db_ids.add(category.getId());
        }

        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, categories);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //db_ids.get(position);
                createAlert(subcategories.get(position));
            }
        });
    }

    private void setLabelName(String name){
        TextView label = (TextView) findViewById(R.id.cat_subcat_label_list);
        label.setText("All subcategories of " + name);
    }

    private void createAlert(final Category category){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CategoriesActivity.this);
        builder1.setMessage("Edit your subcategory.");
        builder1.setCancelable(true);

        final EditText input = new EditText(CategoriesActivity.this);
        input.setText(category.getName());
        builder1.setView(input);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DBCategory db_cat = new DBCategory(CategoriesActivity.this);
                        String old_name = category.getName();
                        Category updated_cat = category;
                        updated_cat.setName(input.getText().toString());
                        db_cat.update(updated_cat);
                        fillListView(updated_cat.getParentId());
                        printToast("\""+old_name+"\" has changed to \""+updated_cat.getName()+"\".");
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNeutralButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DBCategory db_cat = new DBCategory(CategoriesActivity.this);
                        db_cat.delete(category);
                        fillListView(category.getParentId());
                        printToast("\""+category.getName()+"\" was deleted succesfully.");
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void printToast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}


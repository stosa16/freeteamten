package at.sw2017.pocketdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeletePictureScreen extends AppCompatActivity {

    private static final int DELETE_IMAGE_REQUEST = 6;
    public String[] file_paths;
    private List<String> deleted_paths = new ArrayList<>();
    private GridView grid_view;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_picture_screen);

        grid_view = (GridView) findViewById(R.id.gridview);
        adapter = new ImageAdapter(this);
        Intent intent = getIntent();
        file_paths = intent.getStringArrayExtra("file_paths");
        adapter.file_paths = file_paths;
        grid_view.setAdapter(adapter);

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                createAlert(view, adapter.getItem(position));
            }
        });
    }

    private void createAlert(final View view, final String file_path){
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(DeletePictureScreen.this);
        alert_builder.setMessage("Do you really want to remove the picture?");
        alert_builder.setCancelable(true);

        alert_builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        view.setVisibility(View.GONE);
                        deleted_paths.add(file_path);
                        updateFilePaths();
                        grid_view.invalidateViews();
                    }
                });

        alert_builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alert_builder.create();
        alert.show();
    }

    public void updateFilePaths() {
        List<String> result_paths = new ArrayList<>();
        List<String> test = Arrays.asList(file_paths);
        result_paths.addAll(test);
        result_paths.removeAll(deleted_paths);
        file_paths = result_paths.toArray(new String[0]);
        adapter.file_paths = file_paths;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        updateFilePaths();
        String[] paths = file_paths;
        returnIntent.putExtra("file_paths", paths);
        setResult(DELETE_IMAGE_REQUEST, returnIntent);
        finish();
    }

}

package at.sw2017.pocketdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.R.id.list;
import static at.sw2017.pocketdiary.R.id.listView1;
import static at.sw2017.pocketdiary.R.id.parent;

public class Friends extends AppCompatActivity {

    private ListView lv;
    public FriendListAdapter adapter;
    public ArrayList<Integer> friendIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        DBHandler db = new DBHandler(this);

        lv = (ListView) findViewById(listView1);

        List<String> friendNames = new ArrayList<>();

        DBFriend dbf = new DBFriend(this);
        //get all undeleted friends in database (even including isDeleted = 1)
        final List<Friend> allFriends = dbf.getAllNondeletedFriends();


        //list of al friend's IDs
        friendIds = new ArrayList<Integer>();

        final FriendItem friend_data[] = new FriendItem[allFriends.size()];
        for(int i = 0; i < allFriends.size(); i++){
            friend_data[i] = new FriendItem(allFriends.get(i).getName());
            friendIds.add(allFriends.get(i).getId());
        }

        adapter = new FriendListAdapter(this,
                R.layout.list_friend_item, friend_data);

        lv.setAdapter(adapter);

        ListView lvv = (ListView) findViewById(R.id.listView1);
        lvv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
                createAlert(allFriends.get(position));
            }
        });}

    private void createAlert(final Friend friend){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Friends.this);
        builder1.setMessage("Edit a friend.");
        builder1.setCancelable(true);

        final EditText input = new EditText(Friends.this);
        input.setText(friend.getName());
        builder1.setView(input);

        builder1.setPositiveButton(
                "Update friend",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DBFriend db_friend = new DBFriend(Friends.this);
                        String old_name = friend.getName();
                        Friend updated_friend = friend;
                        updated_friend.setName(input.getText().toString());
                        db_friend.updateFriend(updated_friend);
                        finish();
                        startActivity(getIntent());
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
                "Delete friend",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DBFriend db_friend = new DBFriend(Friends.this);
                        String old_name = friend.getName();
                        Friend updated_friend = friend;
                        //delete friend by setting isDeleted attribute to 1
                        updated_friend.setDeleted(true);
                        db_friend.updateFriend(updated_friend);
                        finish();
                        startActivity(getIntent());
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    public void addFriend(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String value = editText.getText().toString();
        Friend friend = new Friend(value, false);
        DBFriend dbf = new DBFriend(this);
        dbf.insert(friend);
        finish();
        startActivity(getIntent());
    }
}

package at.sw2017.pocketdiary;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
        List<Friend> allFriends = dbf.getAllFriends();

        //list of al friend's IDs
        friendIds = new ArrayList<Integer>();

        FriendItem friend_data[] = new FriendItem[allFriends.size()];
        for(int i = 0; i < allFriends.size(); i++){
            friend_data[i] = new FriendItem(allFriends.get(i).getName());
            friendIds.add(allFriends.get(i).getId());
            Log.d("CREATION", String.valueOf(friendIds.get(i)));
        }

        adapter = new FriendListAdapter(this,
                R.layout.list_friend_item, friend_data);

        lv.setAdapter(adapter);

        ListView lvv = (ListView) findViewById(R.id.listView1);
        lvv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Log.d("CREATION", "amazing: " + position + ", " + friendIds.get(position));
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }

    public void addFriend(View view){
        //Log.d("CREATION", "amazing adding friend ");
        EditText editText = (EditText) findViewById(R.id.editText);
        String value = editText.getText().toString();
        Friend friend = new Friend(value, false);
        DBFriend dbf = new DBFriend(this);
        dbf.insert(friend);
        finish();
        startActivity(getIntent());
    }
}

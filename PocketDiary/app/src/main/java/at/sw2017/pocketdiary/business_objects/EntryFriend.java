package at.sw2017.pocketdiary.business_objects;

/**
 * Created by marku on 30.05.2017.
 */

public class EntryFriend {

    private int friend_id;
    private int event_id;

    public EntryFriend() {

    }

    public EntryFriend(int friend_id, int event_id) {
        this.friend_id = friend_id;
        this.event_id = event_id;
    }

    public int getFriendId() {
        return this.friend_id;
    }

    public int getEventId() {
        return this.event_id;
    }

    public void setFriendId(int friend_id) {
        this.friend_id = friend_id;
    }

    public void setEventId(int event_id) {
        this.event_id = event_id;
    }
}

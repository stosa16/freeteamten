package at.sw2017.pocketdiary;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.CustomDate;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBPicture;
import at.sw2017.pocketdiary.database_access.DBStatistic;

public final class TestHelper {

    public static void initCategories(Context context) {
        DBCategory dbc = new DBCategory(context);
        Category category1 = new Category("Sport");
        Category category2 = new Category("Essen");
        Category category3 = new Category("Party");
        Category subcategory11 = new Category("Walking", 1);
        Category subcategory12 = new Category("Running", 1);
        Category subcategory13 = new Category("Climbing", 1);
        Category subcategory21 = new Category("Chinesisch", 2);
        Category subcategory31 = new Category("Einweihungsfeier", 3);
        Category subcategory32 = new Category("Abschlussfeier", 3);
        dbc.insert(category1);
        dbc.insert(category2);
        dbc.insert(category3);
        dbc.insert(subcategory11);
        dbc.insert(subcategory12);
        dbc.insert(subcategory13);
        dbc.insert(subcategory21);
        dbc.insert(subcategory31);
        dbc.insert(subcategory32);
        dbc.close();
    }

    public static void initStatistics(Context context){
        DBStatistic dbs = new DBStatistic(context);
        Statistic statistic_1 = new Statistic();

        statistic_1.setTitle("Statistic");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 4, 1, 0, 0, 0);
        Date date_from = calendar.getTime();
        statistic_1.setDateFrom(date_from);

        calendar.set(2017, 5, 1, 0, 0, 0);
        Date date_until = calendar.getTime();
        statistic_1.setDateUntil(date_until);
        statistic_1.setCategoryId(1);
        //statistic_1.setSubCategoryId(1);
        //statistic_1.setSearchTerm("SearchTerm");
        dbs.insert(statistic_1);

        dbs = new DBStatistic(context);

        statistic_1.setTitle("Statistic_2");
        statistic_1.setDateFrom(date_from);
        statistic_1.setDateUntil(date_until);
        statistic_1.setCategoryId(1);
        statistic_1.setSubCategoryId(4);
        statistic_1.setSearchTerm("test");
        dbs.insert(statistic_1);

        dbs = new DBStatistic(context);
        Statistic statistic_2 = new Statistic();
        statistic_2.setTitle("Statistic_3");
        statistic_2.setDateFrom(date_from);
        statistic_2.setCategoryId(1);
        statistic_2.setSubCategoryId(4);
        statistic_2.setSearchTerm("test");
        dbs.insert(statistic_2);

        dbs = new DBStatistic(context);
        Statistic statistic_3 = new Statistic();
        statistic_3.setTitle("Statistic_4");
        statistic_3.setDateUntil(date_until);
        statistic_3.setCategoryId(1);
        statistic_3.setSubCategoryId(4);
        statistic_3.setSearchTerm("test");
        dbs.insert(statistic_3);
    }

    public static void initFriends(Context context) {
        DBFriend dbf = new DBFriend(context);

        Friend friend_1 = new Friend();
        friend_1.setName("Hermann");

        Friend friend_2 = new Friend();
        friend_2.setName("Egon");

        dbf.insert(friend_1);
        dbf.insert(friend_2);
    }

    public static Entry createTestEntryBasic(Context context) {
        DBEntry dbe = new DBEntry(context);
        Entry entry = new Entry();
        entry.setTitle("Test");
        entry.setDescription("This is a test!");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(4);
        entry.setAddressId(1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 4, 10, 0, 0, 0);
        Date entry_date = calendar.getTime();
        entry.setDate(entry_date);

        Picture picture = new Picture();
        picture.setFilePath("/storage/emulated/0/Pictures/1496724335881.jpg");
        picture.setName("picture");
        DBPicture dbp = new DBPicture(context);
        long id_pic = dbp.insert(picture);
        picture.setId((int) id_pic);
        entry.addPicture(picture);


        List<Picture> list = new ArrayList<>();
        list.add(picture);
        entry.setPictures(list);

        long id = dbe.insert(entry);

        entry.setTitle("Test_Street");
        entry.setDescription("This is a test!");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(4);
        entry.setAddressId(2);
        Friend friend = new Friend();
        friend.setId(1);
        friend.setName("Hermann");
        friend.setDeleted(false);
        List<Friend> friends_list = new ArrayList<>();
        friends_list.add(friend);
        entry.setFriends(friends_list);
        calendar.set(2017, 4, 11, 0, 0, 0);
        entry.setDate(calendar.getTime());

        dbe.insert(entry);

        dbe.close();

        Entry loaded_entry = Helper.getEntryComplete(context, (int) id);
        return loaded_entry;
    }

    public static void initAddress(Context context) {
        DBAddress dba = new DBAddress(context);
        Address address = new Address();

        double coordinates = 0.00;
        address.setPoi("Poi");
        address.setStreet("Street");
        address.setCity("City");
        address.setCountry("Country");
        address.setZip("0000");
        address.setLatitude(coordinates);
        address.setLongitude(coordinates);
        dba.insert(address);


        dba = new DBAddress(context);
        Address address2 = new Address();
        address2.setStreet("Street");
        address2.setCity("City");
        address2.setCountry("Country");
        address2.setZip("0000");
        address2.setLatitude(coordinates);
        address2.setLongitude(coordinates);
        dba.insert(address2);
    }
}

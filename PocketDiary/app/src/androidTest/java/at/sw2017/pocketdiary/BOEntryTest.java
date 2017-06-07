package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BOEntryTest {

    @Test
    public void testEntryInitWithoutParameters(){
        int id, main_id, sub_id, add_id;
        String title = "Title";
        id = main_id = sub_id = add_id = 1;
        Category cat = new Category("Category");
        Date date = new Date(2017,07,07);
        String desc = "Description";
        String all_friends = "1,2,3";

        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;
        Address address = new Address(id, poi, street, zip, city, country, lat_lng, lat_lng);

        List<Friend> friends = new ArrayList<Friend>();
        friends.add(new Friend(id, "Markus", false));
        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("filepath", "name"));

        Entry entry = new Entry();

        entry.setId(id);
        entry.setTitle(title);
        entry.setMainCategoryId(main_id);
        entry.setMainCategory(cat);
        entry.setSubCategoryId(sub_id);
        entry.setSubCategory(cat);
        entry.setDate(date);
        entry.setDescription(desc);
        entry.setAddressId(add_id);
        entry.setAddress(address);
        entry.setFriends(friends);
        entry.setAllFriends(all_friends);
        entry.setPictures(pictures);

        assertEquals(id, entry.getId());
        assertEquals(title, entry.getTitle());
        assertEquals(main_id, entry.getMainCategoryId());
        assertEquals(cat, entry.getMainCategory());
        assertEquals(sub_id, entry.getSubCategoryId());
        assertEquals(cat, entry.getSubCategory());
        assertEquals(date, entry.getDate());
        assertEquals(desc, entry.getDescription());
        assertEquals(new Integer(add_id), entry.getAddressId());
        assertEquals(address, entry.getAddress());
        assertEquals(friends, entry.getFriends());
        assertEquals(all_friends, entry.getAllFriends());
        assertEquals(pictures, entry.getPictures());

    }

    @Test
    public void testEntryInitWithSixParameters(){
        int main_id, sub_id;
        String title = "Title";
        main_id = sub_id  = 1;
        Date date = new Date(2017,07,07);
        String desc = "Description";

        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;
        Address address = new Address(main_id, poi, street, zip, city, country, lat_lng, lat_lng);

        Entry entry = new Entry(title, main_id, sub_id, date, desc, address);

        assertEquals(title, entry.getTitle());
        assertEquals(main_id, entry.getMainCategoryId());
        assertEquals(sub_id, entry.getSubCategoryId());
        assertEquals(date, entry.getDate());
        assertEquals(desc, entry.getDescription());
        assertEquals(address, entry.getAddress());
    }

    @Test
    public void testEntryInitWithFiveParameters(){
        int main_id, sub_id;
        String title = "Title";
        main_id = sub_id  = 1;
        Date date = new Date(2017,07,07);
        String desc = "Description";

        Entry entry = new Entry(title, main_id, sub_id, date, desc);

        assertEquals(title, entry.getTitle());
        assertEquals(main_id, entry.getMainCategoryId());
        assertEquals(sub_id, entry.getSubCategoryId());
        assertEquals(date, entry.getDate());
        assertEquals(desc, entry.getDescription());
    }

    @Test
    public void testEntryInitWithAllParameters(){
        int main_id, sub_id, add_id;
        String title = "Title";
        main_id = sub_id = add_id = 1;
        Category cat = new Category("Category");
        Date date = new Date(2017,07,07);
        String desc = "Description";
        String all_friends = "1,2,3";

        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;
        Address address = new Address(main_id, poi, street, zip, city, country, lat_lng, lat_lng);

        List<Friend> friends = new ArrayList<Friend>();
        friends.add(new Friend(main_id, "Markus", false));
        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("filepath", "name"));

        Entry entry = new Entry(title, main_id, cat, sub_id, cat, date, desc, add_id, address, friends, pictures, all_friends);

        assertEquals(title, entry.getTitle());
        assertEquals(main_id, entry.getMainCategoryId());
        assertEquals(cat, entry.getMainCategory());
        assertEquals(sub_id, entry.getSubCategoryId());
        assertEquals(cat, entry.getSubCategory());
        assertEquals(date, entry.getDate());
        assertEquals(desc, entry.getDescription());
        assertEquals(new Integer(add_id), entry.getAddressId());
        assertEquals(address, entry.getAddress());
        assertEquals(friends, entry.getFriends());
        assertEquals(all_friends, entry.getAllFriends());
        assertEquals(pictures, entry.getPictures());

    }

    @Test
    public void testEntryInitWithAllParametersAndNull(){
        int main_id, sub_id, add_id;
        String title = "Title";
        main_id = sub_id = add_id = 1;
        Category cat = new Category("Category");
        Date date = new Date(2017,07,07);
        String desc = "Description";
        String all_friends = "1,2,3";

        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;
        Address address = new Address(main_id, poi, street, zip, city, country, lat_lng, lat_lng);

        Entry entry = new Entry(title, main_id, cat, sub_id, cat, date, desc, add_id, address, null, null, all_friends);

        assertEquals(title, entry.getTitle());
        assertEquals(main_id, entry.getMainCategoryId());
        assertEquals(cat, entry.getMainCategory());
        assertEquals(sub_id, entry.getSubCategoryId());
        assertEquals(cat, entry.getSubCategory());
        assertEquals(date, entry.getDate());
        assertEquals(desc, entry.getDescription());
        assertEquals(new Integer(add_id), entry.getAddressId());
        assertEquals(address, entry.getAddress());
        assertEquals(new ArrayList<Friend>(), entry.getFriends());
        assertEquals(all_friends, entry.getAllFriends());
        assertEquals(new ArrayList<Picture>(), entry.getPictures());

    }


}

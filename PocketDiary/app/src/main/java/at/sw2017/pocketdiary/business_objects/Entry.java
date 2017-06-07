package at.sw2017.pocketdiary.business_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entry {
    private int id;
    private String title;
    private int mainCategoryId;
    private Category mainCategory;
    private int subCategoryId;
    private Category subCategory;
    private Date date;
    private String description;
    private int addressId;
    private Address address;
    private List<Friend> friends = new ArrayList<Friend>();
    private List<Picture> pictures = new ArrayList<Picture>();
    private String allFriends;

    public Entry() {
    }

    public Entry(String title, int mainCategoryId, int subCategoryId, Date date, String description) {
        this.title = title;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.date = date;
        this.description = description;
    }

    public Entry(String title, int mainCategoryId, int subCategoryId, Date date, String description, Address address) {
        this.title = title;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.date = date;
        this.description = description;
        this.address = address;
    }

    public Entry(String title, int mainCategoryId, Category mainCategory, int subCategoryId, Category subCategory, Date date, String description, int addressId, Address address, List<Friend> friends, List<Picture> pictures, String allFriends) {
        this.title = title;
        this.mainCategoryId = mainCategoryId;
        this.mainCategory = mainCategory;
        this.subCategoryId = subCategoryId;
        this.subCategory = subCategory;
        this.date = date;
        this.description = description;
        this.addressId = addressId;
        this.address = address;
        this.allFriends = allFriends;
        if (friends == null) {
            this.friends = new ArrayList<Friend>();
        } else {
            this.friends = friends;
        }
        if (pictures == null) {
            this.pictures = new ArrayList<Picture>();
        } else {
            this.pictures = pictures;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public Category getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(Category mainCategory) {
        this.mainCategory = mainCategory;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Picture> addPicture(Picture picture) {
        this.pictures.add(picture);
        return this.pictures;
    }
    public String getAllFriends() {
        return allFriends;
    }

    public void setAllFriends(String allFriends) {
        this.allFriends = allFriends;
    }
}
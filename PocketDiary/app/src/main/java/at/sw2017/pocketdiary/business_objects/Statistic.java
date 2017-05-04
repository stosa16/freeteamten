package at.sw2017.pocketdiary.business_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Statistic {
    private int id;
    private String title;
    private Date dateFrom;
    private Date dateUntil;
    private int categoryId;
    private Category category;
    private int subCategoryId;
    private Category subCategory;
    private String searchTerm;
    private List<Friend> friendList = new ArrayList<Friend>();

    public Statistic() {
    }

    public Statistic(int id, String title, Date dateFrom, Date dateUntil, int categoryId, Category category, int subCategoryId, Category subCategory, String searchTerm) {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateUntil = dateUntil;
        this.categoryId = categoryId;
        this.category = category;
        this.subCategoryId = subCategoryId;
        this.subCategory = subCategory;
        this.searchTerm = searchTerm;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateUntil() {
        return dateUntil;
    }

    public void setDateUntil(Date dateUntil) {
        this.dateUntil = dateUntil;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

}

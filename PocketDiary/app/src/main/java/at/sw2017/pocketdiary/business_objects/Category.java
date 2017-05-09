package at.sw2017.pocketdiary.business_objects;

public class Category {
    private int id;
    private String name;
    private int parentId;
    private boolean isDeleted = false;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Category(int id, String name, int parentId, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

package at.sw2017.pocketdiary.business_objects;

public class Friend {
    private int id;
    private String name;
    private boolean isDeleted;

    public Friend() {
    }

    public Friend(String name, boolean isDeleted) {
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public Friend(int id, String name, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public Friend(String name) {
        this.name = name;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}

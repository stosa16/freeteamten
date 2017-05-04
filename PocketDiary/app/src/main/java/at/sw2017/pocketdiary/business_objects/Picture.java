package at.sw2017.pocketdiary.business_objects;

public class Picture {
    private int id;
    private byte[] picture;

    public Picture() {
    }

    public Picture(byte[] picture) {
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}

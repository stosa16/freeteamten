package at.sw2017.pocketdiary.business_objects;

public class UserSetting {
    private int id;
    private String userName;
    private byte[] picture;
    private String pin;
    private String isPinActive; //0 -> not active, 1 -> active

    public UserSetting() {
    }

    public UserSetting(String userName) {
        this.userName = userName;
    }

    public UserSetting(int id, String pin, String isPinActive) {
        this.id = id;
        this.pin = pin;
        this.isPinActive = isPinActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String isPinActive() {
        return isPinActive;
    }

    public void setPinActive(String pinActive) {
        this.isPinActive = pinActive;
    }
}
package at.sw2017.pocketdiary.business_objects;

public class UserSetting {
    private int id;
    private String userName;
    private byte[] picture;
    private int pin;
    private boolean isPinActive;

    public UserSetting() {
    }

    public UserSetting(String userName) {
        this.userName = userName;
    }

    public UserSetting(int id, int pin, boolean isPinActive) {
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

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public boolean isPinActive() {
        return isPinActive;
    }

    public void setPinActive(boolean pinActive) {
        isPinActive = pinActive;
    }
}
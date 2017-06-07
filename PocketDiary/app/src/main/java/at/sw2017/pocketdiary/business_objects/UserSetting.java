package at.sw2017.pocketdiary.business_objects;

public class UserSetting {
    private int id;
    private String userName;
    private String filePath;
    private String Picturename;
    private String pin;
    private String isPinActive; //0 -> not active, 1 -> active

    public UserSetting() {
    }

    public UserSetting(String userName) {
        this.userName = userName;
    }

    public UserSetting(int id, String pin, String isPinActive, String filePath, String name) {
        this.id = id;
        this.pin = pin;
        this.isPinActive = isPinActive;
        this.filePath = filePath;
        this.Picturename = name;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPicturename() {
        return Picturename;
    }

    public void setPicturename(String Picturename) {
        this.Picturename = Picturename;
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
package at.sw2017.pocketdiary.business_objects;

public class Picture {
    private int id;
    private String filePath;
    private String name;

    public Picture() {
    }

    public Picture(String filePath, String name) {
        this.filePath = filePath;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

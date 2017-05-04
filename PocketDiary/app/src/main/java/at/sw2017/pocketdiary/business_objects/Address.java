package at.sw2017.pocketdiary.business_objects;

public class Address {
    private int id;
    private String poi;
    private String street;
    private String zip;
    private String city;
    private String country;
    private Float longitude;
    private Float latitude;

    public Address() {
    }

    public Address(int id, String poi, String street, String zip, String city, String country, Float longitude, Float latitude) {
        this.id = id;
        this.poi = poi;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(String poi, String street, String zip, String city, String country, Float longitude, Float latitude) {
        this.poi = poi;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}

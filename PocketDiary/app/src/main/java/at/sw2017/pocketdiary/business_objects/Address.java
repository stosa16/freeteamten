package at.sw2017.pocketdiary.business_objects;

public class Address {
    private int id;
    private String poi;
    private String street;
    private String zip;
    private String city;
    private String country;
    private double longitude;
    private double latitude;

    public Address() {
    }

    public Address(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(String street, double longitude, double latitude) {
        this.street = street;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(int id, String poi, String street, String zip, String city, String country, double longitude, double latitude) {
        this.id = id;
        this.poi = poi;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(String poi, String street, String zip, String city, String country, double longitude, double latitude) {
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

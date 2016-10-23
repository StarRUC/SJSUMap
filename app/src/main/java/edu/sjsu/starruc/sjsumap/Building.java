package edu.sjsu.starruc.sjsumap;

/**
 * Created by StarRUC on 10/23/16.
 */

public class Building {
    private long id;
    private String name;
    private String address;
    private String photoUrl;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return "Name: " + name + "\n"
                + "Address: " + address + "\n"
                + "Photo Url: " + photoUrl + "\n";
    }
}

package edu.sjsu.starruc.sjsumap;

/**
 * Created by StarRUC on 10/23/16.
 */

public class Building {
    private long id;
    private String name;
    private String address;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int cx;
    private int cy;
    private double lat;
    private double lng;

    public Building() {
    }

    public Building(long id, String name, String address,
                    int x1, int y1, int x2, int y2, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.cx = (x1 + x2) / 2;
        this.cy = (y1 + y2) / 2;
        this.lat = lat;
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
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

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLat() {
        return this.lat;
    }
    public double getLng() {
        return this.lng;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return "Name: " + name + "\n"
                + "Address: " + address + "\n";
    }
}

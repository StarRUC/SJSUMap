package edu.sjsu.starruc.sjsumap;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StarRUC on 10/26/16.
 */

public class CampusManager {
    private static Location origin;
    private static Location topLeft;
    private static Location topRight;
    private static Location bottomLeft;
    private static Location bottomRight;
    private static double slope1, slope2, slope3, slope4;
    private static double width, height, a;
    private static List<Building> buildingList = null;

    public CampusManager() {

        if (buildingList == null) {
            origin = new Location("");
            origin.setLatitude(37.337460);
            origin.setLongitude(-121.887321);

            topLeft = new Location("");
            topLeft.setLatitude(37.335848);
            topLeft.setLongitude(-121.886157);

            topRight= new Location("");
            topRight.setLatitude(37.338958);
            topRight.setLongitude(-121.879656);

            bottomLeft = new Location("");
            bottomLeft.setLatitude(37.331445);
            bottomLeft.setLongitude(-121.882856);

            bottomRight = new Location("");
            bottomRight.setLatitude(37.334533);
            bottomRight.setLongitude(-121.876355);

            slope1 = getSlope(topLeft, topRight);
            slope2 = getSlope(topRight, bottomRight);
            slope3 = getSlope(bottomRight, bottomLeft);
            slope4 = getSlope(bottomLeft, topLeft);

//            width = bottomLeft.distanceTo(bottomRight);
//            height = bottomLeft.distanceTo(origin);
            width = 670.74;
            height = 778.81;
            a = 208.45;

            buildingList = new ArrayList<>();

            addBuilding(1, "King Library",
                    "Dr. Martin Luther King, Jr. Library, " +
                            "150 East San Fernando Street, " +
                            "San Jose, CA 95112",
//                    "King.jpg", 100, 700, 300, 1050);
                    "King.jpg", 40, 235, 120, 375);

            addBuilding(2, "Engineering Building",
                    "San Jose State University Charles W. Davidson " +
                            "College of Engineering, 1 Washington " +
                            "Square, San Jose, CA 95112",
//                    "Eng.jpg", 700, 700, 950, 1100);
                    "Eng.jpg", 285, 235, 380, 400);

            addBuilding(3, "Yoshihiro Uchida Hall",
                    "Yoshihiro Uchida Hall, San Jose, CA 95112",
//                    "YUH.jpg", 100, 1500, 300, 1750);
                    "YUH.jpg", 40, 540, 120, 645);

            addBuilding(4, "Student Union",
                    "Student Union Building, San Jose, CA 95112",
//                    "SU.jpg", 700, 1100, 1050, 1300);
                    "SU.jpg", 285, 400, 420, 480);

            addBuilding(5, "BBC",
                    "Boccardo Business Complex, San Jose, CA 95112",
//                    "BBC.jpg", 1150, 1300, 1300, 1500);
                    "BBC.jpg", 455, 480, 520, 540);

            addBuilding(6, "South Parking Garage",
                    "San Jose State University South Garage, " +
                            "330 South 7th Street, San Jose, CA 95112",
//                    "SPG.jpg", 400, 2050, 700, 2400);
                    "SPG.jpg", 165, 745, 285, 870);
        }
    }

    public static void addBuilding(int id, String name, String address,
                            String photoUrl, int x1, int y1,
                            int x2, int y2) {
        Building b = new Building(id, name, address, photoUrl,
                x1, y1, x2, y2);
        buildingList.add(b);
    }

    public static Building findBuildingByName(String prefix) {
        if (buildingList == null) return null;

        for (int i = 0; i < buildingList.size(); i++) {
            Building b = buildingList.get(i);
            if (b.getName().startsWith(prefix)) {
                return b;
            }
        }

        return null;
    }

    public static Building findBuildingByLocation(int x, int y) {
        if (buildingList == null) return null;

        for (int i = 0; i < buildingList.size(); i++) {
            Building b = buildingList.get(i);
            if (b.getX1() <= x && b.getY1() <= y
                    && b.getX2() >= x && b.getY2() >= y) {
                return b;
            }
        }

        return null;
    }

    public static int[] mapLocationToPixels(Location location) {
        int[] ans = new int[2];

        double b = location.distanceTo(topLeft);
        double c = location.distanceTo(origin);

        double x = (c * c - a * a - b * b) / (2 * a);
        double y = Math.sqrt(b * b - x * x);

        ans[0] = (int) (y * 1380 / width);
        ans[1] = (int) ((x + a) * 2280 / height);

        return ans;
    }

    public static boolean isInCampus(Location location) {
        double latituide = location.getLatitude();
        double longtitude = location.getLongitude();

        if (longtitude < topLeft.getLongitude() ||
                longtitude > bottomRight.getLongitude() ||
                latituide < bottomLeft.getLatitude() ||
                latituide > topRight.getLatitude()) {
            return false;
        }

        if (longtitude >= bottomLeft.getLongitude() &&
                longtitude <= topRight.getLongitude() &&
                latituide >= bottomRight.getLatitude() &&
                latituide <= topLeft.getLatitude()) {
            return true;
        }

        if (latituide >= topLeft.getLatitude() &&
                latituide <= topRight.getLatitude() &&
                longtitude >= topLeft.getLongitude() &&
                longtitude <= topRight.getLongitude()) {
            double slope = getSlope(topLeft, location);
            if (slope > slope1) return false;
            return true;
        }


        if (latituide >= bottomRight.getLatitude() &&
                latituide <= topRight.getLatitude() &&
                longtitude >= topRight.getLongitude() &&
                longtitude <= bottomRight.getLongitude()) {
            double slope = getSlope(topRight, location);
            if (slope < slope2) return false;
            return true;
        }


        if (latituide >= bottomLeft.getLatitude() &&
                latituide <= bottomRight.getLatitude() &&
                longtitude >= bottomLeft.getLongitude() &&
                longtitude <= bottomRight.getLongitude()) {
            double slope = getSlope(bottomRight, location);
            if (slope < slope3) return false;
            return true;
        }


        if (latituide >= bottomLeft.getLatitude() &&
                latituide <= topLeft.getLatitude() &&
                longtitude >= topLeft.getLongitude() &&
                longtitude <= bottomLeft.getLongitude()) {
            double slope = getSlope(bottomLeft, location);
            if (slope < slope4) return false;
            return true;
        }

        return false;

    }

    private static double getSlope(Location l1, Location l2) {
        if (Math.abs(l2.getLongitude() - l2.getLongitude()) < 0.0001) {
            return Double.MAX_VALUE;
        }

        Location point = new Location("");
        point.setLatitude(l1.getLatitude());
        point.setLongitude(l2.getLongitude());

        double h = point.distanceTo(l2);
        double w = point.distanceTo(l1);
        return h / w;
    }
}

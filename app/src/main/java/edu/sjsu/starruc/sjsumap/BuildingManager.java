package edu.sjsu.starruc.sjsumap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StarRUC on 10/26/16.
 */

public class BuildingManager {
    private static List<Building> buildingList = null;

    public BuildingManager() {
        if (buildingList == null) {
            buildingList = new ArrayList<>();

            addBuilding(1, "King Library",
                    "Dr. Martin Luther King, Jr. Library, " +
                            "150 East San Fernando Street, " +
                            "San Jose, CA 95112",
                    "King.jpg", 100, 700, 300, 1050);

            addBuilding(2, "Engineering Building",
                    "San Jose State University Charles W. Davidson " +
                            "College of Engineering, 1 Washington " +
                            "Square, San Jose, CA 95112",
                    "Eng.jpg", 700, 700, 950, 1100);

            addBuilding(3, "Yoshihiro Uchida Hall",
                    "Yoshihiro Uchida Hall, San Jose, CA 95112",
                    "YUH.jpg", 100, 1500, 300, 1750);

            addBuilding(4, "Student Union",
                    "Student Union Building, San Jose, CA 95112",
                    "SU.jpg", 700, 1100, 1050, 1300);

            addBuilding(5, "BBC",
                    "Boccardo Business Complex, San Jose, CA 95112",
                    "BBC.jpg", 1150, 1300, 1300, 1500);

            addBuilding(6, "South Parking Garage",
                    "San Jose State University South Garage, " +
                            "330 South 7th Street, San Jose, CA 95112",
                    "SPG.jpg", 400, 2050, 700, 2400);
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
}

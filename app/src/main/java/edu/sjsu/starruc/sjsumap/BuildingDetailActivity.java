package edu.sjsu.starruc.sjsumap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BuildingDetailActivity extends AppCompatActivity {
//    private BuildingDataSource dataSource;
    private BuildingManager buildingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        TextView buildingText = (TextView) findViewById(R.id.building_text);

        String buildingName = getIntent().getStringExtra("buildingName");
        buildingManager = new BuildingManager();
        Building building = buildingManager.findBuildingByName(buildingName);
//        dataSource = new BuildingDataSource(this);
//        dataSource.open();
//        Building building = dataSource.findBuildingByName(buildingName);
        if (building == null) {
            buildingText.setText("No buildings available");
        }
        else {
            buildingText.setText(building.toString());
        }

    }
}

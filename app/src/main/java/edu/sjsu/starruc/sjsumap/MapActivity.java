package edu.sjsu.starruc.sjsumap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {

    private BuildingDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dataSource = new BuildingDataSource(this);
        dataSource.open();
        dataSource.addBuilding("King Library",
                "Dr. Martin Luther King, Jr. Library, " +
                        "150 East San Fernando Street, " +
                        "San Jose, CA 95112",
                "King.jpg");


        Button detailButton = (Button) findViewById(R.id.detail_button);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, BuildingDetailActivity.class);
                String buildingName = "King Library";
//                String buildingName = "Eng";
                intent.putExtra("buildingName", buildingName);
                startActivity(intent);
            }
        };

        detailButton.setOnClickListener(listener);
    }
}

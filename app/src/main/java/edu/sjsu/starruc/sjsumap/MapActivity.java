package edu.sjsu.starruc.sjsumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                "King.jpg", 100, 700, 300, 1050);

        dataSource.addBuilding("Engineering Building",
                "San Jose State University Charles W. Davidson " +
                        "College of Engineering, 1 Washington " +
                        "Square, San Jose, CA 95112",
                "Eng.jpg", 700, 700, 950, 1100);

        dataSource.addBuilding("Yoshihiro Uchida Hall",
                "Yoshihiro Uchida Hall, San Jose, CA 95112",
                "Eng.jpg", 100, 1500, 300, 1750);

        dataSource.addBuilding("Student Union",
                "Student Union Building, San Jose, CA 95112",
                "Eng.jpg", 700, 1100, 1050, 1300);

        dataSource.addBuilding("BBC",
                "Boccardo Business Complex, San Jose, CA 95112",
                "Eng.jpg", 1150, 1300, 1300, 1500);

        dataSource.addBuilding("South Parking Garage",
                " San Jose State University South Garage, " +
                        "330 South 7th Street, San Jose, CA 95112",
                "Eng.jpg", 400, 2050, 700, 2400);


        final EditText searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            searchBuildingByName(searchText.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

//        Button detailButton = (Button) findViewById(R.id.detail_button);
//
//        View.OnClickListener listener = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapActivity.this, BuildingDetailActivity.class);
//                String buildingName = "King Library";
//                intent.putExtra("buildingName", buildingName);
//                startActivity(intent);
//            }
//        };
//
//        detailButton.setOnClickListener(listener);
    }

    private void searchBuildingByName(String name) {
        dataSource.open();
        Building building = dataSource.findBuildingByName(name);
        if (building == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry! No match buildings found.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            final AlertDialog emptyResultAlert = builder.create();
            emptyResultAlert.show();
        }
        else {
//            buildingText.setText(building.toString());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("getX() = " + x);
//        System.out.println("getY() = " + y);
//
//        x = (int)event.getRawX();
//        y = (int)event.getRawY();
//        System.out.println("getRawX() = " + x);
//        System.out.println("getRawY() = " + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                dataSource.open();
                Building building = dataSource.findBuildingByLocation(x, y);
                if (building != null) {
                    Intent intent = new Intent(MapActivity.this, BuildingDetailActivity.class);
                    String buildingName = building.getName();
                    intent.putExtra("buildingName", buildingName);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

        return false;
    }
}

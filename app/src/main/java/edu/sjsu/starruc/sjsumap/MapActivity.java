package edu.sjsu.starruc.sjsumap;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MapActivity extends AppCompatActivity{
    private RelativeLayout layout;
    private DisplayMetrics metrics;
    private MapPinWidget buildingRectangle;
    private CurrentLocationWidget curLoc;
    private double latitude = 37.335848;
    private double longitude = -121.886157;

    protected String[] permissions;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private CampusManager campusManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        campusManager = new CampusManager();

        layout = (RelativeLayout) findViewById(R.id.activity_map);

        buildingRectangle = new MapPinWidget(this);
        buildingRectangle.setVisibility(View.INVISIBLE);
        layout.addView(buildingRectangle);

        curLoc = new CurrentLocationWidget(this);
        curLoc.setVisibility(View.INVISIBLE);
        layout.addView(curLoc);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                curLoc.setVisibility(View.INVISIBLE);

                if (campusManager.isInCampus(location)) {


                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    int[] newPos = campusManager.mapLocationToPixels(location);
                    curLoc.setX(newPos[0] - 30);
                    curLoc.setY(newPos[1] - 60);
                    curLoc.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        final EditText searchText = (EditText) findViewById(R.id.search_text);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    buildingRectangle.setVisibility(View.GONE);
                }

            }
        });
//
//        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(v.getId() == R.id.search_text && !hasFocus) {
//                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                }
//            }
//        });

        final Looper looper = null;
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText().toString().length() > 0) {
                    searchBuildingByName(searchText.getText().toString());
                }
            }
        });
    }

    private void searchBuildingByName(String name) {
        Building building = campusManager.findBuildingByName(name);
        if (building == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry! No match buildings found.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {

//                            finish();
                        }
                    });

            final AlertDialog emptyResultAlert = builder.create();
            emptyResultAlert.show();
        }
        else {
//            buildingRectangle.setX(building.getCx() * 1380 / 579 - 10);
//            buildingRectangle.setY(building.getCy() * 2280 / 926 - 30);
            buildingRectangle.setX(building.getCx());
            buildingRectangle.setY(building.getCy() - 100);
            buildingRectangle.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.search_text).getWindowToken(), 0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                Building building = campusManager.findBuildingByLocation(x, y);
                if (building != null) {
                    Intent intent = new Intent(MapActivity.this, BuildingDetailActivity.class);
                    String buildingName = building.getName();
                    intent.putExtra("buildingName", buildingName);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);

                    startActivity(intent);
                }
                break;
            default:
                break;
        }

        return false;
    }

}

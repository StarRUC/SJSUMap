package edu.sjsu.starruc.sjsumap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity implements LocationListener {
    private RelativeLayout layout;
    private RelativeLayout.LayoutParams layoutParams;
    private DisplayMetrics metrics;
    private DrawView buildingRectangle;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private BuildingManager buildingManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        layout = (RelativeLayout) findViewById(R.id.activity_map);
        buildingRectangle = new DrawView(this);
        buildingRectangle.setVisibility(View.INVISIBLE);
        layout.addView(buildingRectangle);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }

        buildingManager = new BuildingManager();

        final EditText searchText = (EditText) findViewById(R.id.search_text);
        final TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    buildingRectangle.setVisibility(View.GONE);
                }
            }
        };
        searchText.addTextChangedListener(textWatcher);

        searchText.setOnKeyListener(new EditText.OnKeyListener()
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

    }

    private void searchBuildingByName(String name) {
        Building building = buildingManager.findBuildingByName(name);
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
            buildingRectangle.setX(building.getCx() * metrics.widthPixels / 1440);
            buildingRectangle.setY(building.getCy() * metrics.heightPixels / 2560 - 50);
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
                Building building = buildingManager.findBuildingByLocation(x, y);
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

    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}

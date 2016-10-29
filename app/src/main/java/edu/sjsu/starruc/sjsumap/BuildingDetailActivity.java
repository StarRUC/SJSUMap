package edu.sjsu.starruc.sjsumap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BuildingDetailActivity extends AppCompatActivity {
    private BuildingDataSource dataSource;
    private String url_first = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
    private String url_second = "&destinations=";
    private String api_key = "&key=AIzaSyCTVoZI8aOu9zn9TKc2TJtTh8fOSxXkDBI";
    private String api_key2 = "&key=AIzaSyAb9-5iPgr7Ztg9PTokecodWQ9hZcnDbRs";
    private String api_key_location = "&key=AIzaSyD1s6smr584UMxRRG9FCkBqbfRaXxlLpso";
    private String url_location1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String address = null;
    double buildingLongitude = -121.88424124;;
    double buildingLatitude = 37.33342877;;
    double userLongitude = -122.036078;
    double userLatitude = 37.55968;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        TextView buildingNameTextView = (TextView) findViewById(R.id.building_name);
        TextView buildingAddressTextView = (TextView) findViewById(R.id.address);
        TextView distanceTextView = (TextView) findViewById(R.id.distance);
        TextView travelTimeTextView = (TextView) findViewById(R.id.travelTime);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dataSource = new BuildingDataSource(this);
        dataSource.open();
        String buildingName = getIntent().getStringExtra("buildingName");
        userLatitude = getIntent().getDoubleExtra("latitude", 1.2);
        userLongitude = getIntent().getDoubleExtra("longitude", 1.2);
        CampusManager campusManager = new CampusManager();
        Building building = campusManager.findBuildingByName(buildingName);
        buildingLatitude = building.getLat();
        buildingLongitude = building.getLng();





        dataSource = new BuildingDataSource(this);
        dataSource.open();

        if (building == null) {
            buildingNameTextView.setText("No buildings available");
        } else {
            address = building.getAddress();
            buildingNameTextView.setText(buildingName);
            buildingAddressTextView.setText(address);
            String strUrl = null;
            try {
                strUrl = url_first + userLatitude + "," + userLongitude + url_second + building.getLat() + "," + building.getLng() + api_key;
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;

            try {

                URL url = new URL(strUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                String response = this.convertStreamToString(in);

                JSONObject jsonResponse = this.convertStringToJson(response);
                String rows = jsonResponse.getString("rows");
                JSONObject jsonrows = this.convertStringToJson(rows.substring(1, rows.length() - 1));
                String elements = jsonrows.getString("elements");
                JSONObject jsonElements = this.convertStringToJson(elements.substring(1, elements.length() - 1));

                JSONObject jsonDistance = this.convertStringToJson(jsonElements.getString("distance"));
                JSONObject jsonDuration = this.convertStringToJson(jsonElements.getString("duration"));
                String distance = jsonDistance.getString("text");
                String duration = jsonDuration.getString("text");
                distanceTextView.setText(distance);
                travelTimeTextView.setText(duration);
                streetButtonListener();
                getImage();
            } catch (Exception e) {
                System.out.print(e.toString());
            } finally {
                conn.disconnect();
            }
        }
        dataSource.close();

    }
    private void getImage() {
        HttpURLConnection conn = null;
        try {
            String strUrl = "https://maps.googleapis.com/maps/api/streetview?size=150x150&location="
                    + buildingLatitude + "," + buildingLongitude + "&heading=260.78&pitch=-0.76" + api_key_location;
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            Drawable d = Drawable.createFromStream(in, "image");
            //BufferedReader r = new BufferedReader(new InputStreamReader(in));
            ImageView imageView = (ImageView) findViewById(R.id.building_image);
            imageView.setImageDrawable(d);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }


    private void streetButtonListener() {
        Button streetViewBtn = (Button) findViewById(R.id.streetview);
        streetViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(BuildingDetailActivity.this, StreetviewActivity.class);

                    intent.putExtra("lat", buildingLatitude);
                    intent.putExtra("lng", buildingLongitude);
                    startActivity(intent);

            }
        });
    }


    protected static JSONObject convertStringToJson(String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return jsonObject;
    }

    protected static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }
}

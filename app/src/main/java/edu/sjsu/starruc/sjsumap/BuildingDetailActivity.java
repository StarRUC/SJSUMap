package edu.sjsu.starruc.sjsumap;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        TextView buildingNameTextView = (TextView) findViewById(R.id.building_name);
        TextView distanceTextView = (TextView) findViewById(R.id.distance);
        TextView travelTimeTextView = (TextView) findViewById(R.id.travelTime);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dataSource = new BuildingDataSource(this);
        dataSource.open();
        String buildingName = getIntent().getStringExtra("buildingName");
        Building building = dataSource.findBuildingByName(buildingName);
        double longitude = -121.884999;//getIntent().getDoubleExtra("user_longitude",1.2);
        double latitude = 37.335507;//getIntent().getDoubleExtra("user_latitude", 1.2);
        String address = building.getAddress();


        dataSource = new BuildingDataSource(this);
        dataSource.open();

        if (building == null) {
            buildingNameTextView.setText("No buildings available");
        }
        else {
            buildingNameTextView.setText(buildingName);
            String strUrl = null;
            try {
                strUrl = url_first + latitude + "," + longitude + url_second + URLEncoder.encode(building.getAddress(), "utf-8") + api_key;
            } catch (UnsupportedEncodingException e) {
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
                JSONObject jsonrows = this.convertStringToJson(rows.substring(1, rows.length()-1));
                String elements = jsonrows.getString("elements");
                JSONObject jsonElements = this.convertStringToJson(elements.substring(1, elements.length()-1));

                JSONObject jsonDistance = this.convertStringToJson(jsonElements.getString("distance"));
                JSONObject jsonDuration = this.convertStringToJson(jsonElements.getString("duration"));
                String distance = jsonDistance.getString("text");
                String duration = jsonDuration.getString("text");
                distanceTextView.setText(distance);
                travelTimeTextView.setText(duration);
            } catch (Exception e) {
                System.out.print(e.toString());
            } finally {
                conn.disconnect();
            }
        }
        dataSource.close();

    }

    private JSONObject convertStringToJson(String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return jsonObject;
    }

    private String convertStreamToString(InputStream inputStream) throws IOException {
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

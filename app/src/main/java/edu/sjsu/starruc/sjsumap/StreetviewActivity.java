package edu.sjsu.starruc.sjsumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import android.view.ViewGroup.LayoutParams;


public class StreetviewActivity extends AppCompatActivity {
    private StreetViewPanoramaView mStreetViewPanoramaView;
    private StreetViewPanorama mPanorama;
    protected static double lng;
    protected static double lat;
    /**
     * King Library 37.3358992, -121.8855567
     * Engineering Building  37.33773655, -121.8818146
     * Yoshihiro Uchida Hall 37.33342877 121.88424124
     * Student Union Building 37.33686433 -121.87818825
     * BBC 37.33691724 -122.036078
     * South Parking Garage  37.3334663 -121.8799324
     */
    private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streetview);
        lng = getIntent().getDoubleExtra("lng",1.2);
        lat = getIntent().getDoubleExtra("lat", 1.2);
        setTitle("Street View");
        mStreetViewPanoramaView = (StreetViewPanoramaView) findViewById(R.id.steet_view_panorama);
        mStreetViewPanoramaView.onCreate(savedInstanceState);
        mStreetViewPanoramaView.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                panorama.setPosition(new LatLng(StreetviewActivity.lat, StreetviewActivity.lng));
                mPanorama = panorama;
            }
        });
        //setContentView(R.layout.activity_streetview);
       /* StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        if (savedInstanceState == null) {
            options.position(SYDNEY);
        }
        mStreetViewPanoramaView = (StreetViewPanoramaView) findViewById(R.id.steet_view_panorama);
       // mStreetViewPanoramaView = new StreetViewPanoramaView(this, options);
        addContentView(mStreetViewPanoramaView,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // *** IMPORTANT ***
        // StreetViewPanoramaView requires that the Bundle you pass contain _ONLY_
        // StreetViewPanoramaView SDK objects or sub-Bundles.
        Bundle mStreetViewBundle = null;
        if (savedInstanceState != null) {
            mStreetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
        }
        mStreetViewPanoramaView.onCreate(mStreetViewBundle);*/
    }

    @Override
    protected void onResume() {
        mStreetViewPanoramaView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mStreetViewPanoramaView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mStreetViewPanoramaView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle);
        }

        mStreetViewPanoramaView.onSaveInstanceState(mStreetViewBundle);
    }
}

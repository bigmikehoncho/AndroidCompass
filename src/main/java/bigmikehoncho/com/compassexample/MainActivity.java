package bigmikehoncho.com.compassexample;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import bigmikehoncho.com.androidcompass.Compass;

public class MainActivity extends AppCompatActivity {
    
    private static final String STATE_LOC_START = "start";
    private static final String STATE_LOC_DEST = "dest";
    
    private Compass mCompass;
    private Location mLocStart;
    private Location mLocDestination;
    private ImageView mIVCompassNeedle;
    private TextView mTVDistance;
    private TextView mTVStartLat;
    private TextView mTVStartLng;
    private TextView mTVDestLat;
    private TextView mTVDestLng;
    
    
    private Compass.CompassListener mCompassListener = new Compass.CompassListener() {
        @Override
        public void onDistanceCalculated(float distanceInMeters) {
            mTVDistance.setText(getString(R.string.distance, distanceInMeters));
        }

        @Override
        public void onCompassFailed(int error) {
            switch (error){
                case Compass.SENSORS_NOT_FOUND:
                    mTVDistance.setText(R.string.error_sensors_not_found);
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putParcelable(STATE_LOC_START, mLocStart);
        outState.putParcelable(STATE_LOC_DEST, mLocDestination);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mIVCompassNeedle = (ImageView) findViewById(R.id.image_compass);
        mTVDistance = (TextView) findViewById(R.id.text_distance);
        mTVStartLat = (TextView) findViewById(R.id.text_start_lat);
        mTVStartLng = (TextView) findViewById(R.id.text_start_lng);
        mTVDestLat = (TextView) findViewById(R.id.text_dest_lat);
        mTVDestLng = (TextView) findViewById(R.id.text_dest_lng);

        mCompass = new Compass(this, mIVCompassNeedle, mCompassListener);

        if(savedInstanceState == null) {
            mLocStart = new Location("provider");
            mLocStart.setLatitude(50);
            mLocStart.setLongitude(50);
            mLocDestination = new Location("provider");
            mLocDestination.setLatitude(180);
            mLocDestination.setLongitude(0);
        } else {
            mLocStart = savedInstanceState.getParcelable(STATE_LOC_START);
            mLocDestination = savedInstanceState.getParcelable(STATE_LOC_DEST);
        }
        
        mCompass.setStartLocation(mLocStart);
        mCompass.setDestinationLocation(mLocDestination);
        
        setLocationViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        mCompass.initSensors();
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        mCompass.stopSensors();
    }
    
    private void setLocationViews(){
        mTVStartLat.setText(getString(R.string.latitude, mLocStart.getLatitude()));
        mTVStartLng.setText(getString(R.string.latitude, mLocStart.getLongitude()));
        mTVDestLat.setText(getString(R.string.latitude, mLocDestination.getLatitude()));
        mTVDestLng.setText(getString(R.string.latitude, mLocDestination.getLongitude()));
    }
}

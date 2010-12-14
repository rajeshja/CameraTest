package rja.android.camera;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class CameraTest extends Activity
{
	private CameraView cameraView;

	private AROverlay overlay;

	private String currentLocationProvider = LocationManager.GPS_PROVIDER;

	private LocationManager locManager;

	private static final String LOG_CAT = "CameraTest";

	private LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				//this.updatedLocation = location;
				Log.d(LOG_CAT, "Got location: " + "Latitude: " + location.getLatitude() 
									   + ". Longitude: " + location.getLongitude());
				if (location != null) {
					overlay.setMessage("Latitude: " + location.getLatitude() 
									   + ". Longitude: " + location.getLongitude());
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}

		};

	//private Location updatedLocation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		cameraView = (CameraView) findViewById(R.id.camera_preview);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		cameraView.setMetrics(metrics);

		this.overlay = new AROverlay(this);
		cameraView.setOverlay(overlay);
		addContentView(overlay, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		this.overlay = overlay;
	
    }

	@Override
	public void onPause() {
		super.onPause();

		Log.d(LOG_CAT, "Releasing the camera.");

		cameraView.releaseCamera();
		removeLocationUpdates();
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d(LOG_CAT, "Reopening the camera.");
		
		cameraView.reopenCamera();

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		List<String> providers = locManager.getAllProviders();

		for(String provider: providers) {
			Log.d(LOG_CAT, "Location Provider: " + provider);
		}

		Criteria locCriteria = new Criteria();
		locCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		locCriteria.setCostAllowed(true);
		locCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		//locCriteria.setBearingRequired(true);

		Log.d(LOG_CAT, "Criteria: accuracy = " + locCriteria.getAccuracy());
		Log.d(LOG_CAT, "Criteria: altitude = " + locCriteria.isAltitudeRequired());
		Log.d(LOG_CAT, "Criteria: bearing = " + locCriteria.isBearingRequired());
		Log.d(LOG_CAT, "Criteria: cost = " + locCriteria.isCostAllowed());
		Log.d(LOG_CAT, "Criteria: power = " + locCriteria.getPowerRequirement());
		Log.d(LOG_CAT, "Criteria: speed = " + locCriteria.isSpeedRequired());

		String bestProvider = locManager.getBestProvider(locCriteria, false);
		Log.d(LOG_CAT, "Best provider is: " + bestProvider);
		
		LocationProvider provider = locManager.getProvider(bestProvider);

		requestLocationUpdates();
	}

	public void onClick(View v) {
		//AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		//alertBuilder.setMessage("Clicked the preview");
		//
		//AlertDialog dialog = alertBuilder.show();


		if (currentLocationProvider.equals(LocationManager.GPS_PROVIDER)) {
			currentLocationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			currentLocationProvider = LocationManager.GPS_PROVIDER;
		}

		removeLocationUpdates();

		overlay.setMessage("Getting coordinates...");
		Toast.makeText(getApplicationContext(), currentLocationProvider, Toast.LENGTH_SHORT).show();
		
		requestLocationUpdates();
	}

	private void requestLocationUpdates() {
		try {
			locManager.requestLocationUpdates(currentLocationProvider, 1000, 0, locationListener);
		} catch (Throwable e) {
			Log.e(LOG_CAT, "There was an error requesting location updates.", e);
		}
	}

	private void removeLocationUpdates() {
		locManager.removeUpdates(locationListener);
	}

}

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
					String[] msg = new String[3];

					msg[0] = "Latitude: " + location.getLatitude() 
						+ ". Longitude: " + location.getLongitude();
					msg[1] = "Provider is " + location.getProvider() + ", with accuracy " 
						+ location.getAccuracy();
					msg[2] = "Bearing is " + location.getBearing() + ", and altitude is "
						+ location.getAltitude();
					overlay.setMessage(msg);
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.d(LOG_CAT, "Status changed for " + provider + ", and status is " + status);
				Log.d(LOG_CAT, "Bundles are: " + extras);
			}
			
			public void onProviderEnabled(String provider) {
				Log.d(LOG_CAT, "Enabled " + provider);
			}

			public void onProviderDisabled(String provider) {
				Log.d(LOG_CAT, "Disabled " + provider);
			}

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
		addContentView(overlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

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

		currentLocationProvider = locManager.getBestProvider(locCriteria, false);
		Log.d(LOG_CAT, "Best provider is: " + currentLocationProvider);
		Toast.makeText(getApplicationContext(), currentLocationProvider, Toast.LENGTH_SHORT).show();
		
		LocationProvider provider = locManager.getProvider(currentLocationProvider);

		requestLocationUpdates();

		currentLocationProvider = LocationManager.NETWORK_PROVIDER;
		requestLocationUpdates();
	}

	private void requestLocationUpdates() {
		try {
			locManager.requestLocationUpdates(currentLocationProvider, 0, 0, locationListener);
		} catch (Throwable e) {
			Log.e(LOG_CAT, "There was an error requesting location updates.", e);
		}
	}

	private void removeLocationUpdates() {
		locManager.removeUpdates(locationListener);
	}

}

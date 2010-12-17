package rja.android.camera;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
	
	private SensorManager manager;

	private SensorEventListener accelListener;
	private SensorEventListener compassListener;
	
	private float[] accelValues;

	private float[] compassValues;

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
					overlay.setLocation(msg);
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

		manager.unregisterListener(accelListener);
		manager.unregisterListener(compassListener);
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

		Log.v(LOG_CAT, "Criteria: accuracy = " + locCriteria.getAccuracy());
		Log.v(LOG_CAT, "Criteria: altitude = " + locCriteria.isAltitudeRequired());
		Log.v(LOG_CAT, "Criteria: bearing = " + locCriteria.isBearingRequired());
		Log.v(LOG_CAT, "Criteria: cost = " + locCriteria.isCostAllowed());
		Log.v(LOG_CAT, "Criteria: power = " + locCriteria.getPowerRequirement());
		Log.v(LOG_CAT, "Criteria: speed = " + locCriteria.isSpeedRequired());

		currentLocationProvider = locManager.getBestProvider(locCriteria, false);
		Log.d(LOG_CAT, "Best provider is: " + currentLocationProvider);
		Toast.makeText(getApplicationContext(), currentLocationProvider, Toast.LENGTH_SHORT).show();
		
		LocationProvider provider = locManager.getProvider(currentLocationProvider);

		requestLocationUpdates();

		currentLocationProvider = LocationManager.NETWORK_PROVIDER;
		requestLocationUpdates();

		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);

		accelListener = new SensorEventListener() {
				public void onAccuracyChanged (Sensor sensor, int accuracy) {
					Log.d(LOG_CAT, sensor.getName() + " accuracy changed to " + accuracy);
				}

				public void onSensorChanged (SensorEvent event) {
					accelValues = event.values;
					Log.v(LOG_CAT, event.sensor.getName() + " values are " + event.values);
				}
			};

		Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

		compassListener = new SensorEventListener() {
				public void onAccuracyChanged (Sensor sensor, int accuracy) {
					Log.d(LOG_CAT, sensor.getName() + " accuracy changed to " + accuracy);
				}

				public void onSensorChanged (SensorEvent event) {
					compassValues = event.values;
					Log.v(LOG_CAT, event.sensor.getName() + " values are " + event.values);

					if (accelValues != null) {
						float[] R = new float[9];
						float[] I = new float[9];
		
						SensorManager.getRotationMatrix(R, I, accelValues, compassValues);

						Log.v("CompassListener", "R is " + R[0] + " " + R[1] + " " + R[2]);
						Log.v("CompassListener", "I is " + I[0] + " " + I[1] + " " + I[2]);

						float inclination = SensorManager.getInclination(I);

						float[] orientation = new float[3];
						orientation = SensorManager.getOrientation(R, orientation);

						overlay.setOrientation(R, I, orientation, inclination);
					}
				}
			};

		Sensor compass = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		manager.registerListener(compassListener, compass, SensorManager.SENSOR_DELAY_NORMAL);

		for(Sensor sensor: sensors) {
			Log.d(LOG_CAT, "Sensor found: " + sensor.getName());
		}


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

package rja.android.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;


public class CameraTest extends Activity
{
	private CameraView cameraView;

	private static final String LOG_CAT = "CameraTest";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		cameraView = (CameraView) findViewById(R.id.camera_preview);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		cameraView.setMetrics(metrics);

		AROverlay overlay = new AROverlay(this);

		cameraView.setOverlay(overlay);

		addContentView(overlay, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

	@Override
	public void onPause() {
		super.onPause();

		Log.d(LOG_CAT, "Releasing the camera.");

		cameraView.releaseCamera();
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d(LOG_CAT, "Reopening the camera.");
		
		cameraView.reopenCamera();
	}

	public void showAlert(View v) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setMessage("Clicked the preview");
		
		AlertDialog dialog = alertBuilder.show();
	}
}

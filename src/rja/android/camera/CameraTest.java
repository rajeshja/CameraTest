package rja.android.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;


public class CameraTest extends Activity
{
	private CameraView cameraView;

	private static final String LOG_CAT = "CameraTest";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

		cameraView = (CameraView) findViewById(R.id.camera_preview);
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
}

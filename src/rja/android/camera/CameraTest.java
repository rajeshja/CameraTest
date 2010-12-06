package rja.android.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class CameraTest extends Activity
{
	CameraView cameraView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		//cameraView = (CameraView) findViewById(R.id.camera_preview);
		cameraView = new CameraView(this);

		ViewGroup root = (ViewGroup) findViewById(R.id.root_view);

		root.addView(cameraView);
    }

	@Override
	public void onPause() {
		super.onPause();

		cameraView.releaseCamera();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		cameraView.reopenCamera();
	}
}

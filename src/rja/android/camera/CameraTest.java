package rja.android.camera;

import android.app.Activity;
import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.widget.TextView;

import java.util.List;

public class CameraTest extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Camera camera = Camera.open();
		Parameters cameraParams = camera.getParameters();
		List<Size> sizes = cameraParams.getSupportedPreviewSizes();
		
		StringBuffer sb = new StringBuffer();
		for(Size size: sizes) {
			sb.append(size.width + "x" + size.height + ", ");
		}

		TextView label = (TextView) findViewById(R.id.text);
		label.setText(sb.toString());
		
    }
}

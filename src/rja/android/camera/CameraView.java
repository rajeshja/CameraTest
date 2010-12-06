package rja.android.camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Describe class CameraView here.
 *
 *
 * Created: Mon Dec 06 17:46:56 2010
 *
 * @author <a href="mailto:rajeshja@D-174758"></a>
 * @version 1.0
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private Camera camera;

	public CameraView(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}


	public CameraView(Context context, AttributeSet  attrs) {
		super(context, attrs);

		getHolder().addCallback(this);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public CameraView(Context context, AttributeSet  attrs, int defStyle) {
		super(context, attrs, defStyle);

		getHolder().addCallback(this);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void releaseCamera() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	public void reopenCamera() {
		camera = camera.open();
	}

	public void  surfaceCreated(SurfaceHolder holder) {
		Camera camera = Camera.open();
		try {
			camera.setPreviewDisplay(getHolder());
		} catch (IOException e) {
			releaseCamera();
		}
	}

	public void  surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Parameters cameraParams = camera.getParameters();
		List<Size> sizes = cameraParams.getSupportedPreviewSizes();

		Size selectedSize;

		if ((sizes != null) && (sizes.size()>=0)) {
			selectedSize = sizes.get(0);
		} else {
			selectedSize = null;
		}

		for(Size size: sizes) {
			if ((size.width <= width) && (size.height <= height)) {
				if ((selectedSize == null)
					|| ((selectedSize.width < size.width)
						&& (selectedSize.height < size.height))) {
					selectedSize = size;
				}
			}
		}

		cameraParams.setPreviewSize(selectedSize.width, selectedSize.height);
		camera.setParameters(cameraParams);
		camera.startPreview();
	}

	public void  surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		releaseCamera();
	}
}

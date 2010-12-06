package rja.android.camera;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

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

	CameraView(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void  surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	public void  surfaceCreated(SurfaceHolder holder) {

	}

	public void  surfaceDestroyed(SurfaceHolder holder) {
		
	}
}

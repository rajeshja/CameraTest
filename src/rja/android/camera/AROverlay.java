package rja.android.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

/**
 * Describe class AROverlay here.
 *
 *
 * Created: Fri Dec 10 15:32:00 2010
 *
 * @author <a href="mailto:rajeshja@D-174758"></a>
 * @version 1.0
 */
public class AROverlay extends View {

	private String[] message = {"Hello"};

	private static final String LOG_CAT = "AROverlay";

	/**
	 * Creates a new <code>AROverlay</code> instance.
	 *
	 */
	public AROverlay(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLUE);
		paint.setTextSize(15.0f);

		Log.v(LOG_CAT, "Message length is " + message.length);

		for (int i=0; i<message.length; i++) {
			Log.v(LOG_CAT, "Message line " + i + " is " + message[i]);
			if (message[i] != null) {
				canvas.drawText(message[i], 10, 10 + (i*20), paint);
			}
		}

		super.onDraw(canvas);
	} 

	public void setLocation(String[] message) {

		if (message.length != 11) {
			this.message = new String[11];
		}
		this.message[0] = message[0];
		this.message[1] = message[1];
		this.message[2] = message[2];

		invalidate();
	}

	public void setOrientation(float[] R, float[] I, float[] orientation, float inclination) {
		if (message.length != 11) {
			this.message = new String[11];
		}

		Log.v(LOG_CAT, "Setting orientation");

		this.message[3] = "R is ";
		this.message[4] = "     ";
		this.message[5] = "     ";
		for (int i=0; i<R.length; i++) {
			this.message[3+(i/3)] += R[i] + ", ";
		}

		this.message[6] = "I is ";
		this.message[7] = "     ";
		this.message[8] = "     ";
		for (int i=0; i<I.length; i++) {
			this.message[6+(i/3)] += I[i] + ", ";
		}

		this.message[9] = "Orientation: " + orientation[0] + ", " + orientation[1] + ", " + orientation[2];
		this.message[10] = "Inclination: " + inclination;

		invalidate();

	}

	public void setMessage(String message) {
		this.message = new String[0];
		this.message[0] = message;
		invalidate();
	}

}

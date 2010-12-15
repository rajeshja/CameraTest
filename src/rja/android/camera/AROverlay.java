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

		for (int i=0; i<message.length; i++) {
			canvas.drawText(message[i], 10, 10 + (i*20), paint);
		}

		super.onDraw(canvas);
	} 

	public void setMessage(String[] message) {
		this.message = message;
		invalidate();
	}

	public void setMessage(String message) {
		this.message = new String[0];
		this.message[0] = message;
		invalidate();
	}

}

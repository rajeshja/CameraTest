package rja.android.camera;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.graphics.Paint;

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
		canvas.drawText("Hello", 10, 10, paint);

		super.onDraw(canvas);
	} 

}

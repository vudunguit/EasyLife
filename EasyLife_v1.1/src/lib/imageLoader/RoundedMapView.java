package lib.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.readystatesoftware.maps.OnSingleTapListener;

public class RoundedMapView extends MapView implements OnGestureListener {
	// double lat = 0;
	// double lng = 0;
	// Boolean first;
	// private TapControlledMapView mapView;
	private Bitmap windowFrame;
	// List<Overlay> mapOverlays;

	private GestureDetector gd;
	private OnSingleTapListener singleTapListener;

	Context context;

	/**
	 * Creates a Google Map View with rounded corners Constructor when created
	 * in XML
	 * 
	 * @param context
	 * @param attrs
	 */
	public RoundedMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// init();
		// first = false;
		this.context = context;
		setupGestures();
	}

	public RoundedMapView(Context context, String apiKey) {
		super(context, apiKey);
		setupGestures();
	}

	/**
	 * Creates a Google Map View with rounded corners Contructor when created in
	 * code
	 * 
	 * @param context
	 */
	public RoundedMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupGestures();
		// init();
	}

	private void setupGestures() {
		gd = new GestureDetector(this);

		// set the on Double tap listener
		gd.setOnDoubleTapListener(new OnDoubleTapListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (singleTapListener != null) {
					return singleTapListener.onSingleTap(e);
				} else {
					return false;
				}
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				RoundedMapView.this.getController().zoomInFixing(
						(int) e.getX(), (int) e.getY());
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}

		});
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// if (this.gd.onTouchEvent(ev)) {
	// return true;
	// } else {
	// return super.onTouchEvent(ev);
	// }
	// }

	public void setOnSingleTapListener(OnSingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Disallow ScrollView to intercept touch events.
			this.getParent().requestDisallowInterceptTouchEvent(true);
			break;

		case MotionEvent.ACTION_UP:
			// Allow ScrollView to intercept touch events.
			this.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}

		// Handle MapView's touch events.
		super.onTouchEvent(ev);
		return true;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas); // Call super first (this draws the map) we
									// then draw on top of it

		if (windowFrame == null) {
			createWindowFrame(); // Lazy creation of the window frame, this is
									// needed as we don't know the width &
									// height of the screen until draw time
		}

		canvas.drawBitmap(windowFrame, 0, 0, null);
	}

	protected void createWindowFrame() {
		windowFrame = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888); // Create a new image we will draw
											// over the map
		Canvas osCanvas = new Canvas(windowFrame); // Create a canvas to draw
													// onto the new image

		RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());
		RectF innerRectangle = new RectF(0, 0, getWidth(), getHeight());

		float cornerRadius = getWidth() / 18f; // The angle of your corners

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Anti alias allows for
														// smooth corners
		paint.setColor(Color.parseColor("#EDEDED")); // This is the color of
														// your activity
														// background
		osCanvas.drawRect(outerRectangle, paint);

		paint.setColor(Color.RED); // An obvious color to help debugging
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)); // A
																			// out
																			// B
																			// http://en.wikipedia.org/wiki/File:Alpha_compositing.svg
		osCanvas.drawRoundRect(innerRectangle, cornerRadius, cornerRadius,
				paint);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);

		windowFrame = null; // If the layout changes null our frame so it can be
							// recreated with the new width and height
	}
}
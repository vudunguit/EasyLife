package lib.imageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollTextView extends TextView {

	public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		rotate();
	}

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		rotate();
	}

	public ScrollTextView(Context context) {
		super(context);
		init();
		rotate();
	}

	private void rotate() {
		// TODO Auto-generated method stub
		setSelected(true);
	}

	private void init() {
		if (!isInEditMode()) {

		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

}
package lib.application;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Async task implementation that is safe to activity recreation on
 * configuration change or killing by TaskManager. The task holds activity
 * reference it's attached to. Attaching/Detaching activity is managed by
 * Application that have a cache of all async task that are in progress.
 * 
 * @author Anton Novikov
 */
public abstract class SafeAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, AsyncTaskResult<Result>> {

	protected static final String TAG = SafeAsyncTask.class.getSimpleName();

	protected Activity activity;

	private final MyApplication application;

	/**
	 * Creates an instance of SafeAsyncTask.
	 * 
	 * @param activity
	 *            activity the task is attached to
	 */
	public SafeAsyncTask(Activity activity) {
		this.activity = activity;
		this.application = (MyApplication) activity.getApplication();
	}

	/**
	 * Internal method used to attach/detach task from activity.
	 * 
	 * @param activity
	 *            activity instance being attached/detached
	 */
	final void setActivity(Activity activity) {
		if (activity == null) {
			onActivityDetached();
			this.activity = null;
		} else {
			this.activity = activity;
			onActivityAttached();
		}
	}

	/**
	 * Callback that is called on activity attaching.
	 */
	protected void onActivityAttached() {
	}

	/**
	 * Callback that is called on activity detaching.
	 */
	protected void onActivityDetached() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPreExecute() {
		application.addTask(activity, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPostExecute(AsyncTaskResult<Result> result) {
		application.removeTask(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCancelled() {
		application.removeTask(this);
	}

}

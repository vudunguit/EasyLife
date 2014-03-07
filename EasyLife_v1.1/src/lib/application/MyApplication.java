package lib.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

public class MyApplication extends Application {

	private Map<String, List<SafeAsyncTask<?, ?, ?>>> activityTaskCache;

	public String myNumber = "";
	public String DeviceId = "";
	public String versionName = "";

	public int displayWidth;

	public boolean b_login_check = false;

	public String userID = "";
	public String pinCode = "";

	public Context context = null;
//	public TabpageActivity activity_main;

	@Override
	public void onCreate() {
		super.onCreate();

		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		myNumber = mTelephonyMgr.getLine1Number();
		DeviceId = mTelephonyMgr.getDeviceId();

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		displayWidth = display.getWidth();

		this.activityTaskCache = new HashMap<String, List<SafeAsyncTask<?, ?, ?>>>();

		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Removes async task from cache when it is finished or canceled.
	 * 
	 * @param task
	 *            async task instance being removed
	 */
	public void removeTask(SafeAsyncTask<?, ?, ?> task) {
		for (Entry<String, List<SafeAsyncTask<?, ?, ?>>> entry : activityTaskCache
				.entrySet()) {
			List<SafeAsyncTask<?, ?, ?>> tasks = entry.getValue();
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i) == task) {
					tasks.remove(i);
					break;
				}
			}

			if (tasks.isEmpty()) {
				activityTaskCache.remove(entry.getKey());
				return;
			}
		}
	}

	/**
	 * Adds task to cache when it is started.
	 * 
	 * @param activity
	 *            activity instance to attach task to
	 * @param task
	 *            task being cached
	 */
	public void addTask(Activity activity, SafeAsyncTask<?, ?, ?> task) {
		String key = activity.getClass().getName();
		List<SafeAsyncTask<?, ?, ?>> tasks = activityTaskCache.get(key);
		if (tasks == null) {
			tasks = new ArrayList<SafeAsyncTask<?, ?, ?>>();
			activityTaskCache.put(key, tasks);
		}

		tasks.add(task);
	}

	/**
	 * Detaching activity from async task when it is going to be recreated.
	 * 
	 * @param activity
	 *            activity being detached
	 */
	public void detach(Activity activity) {
		List<SafeAsyncTask<?, ?, ?>> tasks = activityTaskCache.get(activity
				.getClass().getName());
		if (tasks != null) {
			for (SafeAsyncTask<?, ?, ?> task : tasks) {
				task.setActivity(null);
			}
		}
	}

	/**
	 * Attaching activity to async task when it is restored the state.
	 * 
	 * @param activity
	 *            activit being attached
	 */
	public void attach(Activity activity) {
		List<SafeAsyncTask<?, ?, ?>> tasks = activityTaskCache.get(activity
				.getClass().getName());
		if (tasks != null) {
			for (SafeAsyncTask<?, ?, ?> task : tasks) {
				task.setActivity(activity);
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}

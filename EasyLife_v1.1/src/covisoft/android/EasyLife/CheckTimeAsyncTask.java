package covisoft.android.EasyLife;

import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;


public class CheckTimeAsyncTask extends TimerTask {
	
	Activity activity;
	@SuppressWarnings("rawtypes")
	AsyncTask async;
	
    @SuppressWarnings("rawtypes")
	public CheckTimeAsyncTask(Activity activity, AsyncTask async) {
		super();
		this.async = async;
		this.activity = activity;
	}

	@Override
    public void run() {
         
		 
         activity.runOnUiThread(new Runnable() {
             @Override
             public void run() {
            	 
            	 async.cancel(true);
             }
         });
    }
}

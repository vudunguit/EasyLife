package covisoft.android.tab5;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import covisoft.android.EasyLife.MyCountDownTimer;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Manual extends NavigationActivity implements OnPreparedListener, OnErrorListener  {

	public VideoView vv;
	private boolean readyToPlay;
	private Button btnPlay;
	private Button btnStop;
	private ImageButton btnVolume;
	private ProgressBar progress_Volume;
	private ProgressBar progress_Volume_invi;
	private RelativeLayout layout_volume_invi;
	private SeekBar seekbar;
	private SeekBar seekbarInvi;
	
	private CountDownTimer timer;
	private CountDownTimer timerVolume;
	private Vibrator vibrator;
	static long currentDuration = 0 ;
	boolean isStop = false; 

	private String uri = "";
	int oldPosition = 0;
	Boolean loading = true;
	private TextView txtCurrentTime;
	private TextView txtDurationTime;
	
	
	Activity activity;
	int type = 0;
	
	LinearLayout layout_Back;

	Dialog dialog;
	
	private Boolean isLoading = false;
	
	// ---------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manual);

		activity = this;
		
		Intent intent = getIntent();
		uri = intent.getStringExtra("Link");
		
		timerVolume = new MyCountDownTimer(10000, 500) {
			
			@Override
			public void onFinish() {
				
				if (layout_volume_invi.getVisibility() == View.VISIBLE) {
					btnVolume.performClick();
				}
				
				
			}
		};
		
		if(EZUtil.isNetworkConnected(activity)) {
			init_MediaFile();
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (vv.isPlaying()) {
			
			btnStop.performClick();
			oldPosition = vv.getCurrentPosition();
			
		}
		
		vv.setVisibility(View.INVISIBLE);
		super.onPause();
	}
	
	ProgressDialog progressDialog;

	void init_progressDialog() {
		if(!isLoading) {
			isLoading = true;
			if(getParent() != null) {
				progressDialog = ProgressDialog.show(getParent(), "", "Loading...",	true, true);
			} else {
				progressDialog = ProgressDialog.show(activity, "", "Loading...",	true, true);
			}
			progressDialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if(isLoading) {
						progressDialog.show();
					}
				}
			});
		}
		
	}
	
	public void init_MediaFile() {
		init_progressDialog();
		
		txtCurrentTime = (TextView)findViewById(R.id.txt_CurrentTime);
		txtDurationTime = (TextView)findViewById(R.id.txt_DurationTime);
		
		vv = (VideoView)activity.findViewById(R.id.videoView);
		
		/* variables init */
		if (vv != null && vv.getVisibility()==View.INVISIBLE) {
			vv.setVisibility(View.VISIBLE);
		}
		// listeners for VideoView:
		vv.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				loading = false;
				progressDialog.dismiss();
				
				return true;
			}
		});
		vv.setOnPreparedListener(Activity_Manual.this);
		
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playMedia(btnPlay);
			}
		});
		btnStop = (Button) findViewById(R.id.btnPause);
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playMedia(btnStop);
			}
		});
		
		seekbar = (SeekBar)	findViewById(R.id.seekbar);
		seekbarInvi = (SeekBar)	findViewById(R.id.seekbarInvi);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		readyToPlay = false;
		init_BtnVolume();
		initMedia();
	}
	
	public void init_BtnVolume() {
		layout_volume_invi = (RelativeLayout)findViewById(R.id.layout_volume_info);
		progress_Volume = (ProgressBar)findViewById(R.id.progress_volume);
		progress_Volume_invi = (ProgressBar)findViewById(R.id.progress_volume_invi);
		
		progress_Volume.setOnTouchListener(new OnTouchListener() {

			// enables changing of the current playback position
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProgressBar pb = (ProgressBar) v;

				int newPosition =  100 - (int)(100 * event.getY() / pb.getHeight());
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						timerVolume.cancel();
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						timerVolume.start();
						pb.setProgress(newPosition);
						progress_Volume_invi.setProgress(newPosition);
						AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						audio.setStreamVolume(AudioManager.STREAM_MUSIC, newPosition*15/100, 0);
						
						break;
				}
				return true;
			}
		});
		progress_Volume_invi.setOnTouchListener(new OnTouchListener() {

			// enables changing of the current playback position
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProgressBar pb = (ProgressBar) v;

				int newPosition =  100 - (int)(100 * event.getY() / pb.getHeight());
				
				switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						timerVolume.cancel();
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						timerVolume.start();
						pb.setProgress(newPosition);
						progress_Volume.setProgress(newPosition);
						AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						audio.setStreamVolume(AudioManager.STREAM_MUSIC, newPosition*15/100, 0);
						break;
				}
				return true;
			}
		});
		btnVolume = (ImageButton)findViewById(R.id.btnVolume);
		btnVolume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
				
				double newVolume = (double)currentVolume/15;
				int a = (int)(newVolume * 100);
				
				if (layout_volume_invi.getVisibility() == View.GONE) {
					timerVolume.start();
					layout_volume_invi.setVisibility(View.VISIBLE);
					progress_Volume.setProgress(a);
					progress_Volume_invi.setProgress(a);
				} else {
					timerVolume.cancel();
					layout_volume_invi.setVisibility(View.GONE);
				}
				
			}
		});
	}
	
    /**
     * Set media content to VideoView and file name from URI entered by user.
     * 
     * @param v	View the touch event has been dispatched to
     */
	public void initMedia() {
		
		buttonVibrate();
		readyToPlay = true;
		
		stopMedia(null);
		
		try {
			vv.setVideoURI(Uri.parse(uri));
		} catch( Exception e) {
			
			showPopupOneOption(getString(R.string.popup_WrongLink), 2);
		}
		
		seekbar.setSecondaryProgress(100);
		seekbarInvi.setSecondaryProgress(100);
		
		vv.requestFocus();
		
	}
    /**
     * Pause and rewind to beginning of the media content
     * 
     * @param v	View the touch event has been dispatched to
     */
	public void stopMedia(View v) {
		
		Log.e("Stop Media","");
		buttonVibrate();
		if (btnPlay.getVisibility() == Button.GONE) {
			
			btnStop.setVisibility(Button.GONE);
			btnPlay.setVisibility(Button.VISIBLE);
			
		}

		vv.pause();
		vv.seekTo(0);
		if (timer != null) {
			timer.cancel();
		}
		seekbar.setProgress(0);
		seekbarInvi.setProgress(0);
	}
	public void buttonVibrate() {
		vibrator.cancel();
		vibrator.vibrate(50); // vibrate for 50ms
	}
    /**
     * Callback invoked when error occurs during buffering or playback
     * 
     * @param player 	MediaPlayer which error refers to
     * @param what		type of error
     * @param extra		more specific information about the error
     * 
     * @return TRUE when method handled the error, FALSE if didn't
     */
    public boolean onError(MediaPlayer player, int what, int extra) {
    	//progressDialog.dismiss();
    	return false;
    }
    
    
//    private MediaPlayer.OnErrorListener mErrorListener =
//            new MediaPlayer.OnErrorListener() {
	/**
	 * Callback invoked when media is ready for playback
	 * 
	 * @param mp	MediaPlayer that is ready
	 */
	public void onPrepared(MediaPlayer mp) {
		
		Log.d(this.getClass().getName(), "prepared");
		
		mp.setLooping(false);
		loading = false;
		progressDialog.dismiss();
		
		
		// video size check (media is a video if size is defined, audio if not)
		int h = mp.getVideoHeight();
		int w = mp.getVideoWidth();
		if (h != 0 && w != 0) {
			Log.d(this.getClass().getName(), "logo off");
		} else {
			Log.d(this.getClass().getName(), "logo on");
		}

		mp.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				
				return false;
			}
		});
		// onVideoSizeChangedListener declaration
		mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			// video size check (media is a video if size is defined, audio if
			// not)
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				if (width != 0 && height != 0) {
					Log.d(this.getClass().getName(), "logo off");
				} else {
					Log.d(this.getClass().getName(), "logo on");
				}
				
			}  
		});
  
		// onBufferingUpdateListener declaration
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			// show updated information about the buffering progress
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				
				seekbar.setSecondaryProgress(percent);
				seekbarInvi.setSecondaryProgress(percent);
			}
		});
		


		// onSeekCompletionListener declaration
		mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			// show current frame after changing the playback position
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				
				Log.e("seekComplete", "OK");
				
			}
		});

		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				
				vv.seekTo(0);
				playMedia(btnStop);
				seekbar.setProgress(0);
				seekbarInvi.setProgress(0);
				Log.e("Completion", "OK");
			}
		});
		readyToPlay = true;
		int time = vv.getDuration();
		int time_elapsed = vv.getCurrentPosition();
		
		
		seekbar.setProgress(time_elapsed);
		seekbarInvi.setProgress(time_elapsed);

		// update current playback time every 500ms until stop
		timer = new MyCountDownTimer(time, 100) {
			@Override
			public void onTick(long millisUntilFinished) {
				
				String text = countTime(vv.getCurrentPosition());
				txtCurrentTime.setText(text);
				float a = vv.getCurrentPosition();
				float b = vv.getDuration();
				
				seekbar.setProgress((int) (a / b * 100));
				seekbarInvi.setProgress((int) (a / b * 100));
				
			}

			@Override
			public void onFinish() {
			}
		};

		// onTouchListener declaration
		seekbar.setOnTouchListener(new OnTouchListener() {

			// enables changing of the current playback position
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProgressBar pb = (ProgressBar) v;

				int newPosition = (int) (100 * event.getX() / pb.getWidth());
				if (newPosition > pb.getSecondaryProgress()) {
					newPosition = pb.getSecondaryProgress();
				}

				switch (event.getAction()) {
				// update position when finger is DOWN/MOVED/UP
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					
					seekbarInvi.setProgress(newPosition);
					vv.seekTo((int) newPosition * vv.getDuration() / 100);
					
					break;
					
				}
				return true;
			}
		});
		// onTouchListener declaration
		seekbarInvi.setOnTouchListener(new OnTouchListener() {

			// enables changing of the current playback position
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProgressBar pb = (ProgressBar) v;

				int newPosition = (int) (100 * event.getX() / pb.getWidth());
				if (newPosition > pb.getSecondaryProgress()) {
					newPosition = pb.getSecondaryProgress();
				}

				switch (event.getAction()) {
				// update position when finger is DOWN/MOVED/UP
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					
						pb.setProgress(newPosition);
						seekbar.setProgress(newPosition);
						vv.seekTo((int) newPosition * vv.getDuration() / 100);
						
						break;
				}
				return true;
		
			}
		});
		txtCurrentTime.setText(countTime(time_elapsed));
		txtDurationTime.setText(countTime(time));
		playMedia(btnPlay);

	}
	/**
     * Start or Pause playback of media content
     * 
     * @param v	View the touch event has been dispatched to
     */
	public void playMedia(View v) {
		buttonVibrate();
		
		isLoading = false;
		progressDialog.dismiss();
		
		if (readyToPlay) {
			
			if (v == btnPlay) {
				
				isStop = false;
				btnPlay.setVisibility(Button.GONE);  
				btnStop.setVisibility(Button.VISIBLE);
				
				vv.start(); 
				timer.start();
				Log.e("play at: ", vv.getCurrentPosition() +"");
			} else if (v == btnStop) {
				
				if (!isStop) {
					btnPlay.setVisibility(Button.VISIBLE);
					btnStop.setVisibility(Button.GONE);
					
					vv.pause();
					timer.cancel();
					Log.e("pause at: ", vv.getCurrentPosition() +"");
				} else {
					playMedia(btnPlay);
				}
				
			} else {
				isStop = true;
				btnPlay.setVisibility(Button.VISIBLE);
				btnStop.setVisibility(Button.GONE);
				
				
				Log.e("stop at: ", vv.getCurrentPosition() +"");
				
				vv.pause();
				timer.cancel();
				
			}
		}
    }
	/**
     * Convert time from milliseconds into minutes and seconds, proper to media player
     * 
     * @param miliseconds	media content time in milliseconds
     * @return	time in format minutes:seconds
     */
    public String countTime(int miliseconds) {
    	String timeInMinutes = new String();
    	int minutes = miliseconds / 60000;
    	int seconds = (miliseconds % 60000)/1000;
    	timeInMinutes = minutes + ":" + (seconds<10?"0" + seconds:seconds);
		return timeInMinutes;
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			timerVolume.cancel();
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
			
			if (currentVolume > 0) {
				currentVolume -= 1;
			}
			
			double newVolume = (double)currentVolume/15;
			int a = (int)(newVolume * 100);
			Log.e("down", a+"");
			if (layout_volume_invi.getVisibility() == View.VISIBLE) {
				progress_Volume.setProgress(a);
				progress_Volume_invi.setProgress(a);
				
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				
			} else {
				btnVolume.performClick();
				progress_Volume.setProgress(a);
				progress_Volume_invi.setProgress(a);
				
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			}
		    return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			timerVolume.cancel();
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
			
			if (currentVolume < 15) {
				currentVolume += 1;
			}
			
			
			double newVolume = (double)currentVolume/15;
			int a = (int)(newVolume * 100);
			Log.e("up", a+"");
			if (layout_volume_invi.getVisibility() == View.VISIBLE) {
				progress_Volume.setProgress(a);
				progress_Volume_invi.setProgress(a);
				
				audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume , 0);
			} else {
				btnVolume.performClick();
				progress_Volume.setProgress(a);
				progress_Volume_invi.setProgress(a);
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			}
		    return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
		
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			timerVolume.start();
			
			return super.onKeyUp(keyCode, event);
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			timerVolume.start();
			
			return super.onKeyUp(keyCode, event);
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	public void showPopupOneOption(String content, int type) {
		
    	final int fiType = type;
		if(!isLoading) {
			isLoading = true;
			
			dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(content);

			Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
			btn_OK.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		

			dialog.getWindow().setLayout(500, 400);
			dialog.show();
			
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					isLoading = false;
					if(fiType == 2) {
						onBackPressed();
					}
				}
			});
		}
	}
	
}

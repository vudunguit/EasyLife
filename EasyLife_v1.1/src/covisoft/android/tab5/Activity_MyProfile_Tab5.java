package covisoft.android.tab5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lib.application.CropOption;
import lib.application.CropOptionAdapter;
import lib.imageLoader.ImageLoader_Rounded;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.SessionStore;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.ServerUtilities;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.item.item_Notification;
import covisoft.android.services.service_Notification;
import covisoft.android.services.service_User_Info;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_MyProfile_Tab5 extends NavigationActivity {

	private Activity activity;

	private ImageView imgAddPhoto;
	private Button btnEditPhoto;

	private TextView txtName;
	private TextView txtPhone;
	private TextView txtGender;

	private Button btnAccount;
	private Button btnLogout;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	private Timer timer;
  
	private ImageLoader_Rounded imageLoader;

	private Uri mImageCaptureUri;

	HttpResponse response;
	String imageByte = "";

	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	private ArrayList<item_Notification> arNotify = null;
// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.layout_logout, null);
		this.setContentView(viewToLoad);
		
		activity = this;

		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());

		init();

	}

	public void init() {
		
		initBtnBack();
		initBtnLogout();

		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}

	}

	public void initBtnBack() {
		LinearLayout layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String requestCode = intent.getStringExtra("requestCode");
		if (requestCode != null) {
			switch (Integer.parseInt(requestCode)) {
			case PICK_FROM_CAMERA:
				doCrop();

				break;

			case PICK_FROM_FILE:
				mImageCaptureUri = intent.getData();

				doCrop();

				break;

			case CROP_FROM_CAMERA:
				Bundle extras = intent.getExtras();

				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm
																			// is
																			// the
																			// bitmap
																			// object
					byte[] b = baos.toByteArray();
					imageByte = Base64.encodeToString(b, Base64.DEFAULT);

					byte[] decodedString = Base64.decode(imageByte, 1);
					Bitmap bitmapObj = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

					imgAddPhoto.setImageBitmap(bitmapObj);

				}

				File f = new File(mImageCaptureUri.getPath());

				if (f.exists())
					f.delete();

				BitmapDrawable drawable = (BitmapDrawable) imgAddPhoto.getDrawable();
				Bitmap photo = drawable.getBitmap();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is
																		// the
																		// bitmap
																		// object
				byte[] b = baos.toByteArray();
				imageByte = Base64.encodeToString(b, Base64.DEFAULT);

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://easylife.com.vn/Web_Mobile_New/api/uploadImage/?username=" + EasyLifeActivity.user.getUsername() + "&TypeCall=update");

				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("images[]", imageByte));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

				break;

			}
		}

		super.onResume();

	}

	public void initBtnLogout() {
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Unregister notification
				AsyncTask_UnRegisID unRegister = new AsyncTask_UnRegisID();
				unRegister.execute();
				
				Utility.mFacebook.setAccessExpires(-1);
				Utility.mFacebook.setAccessToken(null);
				
				if(Utility.mFacebook != null) {
					try {
						Utility.mFacebook.logout(activity);
						SessionStore.clear(activity);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				EasyLifeActivity.user = null;

				AsyncTask_Notify task = new AsyncTask_Notify();
				task.execute();
				
				settings = getSharedPreferences("UserName", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("username", "");
				editor.commit();

				settings = getSharedPreferences("Pass", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("pass", "");
				editor.commit();

				settings = getSharedPreferences("Save", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putBoolean("save", false);

				editor.commit();
				
				Intent intent = new Intent(Activity_MyProfile_Tab5.this, Activity_Login_Tab5.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory_2("Activity_Login_Tab5", intent);

			}
		});
	}
	
	private class AsyncTask_UnRegisID extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

//			GCMRegistrar.unregister(activity);
			if(EasyLifeActivity.user != null) {
				ServerUtilities.unregister(activity, EasyLifeActivity.regId, EasyLifeActivity.user.getUsername());
			}
			
			return null;
		}

	}

	public void initTextInfo() {
		txtName = (TextView) findViewById(R.id.txtName);
		txtName.setText(EasyLifeActivity.user.getName());

		txtPhone = (TextView) findViewById(R.id.txtPhone);
		txtPhone.setText(EasyLifeActivity.user.getTel());

		txtGender = (TextView) findViewById(R.id.txtGender);

		if (EasyLifeActivity.user.getGender().equals("M")) {
			txtGender.setText(getResources().getString(R.string.Male));
		} else if (EasyLifeActivity.user.getGender().equals("F")) {
			txtGender.setText(getResources().getString(R.string.FeMale));
		} else {
			txtGender.setText("");
		}

		btnAccount = (Button) findViewById(R.id.btnAccount);
		btnAccount.setText(EasyLifeActivity.user.getEmail());
		btnAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(Activity_MyProfile_Tab5.this, Activity_Account.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Account", intent);
			}
		});

		imgAddPhoto = (ImageView) findViewById(R.id.imgAddPhoto);
		imageLoader.DisplayImage(EasyLifeActivity.user.getImageUrl(), imgAddPhoto, R.drawable.add_image_profile, 300);

	}

	private class AsyncTaskRequestData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (getParent() != null) {
				EZUtil.init_progressDialog(getParent());
			} else {
				EZUtil.init_progressDialog(activity);
			}
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			if(EasyLifeActivity.user != null) {
				service_User_Info xml_UserInfo = new service_User_Info(activity, EasyLifeActivity.user.getUsername());
				xml_UserInfo.start();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if(EasyLifeActivity.user != null) {
				initTextInfo();
				init_Image();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

			timer.cancel();

			EZUtil.cancelProgress();
			
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
						public void run() {
							showPopupOneOption(activity.getResources().getString(R.string.No_Response), 1);
						}
					});
				}
			}, 500);
		}
	}

	public void showPopupOneOption(String content, int type) {

		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;

			final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(content);

			Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
			if (type == 1) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			} else if (type == 2) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
						onBackPressed();
					}
				});
			}

			dialog.getWindow().setLayout(500, 400);
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					EZUtil.isLoading = false;

				}
			});
		}
	}

	private void init_Image() {

		final String[] items = new String[] { "Take from camera", "Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_MyProfile_Tab5.this.getParent(), android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_MyProfile_Tab5.this.getParent());

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);

						getParent().startActivityForResult(intent, PICK_FROM_CAMERA);

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { // pick from file
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					getParent().startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);

				}
			}
		});

		final AlertDialog dialog = builder.create();
		imgAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
			}
		});

		btnEditPhoto = (Button) findViewById(R.id.btnEditPhoto);
		btnEditPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		Log.e("MyProfile_Tab5", "onSaveInstanceState is Called");
		savedInstanceState.putString("MyProfile_Tab5", "TakePhoto");
		// etc.
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		String saved = savedInstanceState.getString("MyProfile_Tab5");
        if(saved != null) {
        	Log.e("Home_Activity", saved);
        }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", requestCode + "");
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_CAMERA:
			doCrop();

			break;

		case PICK_FROM_FILE:
			mImageCaptureUri = data.getData();

			doCrop();

			break;

		case CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();

			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is
																		// the
																		// bitmap
																		// object
				byte[] b = baos.toByteArray();
				imageByte = Base64.encodeToString(b, Base64.DEFAULT);

				byte[] decodedString = Base64.decode(imageByte, 1);
				Bitmap bitmapObj = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

				imgAddPhoto.setImageBitmap(bitmapObj);

			}

			File f = new File(mImageCaptureUri.getPath());

			if (f.exists())
				f.delete();

			BitmapDrawable drawable = (BitmapDrawable) imgAddPhoto.getDrawable();
			Bitmap photo = drawable.getBitmap();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the
																	// bitmap
																	// object
			byte[] b = baos.toByteArray();
			imageByte = Base64.encodeToString(b, Base64.DEFAULT);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://easylife.com.vn/Web_Mobile_New/api/uploadImage/?username=" + EasyLifeActivity.user.getUsername() + "&TypeCall=update");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("images[]", imageByte));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}

			break;

		}
	}

	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

		int size = list.size();

		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			intent.setData(mImageCaptureUri);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

				getParent().startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						getParent().startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
					}
				});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (mImageCaptureUri != null) {
							getContentResolver().delete(mImageCaptureUri, null, null);
							mImageCaptureUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}

	private class AsyncTask_Notify extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			service_Notification xmlNotify;
			if(EasyLifeActivity.user != null) {
				xmlNotify = new service_Notification(activity, EasyLifeActivity.user.getUsername());
			} else {
				xmlNotify = new service_Notification(activity, "");
			}
			
			arNotify = xmlNotify.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (arNotify != null) {
				
				//==================================================================
		        if(arNotify.size() > 0 && arNotify.size() < 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText(arNotify.size() + "");
				} else if(arNotify.size() >= 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText("...");
				} else {
					TabpageActivity.rel_Notification.setVisibility(View.GONE);
					TabpageActivity.txt_Number.setText("0");
				}
				
				//==================================================================
			} else {
				TabpageActivity.rel_Notification.setVisibility(View.GONE);
				TabpageActivity.txt_Number.setText("0");
			}
			
			super.onPostExecute(result);
		}

	}
	
}

package covisoft.android.tab3_Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_ChangeLanguage extends NavigationActivity {
	
	private LinearLayout layout_Back;
	
	private RelativeLayout layout_VN;
	private CheckBox cb_VN;

	private RelativeLayout layout_EN;
	private CheckBox cb_EN;
	
	private Button btnDone;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
//  ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_changelanguage);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		init();
		super.onResume();

	}

	public void init() {
		initBtnBack();
		init_VN();
		init_EN();
		initBtnDone();
	}
	
	public void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
			}
		});
	}

	public void init_VN() {

		layout_VN = (RelativeLayout)findViewById(R.id.layout_VN);
		layout_VN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(cb_VN.isChecked()) {
					cb_VN.setChecked(true);
					cb_VN.setClickable(false);
					
					cb_EN.setChecked(false);
					cb_EN.setClickable(true);
				} else {
					cb_VN.setChecked(true);
					cb_VN.setClickable(false);
					
					cb_EN.setChecked(false);
					cb_EN.setClickable(true);
				}
			}
		});

		cb_VN = (CheckBox) findViewById(R.id.cb_VN);
		cb_VN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				if(cb_VN.isChecked()) {
					cb_VN.setChecked(true);
					cb_VN.setClickable(false);
					
					cb_EN.setChecked(false);
					cb_EN.setClickable(true);
				} else {
					cb_VN.setChecked(true);
					cb_VN.setClickable(false);
					
					cb_EN.setChecked(false);
					cb_EN.setClickable(true);
				}
				
			}
		});
		
		settings = getSharedPreferences("language", MODE_WORLD_WRITEABLE);
		String text = "";
		text = settings.getString("language", text);
		
		
		if (text.equals("vn")) {
			cb_VN.setChecked(true);
		} else {
			cb_VN.setChecked(false);
		}

	}

	public void init_EN() {
		
		layout_EN = (RelativeLayout)findViewById(R.id.layout_EN);
		layout_EN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(cb_EN.isChecked()) {
					cb_EN.setChecked(true);
					cb_EN.setClickable(false);
					
					cb_VN.setChecked(false);
					cb_VN.setClickable(true);
				} else {
					cb_EN.setChecked(true);
					cb_EN.setClickable(false);
					
					cb_VN.setChecked(false);
					cb_VN.setClickable(true);
				}
			}
		});
		
		cb_EN = (CheckBox) findViewById(R.id.cb_EN);
		cb_EN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cb_EN.isChecked()) {
					cb_EN.setChecked(true);
					cb_EN.setClickable(false);
					
					cb_VN.setChecked(false);
					cb_VN.setClickable(true);
				} else {
					cb_EN.setChecked(true);
					cb_EN.setClickable(false);
					
					cb_VN.setChecked(false);
					cb_VN.setClickable(true);
				}
			}
		});
		
		settings = getSharedPreferences("language", MODE_WORLD_WRITEABLE);
		String text = "";
		text = settings.getString("language", text);
		
		
		if (text.equals("en")) {
			cb_EN.setChecked(true);
		} else {
			cb_EN.setChecked(false);
		}
	}
	
	public void initBtnDone() {
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text  = "";
				if(cb_VN.isChecked()) {
					text = "vn";
				} else {
					text = "en";
				}
				
				settings = getSharedPreferences("language", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("language", text);
				editor.commit();
				
				Intent intent = new Intent(Activity_ChangeLanguage.this, TabpageActivity.class);
				startActivity(intent);
				System.exit(0);
				
				onBackPressed();
			}
		});
	}
	
}

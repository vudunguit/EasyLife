package covisoft.android.tab3_Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Privacy extends NavigationActivity {

	private TextView txt_Content;
	private TextView txt_Title;

	private String type = "";

	private LinearLayout layout_Back;

// ================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_privacy_term_content);

		Intent intent = getIntent();
		type = intent.getStringExtra("type");

		init();
	}

	public void init() {
		init_Content();
		init_Btn_Back();
	}

	public void init_Content() {

		txt_Content = (TextView) findViewById(R.id.txt_Content);
		txt_Title = (TextView) findViewById(R.id.title);

		if (type.equals("privacy")) {
			txt_Content.setText(R.string.privacy);
			txt_Title.setText(getString(R.string.setting5));
		} else if (type.equals("term")) {
			txt_Content.setText(R.string.use);
			txt_Title.setText(getString(R.string.setting4));
		} else if (type.equals("customerService")) {
			txt_Content.setText(R.string.customerService);
			txt_Title.setText(getString(R.string.setting3));
		} else if (type.equals("version")) {
			txt_Content.setText(R.string.VERSION);
			txt_Title.setText(getString(R.string.setting6));
		}

	}

	public void init_Btn_Back() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();

			}

		});

	}
}

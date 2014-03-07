package covisoft.android.tab3_Home;

import lib.imageLoader.ImageLoader;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_QRCoupon_Shop;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_QRCoupon_Shop extends NavigationActivity {
	
	private LinearLayout layout_Back;
	
	private item_QRCoupon_Shop qrCoupon;
	
//  ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_13_qrcoupon);

		Intent intent = getIntent();
		qrCoupon = (item_QRCoupon_Shop)intent.getSerializableExtra("qrCoupon");
		init();
	}

	public void init() {
		initBtnBack();
		init_Content();
	}
	
	public void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				onBackPressed();
			}
		});
	}

	
	void init_Content() {
		TextView txtName = (TextView)findViewById(R.id.txtName);
		txtName.setText(qrCoupon.getCouponName());
		
		ImageLoader imageLoader = new ImageLoader(getApplicationContext());
		LinearLayout ln_img = (LinearLayout) findViewById(R.id.ln_img);
		imageLoader.DisplayImage3(qrCoupon.getLinkImage(), ln_img, R.drawable.s_24_noimage, EZUtil.REQUIRED_SIZE_BIG, qrCoupon);
		
		TextView txt_Content = (TextView)findViewById(R.id.txt_Content);
		txt_Content.setText(qrCoupon.getCont());
		
		TextView txt_EndDate = (TextView)findViewById(R.id.txt_EndDate);
		txt_EndDate.setText(qrCoupon.getEndDate());
	}
}

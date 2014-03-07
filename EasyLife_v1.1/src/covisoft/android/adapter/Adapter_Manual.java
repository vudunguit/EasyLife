package covisoft.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Manual;
import covisoft.android.tab5.Activity_Manual;

public class Adapter_Manual extends BaseAdapter {

	private final ArrayList<item_Manual> values;
	private Activity activity;

	public Adapter_Manual(Activity activity, ArrayList<item_Manual> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public item_Manual getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return 1;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final int pos = position;

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_item_manual, parent, false);
			holder = new ViewHolder();
			holder.layout = (RelativeLayout) view.findViewById(R.id.layout);
			holder.layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(activity, Activity_Manual.class);
					intent.putExtra("Link", values.get(pos).getLink());
					activity.startActivity(intent);
				}
			});
			holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
			holder.txtTitle.setText(values.get(position).getTitle());

			view.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return (view);

	}

	class ViewHolder {
		RelativeLayout layout;
		TextView txtTitle;
	}
}

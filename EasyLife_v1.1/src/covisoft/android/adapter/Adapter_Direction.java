package covisoft.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.R;

public class Adapter_Direction extends BaseAdapter {

	private final ArrayList<String> values;
	Activity activity;

	public Adapter_Direction(Activity activity, ArrayList<String> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public String getItem(int position) {
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
			view = inflater.inflate(R.layout.listview_item_direction, parent, false);
			holder = new ViewHolder();
			holder.layout_Root = (LinearLayout) view.findViewById(R.id.root);
			holder.imgDirection = (ImageView) view.findViewById(R.id.imgDirection);
			holder.txtDescription = (TextView) view.findViewById(R.id.txtDescription);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String text = getUnsignedString(values.get(pos).toLowerCase());

		if (text.contains(activity.getResources().getString(R.string.direction_head))) {
			holder.imgDirection.setBackgroundResource(R.drawable.ic_head);
		} else if (text.contains(activity.getResources().getString(R.string.direction_left))) {
			holder.imgDirection.setBackgroundResource(R.drawable.ic_left);
		} else if (text.contains(activity.getResources().getString(R.string.direction_right))) {
			holder.imgDirection.setBackgroundResource(R.drawable.ic_right);
		} else if (text.contains(activity.getResources().getString(R.string.direction_straight))) {
			holder.imgDirection.setBackgroundResource(R.drawable.ic_straight);
		}

		holder.txtDescription.setText(values.get(pos));

		return (view);

	}

	class ViewHolder {
		LinearLayout layout_Root;
		ImageView imgDirection;
		TextView txtDescription;
	}

	public static String getUnsignedString(String s) {
		StringBuffer unsignedString = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			unsignedString.append(getUnsignedChar(s.charAt(i)));
		}
		return unsignedString.toString();
	}

	public static char getUnsignedChar(char c) {
		if (c == '\u00E1' || c == '\u00E0' || c == '\u1EA3' || c == '\u00E3' || c == '\u1EA1' || c == '\u0103' || c == '\u1EAF' || c == '\u1EB1' || c == '\u1EB3' || c == '\u1EB5' || c == '\u1EB7' || c == '\u00E2' || c == '\u1EA5' || c == '\u1EA7' || c == '\u1EA9' || c == '\u1EAB' || c == '\u1EAD') {
			return 'a';
		} else if (c == '\u00C1' || c == '\u00C0' || c == '\u1EA2' || c == '\u00C3' || c == '\u1EA0' || c == '\u0102' || c == '\u1EAE' || c == '\u1EB0' || c == '\u1EB2' || c == '\u1EB4' || c == '\u1EB6' || c == '\u00C2' || c == '\u1EA4' || c == '\u1EA6' || c == '\u1EA8' || c == '\u1EAA' || c == '\u1EAC') {
			return 'A';
		} else if (c == '\u00E9' || c == '\u00E8' || c == '\u1EBB' || c == '\u1EBD' || c == '\u1EB9' || c == '\u00EA' || c == '\u1EBF' || c == '\u1EC1' || c == '\u1EC3' || c == '\u1EC5' || c == '\u1EC7') {
			return 'e';
		} else if (c == '\u00C9' || c == '\u00C8' || c == '\u1EBA' || c == '\u1EBC' || c == '\u1EB8' || c == '\u00CA' || c == '\u1EBE' || c == '\u1EC0' || c == '\u1EC2' || c == '\u1EC4' || c == '\u1EC6') {
			return 'E';
		} else if (c == '\u00ED' || c == '\u00EC' || c == '\u1EC9' || c == '\u0129' || c == '\u1ECB') {
			return 'i';
		} else if (c == '\u00CD' || c == '\u00CC' || c == '\u1EC8' || c == '\u0128' || c == '\u1ECA') {
			return 'I';
		} else if (c == '\u00F3' || c == '\u00F2' || c == '\u1ECF' || c == '\u00F5' | c == '\u1ECD' || c == '\u00F4' || c == '\u1ED1' || c == '\u1ED3' || c == '\u1ED5' || c == '\u1ED7' || c == '\u1ED9' || c == '\u01A1' || c == '\u1EDB' || c == '\u1EDD' || c == '\u1EDF' || c == '\u1EE1' || c == '\u1EE3') {
			return 'o';
		} else if (c == '\u00D3' || c == '\u00D2' || c == '\u1ECE' || c == '\u00D5' | c == '\u1ECC' || c == '\u00D4' || c == '\u1ED0' || c == '\u1ED2' || c == '\u1ED4' || c == '\u1ED6' || c == '\u1ED8' || c == '\u01A0' || c == '\u1EDA' || c == '\u1EDC' || c == '\u1EDE' || c == '\u1EE0' || c == '\u1EE2') {
			return 'O';
		} else if (c == '\u00FA' || c == '\u00F9' || c == '\u1EE7' || c == '\u0169' | c == '\u1EE5' || c == '\u01B0' || c == '\u1EE9' || c == '\u1EEB' || c == '\u1EED' || c == '\u1EEF' || c == '\u1EF1') {
			return 'u';
		} else if (c == '\u00DA' || c == '\u00D9' || c == '\u1EE6' || c == '\u0168' | c == '\u1EE4' || c == '\u01AF' || c == '\u1EE8' || c == '\u1EEA' || c == '\u1EEC' || c == '\u1EEE' || c == '\u1EF0') {
			return 'U';
		} else if (c == '\u00FD' || c == '\u1EF3' || c == '\u1EF7' || c == '\u1EF9' || c == '\u1EF5') {
			return 'y';
		} else if (c == '\u00DD' || c == '\u1EF2' || c == '\u1EF6' || c == '\u1EF8' || c == '\u1EF4') {
			return 'Y';
		} else if (c == '\u0111') {
			return 'd';
		} else if (c == '\u0110') {
			return 'D';
		}
		return c;
	}

}

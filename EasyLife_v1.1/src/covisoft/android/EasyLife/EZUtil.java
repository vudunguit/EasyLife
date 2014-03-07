package covisoft.android.EasyLife;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Class control all Variable/Function use in all application
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *       - Delete: curCategoryNo, curSubCategoryNo, curSubCategoryName, curItemNo (another place just set value, didn't use)
 * 
 * 
 */

@SuppressLint("SimpleDateFormat")
public class EZUtil {

	public static String encoding = "UTF-8";
	
	public static int popup = 0;

	public static String db_name = "db_subnara.db";
	public static String db_act_general = "general";
	public static String db_act_franchise = "franchise";
	public static String db_act_favorite = "favorite";

	public static String category_code_1 = "4"; // giao duc
	public static String category_code_2 = "1"; // food
	public static String category_code_3 = "8"; // lam dep
	public static String category_code_4 = "9"; // beer
	public static String category_code_5 = "5"; // y te
	public static String category_code_6 = "10"; // giai tri
	public static String category_code_7 = "11"; // service
	public static String category_code_8 = "71"; //
	public static String category_code_9 = "73"; //

	/* Delaytime when request data */
	public static final int DelayTime = 15000;
	
	/* Number item of List View */
	public static final int LISTVIEW_NUMBER_INIT = 50;
	public static final int LISTVIEW_NUMBER_MORE = 10;

	/* Scale Image Size */
	public static int REQUIRED_SIZE_SMALL = 70;
	public static int REQUIRED_SIZE_MIDDLE = 320;
	public static int REQUIRED_SIZE_BIG = 640;
	
	/* Facebook Easylife Application ID */
	public static final String Facebook_APP_ID = "247529672050707";
	
	
	
// =========================================================================
	/* 
	 * Use to show progress Loading 
	 */
	public static Boolean isLoading = false;
	public static Dialog dialog;
	public static ProgressDialog progressDialog;
	public static void init_progressDialog(Context context) {
		if (!isLoading) {
			isLoading = true;
			progressDialog = ProgressDialog.show(context, "", "Loading...", true, true);
			progressDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (isLoading) {
						progressDialog.show();
					}
				}
			});
		}
	}
	public static void cancelProgress() {
		isLoading = false;
		progressDialog.dismiss();
	}
// ===========================================================================
	
	public static boolean isNetworkConnected(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Check date input is valid follow format
	 */
	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {
			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Check special character in Username
	 */
	public static int getSpecialCharacter(String s) {
		int theCount = 0;
		String specialChars = "/*!#$@%^&*()\"{}_[]|\\?/<>,";
		for (int i = 0; i < s.length(); i++) {
			if (specialChars.contains(s.substring(i, i + 1))) {
				theCount++;
			}
		}
		return theCount;
	}

	/*
	 * Check email is valid or not
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	/*
	 * ...
	 */
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	/*
	 * Check Uppercase in String
	 */
	public static Pattern UPPERCASE = Pattern.compile("[A-Z]+");
	public static boolean checkUppercase(String s) {
		if (s == null) {
			return false;
		} else {
			Matcher m = UPPERCASE.matcher(s);
			return m.matches();
		}
	}
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
}

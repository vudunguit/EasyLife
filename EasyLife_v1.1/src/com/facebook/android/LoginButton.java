/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.android;

import org.json.JSONException;
import org.json.JSONStringer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

import covisoft.android.tab2_MyCoupon.Activity_UseCoupon;
import covisoft.android.tab2_MyCoupon.QRCodeActivity;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tab3_Home.Activity_QRCode;
import covisoft.android.tab3_Home.Activity_SocialNetwork;

public class LoginButton extends ImageButton {

    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    private Activity mActivity;
    private int mActivityCode;
    public static String fromAct = "";

    public LoginButton(Context context) {
        super(context);
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(final Activity activity, final int activityCode, final Facebook fb) {
        init(activity, activityCode, fb, new String[] {});
    }

    public void init(final Activity activity, final int activityCode, final Facebook fb, final String[] permissions) {
        mActivity = activity;
        mActivityCode = activityCode;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();

        drawableStateChanged();

        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        setOnClickListener(new ButtonOnClickListener());
    }

    private final class ButtonOnClickListener implements OnClickListener {
    	
        /*
         * Source Tag: login_tag
         */
        @Override
        public void onClick(View arg0) {
            if (mFb.isSessionValid()) {
            	
                if(fromAct.equals("Activity_Home_Item")) {
                	Bundle params = new Bundle();
                	params.putString("name", Activity_Home_Item.item.name);
                	params.putString("caption", Activity_Home_Item.item.addr);
                    params.putString("description", Activity_Home_Item.xml.s_info);
                    params.putString("picture", Activity_Home_Item.item.s_logo);
                    params.putString("link", "http://www.easylife.com.vn/easy4u/cua-hang/?no=" + Activity_Home_Item.item.no);
                    JSONStringer actions;
                    try {
                        actions = new JSONStringer().object()
                                    .key("name").value("EZlife")
                                    .key("link").value("http://www.easylife.com.vn/easy4u/").endObject();
                         params.putString("actions", actions.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    Utility.mFacebook.dialog(mActivity, "feed", params, new UpdateStatusListener());
                    String access_token = Utility.mFacebook.getAccessToken();
                    System.out.println(access_token);
                    
                } else if(fromAct.equals("Activity_Social_Network")) {
                	SessionEvents.onLogoutBegin();
                    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                    asyncRunner.logout(getContext(), new LogoutRequestListener());
                    if(Activity_SocialNetwork.cb_fb != null) {
                    	Activity_SocialNetwork.cb_fb.setSelected(false);
                    	Activity_SocialNetwork.cb_fb.setChecked(false);
                    }
                } else if(fromAct.equals("Activity_Recommend")) {
                	Bundle params = new Bundle();
                	params.putString("link", "http://easylife.com.vn/easy4u/");
                	params.putString("name", "EZlife");
                	params.putString("picture", "http://www.easylife.com.vn/Web_Mobile_New/admin/images/Logoez.png");
                    params.putString("caption", "Ăn uống, Giao hàng, Làm đẹp, Beer/Rượu, Chuỗi cửa hàng, Dịch vụ, Giải trí, Y tế, Khuyến mãi và các sự kiện khác.");
                    params.putString("description", "Ứng dụng tốt nhất cung cấp thông tin chi tiết của các cửa hàng xung quanh bạn.");
                    
    	  
                    Utility.mFacebook.dialog(mActivity, "feed", params, new UpdateStatusListener());
                    String access_token = Utility.mFacebook.getAccessToken();
                    System.out.println(access_token);
                    
                } else if(fromAct.equals("Activity_UseCoupon")) {
                	Bundle params = new Bundle();
                	
                	params.putString("name", Activity_UseCoupon.coupon.getCompanyName());
                	params.putString("caption", Activity_UseCoupon.coupon.getAddr());
                	params.putString("description", Activity_UseCoupon.coupon.getCont());
                	params.putString("picture", Activity_UseCoupon.coupon.getDownUpfile1());
                    params.putString("link", "http://www.easylife.com.vn/easy4u/cua-hang/?no=" + Activity_UseCoupon.coupon.getCompanyNo() + "&view=pr");
                    JSONStringer actions;
                    try {
                        actions = new JSONStringer().object()
                                    .key("name").value("EZlife")
                                    .key("link").value("http://www.easylife.com.vn/easy4u/").endObject();
                         params.putString("actions", actions.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utility.mFacebook.dialog(mActivity, "feed", params, new UpdateStatusListener());
                    String access_token = Utility.mFacebook.getAccessToken();
                    System.out.println(access_token);
                    
                } else if(fromAct.equals("Activity_QRCode")) {
                	Bundle params = new Bundle();
                	
                	params.putString("name", Activity_QRCode.coupon.getCompanyName());
                	params.putString("caption", Activity_QRCode.coupon.getAddress());
                	params.putString("description", Activity_QRCode.coupon.getCont());
                	params.putString("picture", Activity_QRCode.coupon.getLinkImage());
                    params.putString("link", "http://www.easylife.com.vn/easy4u/cua-hang/?no=" + Activity_QRCode.coupon.getCompanyNo() + "&view=pr");
                    JSONStringer actions;
                    try {
                        actions = new JSONStringer().object()
                                    .key("name").value("EZlife")
                                    .key("link").value("http://www.easylife.com.vn/easy4u/").endObject();
                         params.putString("actions", actions.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utility.mFacebook.dialog(mActivity, "feed", params, new UpdateStatusListener());
                    String access_token = Utility.mFacebook.getAccessToken();
                    System.out.println(access_token);
                    
                } else if(fromAct.equals("QRCodeActivity")) {
                	Bundle params = new Bundle();
                	
                	params.putString("name", QRCodeActivity.coupon.getCompanyName());
                	params.putString("caption", QRCodeActivity.coupon.getAddress());
                	params.putString("description", QRCodeActivity.coupon.getCont());
                	params.putString("picture", QRCodeActivity.coupon.getLinkImage());
                    params.putString("link", "http://www.easylife.com.vn/easy4u/cua-hang/?no=" + QRCodeActivity.coupon.getCompanyNo() + "&view=pr");
                    JSONStringer actions;
                    try {
                        actions = new JSONStringer().object()
                                    .key("name").value("EZlife")
                                    .key("link").value("http://www.easylife.com.vn/easy4u/").endObject();
                         params.putString("actions", actions.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utility.mFacebook.dialog(mActivity, "feed", params, new UpdateStatusListener());
                    String access_token = Utility.mFacebook.getAccessToken();
                    System.out.println(access_token);
                    
                }
                
            } else {
                mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());
            }
        }
    }

    private final class LoginDialogListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {
        	Toast toast = Toast.makeText(mActivity, "Login Successful", Toast.LENGTH_SHORT);
            toast.show();
            SessionEvents.onLoginSuccess();
            
            if(Activity_SocialNetwork.cb_fb != null) {
            	Activity_SocialNetwork.cb_fb.setSelected(true);
            	Activity_SocialNetwork.cb_fb.setChecked(true);
            }
            
            mActivity.setResult(Activity.RESULT_OK);
            
        }

        @Override
        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }

    private class LogoutRequestListener extends BaseRequestListener {
        @Override
        public void onComplete(String response, final Object state) {
            /*
             * callback should be run in the original thread, not the background
             * thread
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }

    private class SessionListener implements AuthListener, LogoutListener {

        @Override
        public void onAuthSucceed() {
//            setImageResource(R.drawable.logout_button);
            SessionStore.save(mFb, getContext());
        }

        @Override
        public void onAuthFail(String error) {
        }

        @Override
        public void onLogoutBegin() {
        }

        @Override
        public void onLogoutFinish() {
            SessionStore.clear(getContext());
//            setImageResource(R.drawable.login_button);
        }
    }
    
    /*
     * callback for the feed dialog which updates the profile status
     */
    public class UpdateStatusListener extends BaseDialogListener {
        @Override
        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                Toast toast = Toast.makeText(mActivity, "Đã Chia Sẻ Thành Công", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(mActivity, "Không Thể Chia Sẻ", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onFacebookError(FacebookError error) {
            Toast.makeText(mActivity, "Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast toast = Toast.makeText(mActivity, "Hủy Chia Sẻ", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

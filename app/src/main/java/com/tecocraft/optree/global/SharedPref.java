package com.tecocraft.optree.global;

/**
 * Shared Preference class which is used to store String , int , boolean data
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SharedPref {
	public static final String LOGIN ="isLogin";
	public static final String USER_ID ="useId";
	public static final String CODE_NAME ="codeName";
	public static final String BANNER ="banner";
	public static final String SPLASH ="splash";
	public static final String EMAIL ="email";
	public static final String PASSWORD ="password";

	Context mContext;

	SharedPreferences mSettingPrefs;
    
	SharedPreferences.Editor mSettingPrefEditor;

	public SharedPref(Context mContext) {
		this.mContext = mContext;

		// Get the xml/configuration_activity.xml preferences
		mSettingPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	/**
	 * Used to store String data
	 * 
	 * @param mKey
	 * @param mItem
	 */
	public void setDataInPref(String mKey, String mItem) {
		mSettingPrefEditor = mSettingPrefs.edit();
		mSettingPrefEditor.putString(mKey, mItem);
		mSettingPrefEditor.commit();
	}

	/**
	 * Used to get String data
	 * 
	 * @param mKey
	 * @return String
	 */
	public String getDataFromPref(String mKey) {
		String mSplashData = mSettingPrefs.getString(mKey, "");
		return mSplashData;
	}

	/**
	 * Used to store int data
	 * 
	 * @param mKey
	 * @param mItem
	 */
	public void setInt(String mKey, int mItem) {
		mSettingPrefEditor = mSettingPrefs.edit();
		mSettingPrefEditor.putInt(mKey, mItem);
		mSettingPrefEditor.commit();
	}

	/**
	 * Used to get int data stored in preference
	 * 
	 * @param mKey
	 * @return int
	 */
	public int getInt(String mKey) {
		int mPos = mSettingPrefs.getInt(mKey, 0);
		return mPos;
	}

	// /**
	// * Used to clear Shared Preference
	// */
	public void clearAllPref() {
		mSettingPrefEditor = mSettingPrefs.edit();
		mSettingPrefEditor.clear();
		mSettingPrefEditor.commit();
	}

	/**
	 * Used to store boolean data
	 * 
	 * @param mKey
	 * @param mItem
	 */
	public void setBoolean(String mKey, boolean mItem) {
		mSettingPrefEditor = mSettingPrefs.edit();
		mSettingPrefEditor.putBoolean(mKey, mItem);
		mSettingPrefEditor.commit();
	}

	/**
	 * Used to get boolean data stored ibn shared preferences
	 * 
	 * @param mKeys
	 * @return boolean
	 */
	public boolean getBoolean(String mKeys) {
		boolean mPos = mSettingPrefs.getBoolean(mKeys, false);
		return mPos;
	}

	public void setArrayPref(String key, ArrayList<String> values) {

		mSettingPrefEditor = mSettingPrefs.edit();
		JSONArray a = new JSONArray();
		for (int i = 0; i < values.size(); i++) {
			a.put(values.get(i));
		}
		if (!values.isEmpty()) {
			mSettingPrefEditor.putString(key, a.toString());
		} else {
			mSettingPrefEditor.putString(key, null);
		}
		mSettingPrefEditor.commit();
	}

	public ArrayList<String> getArrayPref(String key) {

		String json = mSettingPrefs.getString(key, null);
		ArrayList<String> urls = new ArrayList<String>();
		if (json != null) {
			try {
				JSONArray a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					String url = (a.optString(i));
					urls.add(url);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}
}

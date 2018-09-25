package bg.tu.thesis.medicalportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

	private static String PREFERENCES = "portal";
	
	public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0);
    }

	public static boolean contains(Context context, String value){
		//SharedPreferences settings = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		if (settings.contains(value)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void putInt(Context context, String key, int value){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void putFloat(Context context, String key, float value){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void putString(Context context, String key, String value){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putBoolean(Context context, String key, boolean value){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key,int defaultValue){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}
	
	public static float getFloat(Context context, String key,float defaultValue){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	public static String getString(Context context, String key,String defaultValue){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key,defaultValue);
	}

	public static boolean getBoolean(Context context, String key,boolean defaultValue){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static void deleteKey(Context context, String key){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}
}

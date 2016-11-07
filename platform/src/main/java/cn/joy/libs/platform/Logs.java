package cn.joy.libs.platform;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/25 0025.
 */

public class Logs {

	private static boolean DEBUG = true;

	public static void d(String tag, String str){
		if(DEBUG)
		Log.d(tag, str);
	}

	public static void e(String tag, String str){
		if(DEBUG)
			Log.e(tag, str);
	}

	public static void i(String tag, String str){
		if(DEBUG)
			Log.i(tag, str);
	}

	public static void v(String tag, String str){
		if(DEBUG)
			Log.v(tag, str);
	}
}

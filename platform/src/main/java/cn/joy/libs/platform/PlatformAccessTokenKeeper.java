package cn.joy.libs.platform;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * User: Joy
 * Date: 2016/11/3
 * Time: 12:01
 */

public abstract class PlatformAccessTokenKeeper<T> {

	private SharedPreferences sharedPreferences;

	public PlatformAccessTokenKeeper(Context context) {
		if (context == null)
			throw new NullPointerException();
		sharedPreferences = context.getSharedPreferences(getKeeperName(), Context.MODE_APPEND);
	}

	protected abstract String getKeeperName();

	public abstract void writeAccessToken(T t);

	public abstract T readAccessToken();

	/**
	 * 是否已经授权
	 * @return return
	 */
	public boolean isAuth() {
		return false;
	}

	public void clear() {
		getEditor().clear().apply();
		Logs.d(getClass().getCanonicalName(), "clear!!!!!!!!!!!!!!!!!!!!");
	}

	protected SharedPreferences.Editor getEditor() {
		return sharedPreferences.edit();
	}

	protected SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}
}

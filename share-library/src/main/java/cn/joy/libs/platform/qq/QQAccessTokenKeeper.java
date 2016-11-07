package cn.joy.libs.platform.qq;

import android.content.Context;
import android.text.TextUtils;

import cn.joy.libs.platform.PlatformAccessTokenKeeper;


/**
 * User: Joy
 * Date: 2016/11/3
 * Time: 11:47
 */

class QQAccessTokenKeeper extends PlatformAccessTokenKeeper<QQAuthInfo> {

	private static final String PREFERENCES_NAME = "_platform_info_qq";

	private static final String KEY_UID = "uid";
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_EXPRESS_IN = "express_in";

	public QQAccessTokenKeeper(Context context) {
		super(context);
	}

	@Override
	protected String getKeeperName() {
		return PREFERENCES_NAME;
	}

	@Override
	public void writeAccessToken(QQAuthInfo qqAuthInfo) {
		if (qqAuthInfo == null)
			return;
		getEditor().putString(KEY_UID, qqAuthInfo.getOpenId())
				.putString(KEY_ACCESS_TOKEN, qqAuthInfo.getAccessToken())
				.putLong(KEY_EXPRESS_IN, qqAuthInfo.getExpires())
				.apply();
	}

	@Override
	public QQAuthInfo readAccessToken() {
		QQAuthInfo authInfo = new QQAuthInfo();
		authInfo.setOpenId(getSharedPreferences().getString(KEY_UID, ""));
		authInfo.setAccessToken(getSharedPreferences().getString(KEY_ACCESS_TOKEN, ""));
		authInfo.setExpires(getSharedPreferences().getLong(KEY_EXPRESS_IN, 0));
		return authInfo;
	}

	@Override
	public boolean isAuth() {
		QQAuthInfo info = readAccessToken();
		return !TextUtils.isEmpty(info.getOpenId());
	}
}

/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.joy.libs.platform.sina;

import android.content.Context;
import android.text.TextUtils;

import cn.joy.libs.platform.PlatformAccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类定义了微博授权时所需要的参数。
 * @author SINA
 * @since 2013-10-07
 */
class SinaAccessTokenKeeper extends PlatformAccessTokenKeeper<Oauth2AccessToken> {
	private static final String PREFERENCES_NAME = "_platform_info_sina";

	private static final String KEY_UID = "uid";
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_EXPIRES_IN = "expires_in";
	private static final String KEY_REFRESH_TOKEN = "refresh_token";

	SinaAccessTokenKeeper(Context context) {
		super(context);
	}


	@Override
	protected String getKeeperName() {
		return PREFERENCES_NAME;
	}

	@Override
	public void writeAccessToken(Oauth2AccessToken token) {
		if (token == null)
			return;
		getEditor().putString(KEY_UID, token.getUid())
				.putString(KEY_ACCESS_TOKEN, token.getToken())
				.putString(KEY_REFRESH_TOKEN, token.getRefreshToken())
				.putLong(KEY_EXPIRES_IN, token.getExpiresTime())
				.apply();
	}

	@Override
	public Oauth2AccessToken readAccessToken() {
		Oauth2AccessToken token = new Oauth2AccessToken();
		token.setUid(getSharedPreferences().getString(KEY_UID, ""));
		token.setToken(getSharedPreferences().getString(KEY_ACCESS_TOKEN, ""));
		token.setRefreshToken(getSharedPreferences().getString(KEY_REFRESH_TOKEN, ""));
		token.setExpiresTime(getSharedPreferences().getLong(KEY_EXPIRES_IN, 0));
		return token;
	}

	@Override
	public boolean isAuth() {
		Oauth2AccessToken token = readAccessToken();
		return !TextUtils.isEmpty(token.getUid());
	}
}

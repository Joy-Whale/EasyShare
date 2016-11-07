package cn.joy.libs.platform.sina;

import android.content.Context;

import cn.joy.libs.platform.Platform;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 15-04
 */

public class Sina extends Platform {

	public static final String NAME = "Sina";
	private static final String REDIRECT_URL = "http://www.sina.com";
	private static final String SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

	private static final String SINA_PACKAGE = "com.sina.weibo";

	private String redirectUrl, scope;

	public Sina(Context context, String key, String secret, String redirectUrl) {
		this(context, key, secret, redirectUrl, SCOPE);
	}


	public Sina(Context context, String key, String secret, String redirectUrl, String scope) {
		super(context, key, secret);
		this.redirectUrl = redirectUrl;
		this.scope = scope;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean checkInstall() {
		return WeiboShareSDK.createWeiboAPI(getContext(), getKey()).isWeiboAppInstalled();
	}

	@Override
	public void register() {

	}

	@Override
	public Object getApi() {
		return null;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getScope() {
		return scope;
	}
}

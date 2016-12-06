package cn.joy.libs.platform;

import android.content.Context;

import cn.joy.libs.platform.wechat.WeChat;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-39
 */

public abstract class Platform<T> {

	private Context context;
	private String key, secret;

	public enum Target {
		QQ(cn.joy.libs.platform.qq.QQ.NAME),
		Wechat(WeChat.NAME),
		Sina(cn.joy.libs.platform.sina.Sina.NAME);

		private String name;

		Target(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum Operate {
		Login, Share
	}

	protected Platform(Context context, String key, String secret) {
		this.context = context;
		this.key = key;
		this.secret = secret;
	}

	//	protected Platform(String key, String secret, boolean shareByClient) {
	//		this.key = key;
	//		this.secret = secret;
	//		this.shareByClient = shareByClient;
	//	}

	public abstract String getName();

	public abstract boolean checkInstall();

	public abstract void register();

	public abstract T getApi();

	public String getKey() {
		return key;
	}

	public String getSecret() {
		return secret;
	}


	public Context getContext() {
		return context;
	}
	//
	//	public boolean isShareByClient() {
	//		return shareByClient;
	//	}
}

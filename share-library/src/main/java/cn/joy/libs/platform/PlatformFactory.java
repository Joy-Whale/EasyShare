package cn.joy.libs.platform;

import android.content.Context;

import cn.joy.libs.platform.qq.QQ;
import cn.joy.libs.platform.sina.Sina;
import cn.joy.libs.platform.wechat.Wechat;


/**
 * User: Administrator
 * Date: 2016/7/22 0022
 * Time: 12:08
 */

public class PlatformFactory {

	public static Wechat createWechat(String key, String secret) {
		return new Wechat(getContext(), key, secret);
	}

	public static QQ createQQ(String key, String secret) {
		return new QQ(getContext(), key, secret);
	}

	public static Sina createSina(String key, String secret, String redirectUrl) {
		return new Sina(getContext(), key, secret, redirectUrl);
	}

	public static Sina createSina(String key, String secret, String redirectUrl, String scope) {
		return new Sina(getContext(), key, secret, redirectUrl, scope);
	}

	public static Wechat getWechat() {
		return (Wechat) PlatformManager.getInstance().getPlatform(Wechat.NAME);
	}

	public static QQ getQQ() {
		return (QQ) PlatformManager.getInstance().getPlatform(QQ.NAME);
	}

	public static Sina getSina() {
		return (Sina) PlatformManager.getInstance().getPlatform(Sina.NAME);
	}

	private static Context getContext() {
		return PlatformManager.getInstance().getContext();
	}
}

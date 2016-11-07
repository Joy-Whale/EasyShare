package cn.joy.libs.platform.demo;

import android.app.Application;

import cn.joy.libs.platform.PlatformFactory;
import cn.joy.libs.platform.PlatformManager;

/**
 * User: Joy
 * Date: 2016/11/7
 * Time: 12:26
 */

public class SimpleApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化分享相关信息
		PlatformManager.init(this)
				.register(PlatformFactory.createWechat(BuildConfig.PLATFOMR_WECHAT_KEY, BuildConfig.PLATFOMR_WECHAT_SERCET_KEY))
				.register(PlatformFactory.createQQ(BuildConfig.PLATFOMR_QQ_KEY, BuildConfig.PLATFOMR_QQ_SERCET_KEY))
				.register(PlatformFactory.createSina(BuildConfig.PLATFOMR_SINA_KEY, BuildConfig.PLATFOMR_SINA_SERCET_KEY, BuildConfig.PLATFOMR_SINA_TARGET_URL));

	}
}

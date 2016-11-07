package cn.joy.libs.platform.wechat;

import android.content.Context;

import cn.joy.libs.platform.Platform;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 15-05
 */

public class Wechat extends Platform<IWXAPI> {

	public static final String NAME = "Wechat";

	private IWXAPI iwxapi;

	public Wechat(Context context, String key, String secret) {
		super(context, key, secret);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean checkInstall() {
		return iwxapi.isWXAppInstalled();
	}

	@Override
	public void register() {
		iwxapi = WXAPIFactory.createWXAPI(getContext(), getKey(), true);
		iwxapi.registerApp(getKey());
	}

	@Override
	public IWXAPI getApi() {
		return iwxapi;
	}
}

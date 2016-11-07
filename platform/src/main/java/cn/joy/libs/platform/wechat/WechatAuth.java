package cn.joy.libs.platform.wechat;

import cn.joy.libs.platform.AuthWithReceiver;
import cn.joy.libs.platform.ErrorCode;
import com.tencent.mm.sdk.modelmsg.SendAuth;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-00
 */

public class WechatAuth extends AuthWithReceiver<Wechat, WechatAuthInfo> {

	public WechatAuth(Wechat platform, boolean loginByClient) {
		super(platform, loginByClient);
	}

	@Override
	protected boolean checkInstall() {
		return getPlatform().checkInstall();
	}

	@Override
	public boolean auth(boolean requestUserInfo) {
		if (!super.auth(requestUserInfo))
			return false;
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		getPlatform().getApi().sendReq(req);
		return true;
	}

	/**
	 * 取消授权,目前没找到方法
	 */
	@Override
	public boolean deAuth() {
		if (!super.deAuth())
			return false;
		onError(ErrorCode.ERROR_AUTH);
		return true;
	}
}

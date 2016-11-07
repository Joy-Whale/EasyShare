package cn.joy.libs.platform.sina;

import cn.joy.libs.platform.AuthWithReceiver;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 17-27
 */

public class SinaAuth extends AuthWithReceiver<Sina, SinaAuthInfo> {

	public SinaAuth(Sina platform, boolean loginByClient) {
		super(platform, loginByClient);
	}

	@Override
	protected boolean checkInstall() {
		return getPlatform().checkInstall();
	}

	@Override
	public boolean auth(boolean requestUserInfo) {
		if (!super.auth(requestUserInfo)) {
			return false;
		}
		SinaWeiboEntryActivity.auth(getPlatform().getContext(), requestUserInfo);
		return true;
	}

	@Override
	public boolean deAuth() {
		if (!super.deAuth())
			return false;
		SinaWeiboEntryActivity.deAuth(getPlatform().getContext());
		return true;
	}

	@Override
	public boolean isAuth() {
		return new SinaAccessTokenKeeper(getPlatform().getContext()).isAuth();
	}
}

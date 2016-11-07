package cn.joy.libs.platform.qq;

import cn.joy.libs.platform.AuthWithReceiver;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 16-56
 */

public class QQAuth extends AuthWithReceiver<QQ, QQAuthInfo> {

	public QQAuth(QQ platform, boolean loginByClient) {
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
		QQEntryActivity.auth(getPlatform().getContext(), requestUserInfo);
		return true;
	}

	@Override
	public boolean deAuth() {
		if (!super.deAuth()) {
			return false;
		}
		QQEntryActivity.deAuth(getPlatform().getContext());
		return true;
	}

	@Override
	public boolean isAuth() {
		return getPlatform().getApi().isReady();
	}
}

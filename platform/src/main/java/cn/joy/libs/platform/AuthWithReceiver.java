package cn.joy.libs.platform;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 10-35
 */

public abstract class AuthWithReceiver<T extends Platform, Q extends PlatformAuthInfo> extends Auth<T, Q> {

	private AuthCallbackReceiver receiver = new AuthCallbackReceiver();

	public AuthWithReceiver(T platform, boolean loginByClient) {
		super(platform, loginByClient);
	}

	@Override
	public boolean auth(boolean requestUserInfo) {
		if (!super.auth(requestUserInfo))
			return false;
		receiver.register(getPlatform().getContext(), this);
		return true;
	}

	@Override
	protected void onFinish() {
		receiver.unregister();
	}
}

package cn.joy.libs.platform;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 16-49
 */

public abstract class Auth<T extends Platform, Q extends PlatformAuthInfo> implements PlatformActionListener<Q> {

	private T platform;
	private boolean loginByClient = true;
	private PlatformActionListener<Q> listener;

	public enum Target {
		QQ, Wechat, Sina
	}

	public Auth(T platform, boolean loginByClient) {
		this.platform = platform;
		this.loginByClient = loginByClient;
	}

	public Auth listener(PlatformActionListener<Q> listener) {
		this.listener = listener;
		return this;
	}

	protected abstract boolean checkInstall();


	/**
	 * 授权
	 */
	public boolean auth() {
		return auth(true);
	}

	/**
	 * 授权
	 * @param requestUserInfo 是否需要获取用户信息
	 */
	public boolean auth(boolean requestUserInfo) {
		if (getPlatform() == null) {
			onError(ErrorCode.ERROR_NOT_INIT);
		}
		if (loginByClient && !checkInstall()) {
			onError(ErrorCode.ERROR_NOT_INSTALL);
			return false;
		}
		return true;
	}

	public boolean deAuth() {
		if (getPlatform() == null) {
			onError(ErrorCode.ERROR_NOT_INIT);
		}
		if (loginByClient && !checkInstall()) {
			onError(ErrorCode.ERROR_NOT_INSTALL);
			return false;
		}
		return true;
	}

	/**
	 * 是否已经授权
	 */
	public boolean isAuth() {
		return false;
	}

	@Override
	public void onComplete(Q info) {
		if (listener != null) {
			listener.onComplete(info);
		}
		onFinish();
	}

	@Override
	public void onError(int code) {
		if (listener != null) {
			listener.onError(code);
		}
		onFinish();
	}

	@Override
	public void onCancel() {
		if (listener != null) {
			listener.onCancel();
		}
		onFinish();
	}

	protected void onFinish() {

	}

	public T getPlatform() {
		return platform;
	}

}

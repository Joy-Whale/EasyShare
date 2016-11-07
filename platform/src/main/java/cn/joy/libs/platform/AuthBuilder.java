package cn.joy.libs.platform;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-04
 */

public class AuthBuilder {

	private Auth.Target target;
	private boolean loginByClient = true;
	private PlatformActionListener listener;

	public AuthBuilder authTo(Auth.Target target) {
		this.target = target;
		return this;
	}

	public <T extends PlatformAuthInfo> AuthBuilder listener(PlatformActionListener<T> listener) {
		this.listener = listener;
		return this;
	}

	public AuthBuilder byClient(boolean byClient) {
		this.loginByClient = byClient;
		return this;
	}

	public void auth() {
		auth(true);
	}

	public void auth(boolean requestUserInfo) {
		Auth auth = createAuth();
		if (auth == null) {
			return;
		}
		auth.auth(requestUserInfo);
	}

	public void deAuth() {
		Auth auth = createAuth();
		if (auth == null) {
			return;
		}
		auth.deAuth();
	}

	public boolean checkAuth() {
		Auth auth = createAuth();
		if (auth == null) {
			return false;
		}
		return auth.isAuth();
	}

	private Auth createAuth() {
		if (target == null) {
			return null;
		}
		Auth auth = AuthFactory.create(target, loginByClient);
		auth.listener(listener);
		return auth;
	}
}

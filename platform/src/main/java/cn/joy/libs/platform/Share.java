package cn.joy.libs.platform;


/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 17-48
 *
 */

public abstract class Share<T extends Platform> {

	private static final String TAG = "Share";

	private T platform;
	private ShareParams params;

	private boolean shareByClient;

	public enum Target {
		QQ, QQZone, Wechat, WechatMoments, SinaWeibo
	}

	public Share(T platform, ShareParams params) {
		this(platform, params, true);
	}

	public Share(T platform, ShareParams params, boolean shareByClient) {
		this.platform = platform;
		this.params = params;
		this.shareByClient = shareByClient;
	}

	private PlatformActionListener listener;

	protected abstract boolean checkInstall();

	protected abstract boolean checkArgs();

	public Share listener(PlatformActionListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * 分享
	 */
	public boolean share() {
		if (platform == null) {
			onError(ErrorCode.ERROR_NOT_INIT);
			return false;
		}
		//  检查是否需要安装客户端以及客户端是否已经安装
		if (shareByClient && !checkInstall()) {
			onError(ErrorCode.ERROR_NOT_INSTALL);
			return false;
		}
		//  检查参数是否正确
		if (!checkArgs()) {
			onError(ErrorCode.ERROR_ARGS);
			return false;
		}
		return true;
	}

	public ShareParams getShareParams() {
		return params;
	}

	public T getPlatform() {
		return platform;
	}

	public boolean isShareByClient() {
		return shareByClient;
	}

	protected void onComplete() {
		if (listener != null) {
			listener.onComplete(null);
		}
		onFinish();
	}

	protected void onError(int code) {
		Logs.d(TAG, code + "");
		if (listener != null) {
			listener.onError(code);
		}
		onFinish();
	}

	protected void onCancel() {
		if (listener != null) {
			listener.onCancel();
		}
		onFinish();
	}

	protected void onFinish() {
		getShareParams().destroy();
	}
}

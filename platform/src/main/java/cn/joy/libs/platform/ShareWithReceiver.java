package cn.joy.libs.platform;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-13
 *
 */

public abstract class ShareWithReceiver<T extends Platform> extends Share<T> {

	private ShareCallbackReceiver receiver = new ShareCallbackReceiver();

	public ShareWithReceiver(T platform, ShareParams params) {
		super(platform, params);
	}

	public ShareWithReceiver(T platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	public boolean share() {
		if (!super.share())
			return false;
		receiver.register(getPlatform().getContext(), this);
		return true;
	}

	@Override
	protected void onFinish() {
		super.onFinish();
		receiver.unregister();
	}
}

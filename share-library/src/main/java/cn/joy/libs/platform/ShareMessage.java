package cn.joy.libs.platform;

/**
 * Created by Administrator on 2016/7/25 0025.
 */

public abstract class ShareMessage<T>{

	private ShareParams params;

	protected ShareMessage(ShareParams params) {
		this.params = params;
	}

	protected abstract T createMessage();

	public ShareParams getShareParams() {
		return params;
	}
}

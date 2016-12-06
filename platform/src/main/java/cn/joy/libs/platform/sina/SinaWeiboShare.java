package cn.joy.libs.platform.sina;

import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.ShareWithReceiver;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;


public class SinaWeiboShare extends ShareWithReceiver<Sina> {

	private IWeiboShareAPI sdk;

	public SinaWeiboShare(Sina platform, ShareParams params) {
		super(platform, params);
	}

	public SinaWeiboShare(Sina platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	protected boolean checkInstall() {
		return WeiboShareSDK.createWeiboAPI(getPlatform().getContext(), getPlatform().getKey()).isWeiboAppInstalled();
	}

	@Override
	protected boolean checkArgs() {
		return true;
	}

	@Override
	public boolean share() {
		if (!super.share())
			return false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				WeiboMultiMessage message = null;
				switch (getShareParams().getShareType()) {
					case Text:
						message = new SinaWeiboShareTextMessage(getShareParams()).createMessage();
						break;
					case TextAndImage:
						message = new SinaWeiboShareImageAndTextMessage(getShareParams()).createMessage();
						break;
					case WebPage:
						message = new SinaWeiboShareWebPageMessage(getShareParams()).createMessage();
						break;
				}
				SinaWeiboEntryActivity.share(getShareParams().getShareActivity(), message);
			}
		}).start();
		return true;
	}
}

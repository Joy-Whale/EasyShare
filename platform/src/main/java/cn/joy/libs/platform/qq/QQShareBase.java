package cn.joy.libs.platform.qq;

import android.os.Bundle;
import android.text.TextUtils;

import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.ShareWithReceiver;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-09
 */

abstract class QQShareBase extends ShareWithReceiver<QQ> {

	static final int SHARE_TARGET_QQ = 1;
	static final int SHARE_TARGET_QZONE = 2;

	public QQShareBase(QQ platform, ShareParams params) {
		super(platform, params);
	}

	public QQShareBase(QQ platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	protected boolean checkInstall() {
		return true;
	}

	@Override
	protected boolean checkArgs() {
		if (getShareParams() == null)
			return false;
		switch (getShareParams().getShareType()) {
			case Image:
				if (getShareParams().getImage() == null)
					return false;
				break;
			case TextAndImage:
				if (TextUtils.isEmpty(getShareParams().getTargetUrl()) || TextUtils.isEmpty(getShareParams().getTitle()))
					return false;
				break;
			case WebPage:
				break;
		}
		return true;
	}

	@Override
	public boolean share() {
		if (!super.share())
			return false;
		Bundle bundle = null;
		switch (getShareParams().getShareType()) {
			case Image:
				bundle = new QQShareImageMessage(getShareParams()).createMessage();
				break;
			case TextAndImage:
			case WebPage:
				bundle = new QQShareTextAndImageMessage(getShareParams()).createMessage();
				break;
		}
		if (bundle != null) {
			QQEntryActivity.share(getShareParams().getShareActivity(), bundle, getShareTarget());
			//			switch (getShareTarget()) {
			//				case SHARE_TARGET_QQ:
			//					getPlatform().getApi().shareToQQ(getShareParams().getShareActivity(), bundle, listener);
			//					break;
			//				case SHARE_TARGET_QZONE:
			//					getPlatform().getApi().shareToQzone(getShareParams().getShareActivity(), bundle, listener);
			//					break;
			//			}
		}
		return true;
		//		getPlatform().getApi().shareToQQ(activity, );
	}

	abstract int getShareTarget();
}

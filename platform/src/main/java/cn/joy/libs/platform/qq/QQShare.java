package cn.joy.libs.platform.qq;


import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.Utils;

public class QQShare extends QQShareBase {

	public QQShare(QQ platform, ShareParams params) {
		super(platform, params);
	}

	public QQShare(QQ platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	protected boolean checkInstall() {
		return Utils.isQQInstall();
	}

	@Override
	int getShareTarget() {
		return SHARE_TARGET_QQ;
	}
}

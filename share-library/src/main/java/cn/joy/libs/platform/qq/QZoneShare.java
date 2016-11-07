package cn.joy.libs.platform.qq;

import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.Utils;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-17
 *
 */

public class QZoneShare extends QQShareBase {

	public QZoneShare(QQ platform, ShareParams params) {
		super(platform, params);
	}

	public QZoneShare(QQ platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	int getShareTarget() {
		return SHARE_TARGET_QZONE;
	}

	@Override
	protected boolean checkInstall() {
		return Utils.isQZoneInstall();
	}
}

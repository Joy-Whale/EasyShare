package cn.joy.libs.platform.qq;

import android.os.Bundle;

import cn.joy.libs.platform.ShareParams;
import com.tencent.connect.share.QQShare;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-11
 */

class QQShareImage extends QQShareMessage {

	QQShareImage(ShareParams params) {
		super(params);
	}


	@Override
	protected Bundle createMessage() {
		Bundle bundle = new Bundle();
		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getShareParams().getTargetUrl());
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, getShareParams().getTitle());
		switch (getShareParams().getImage().getSource()) {
			case File:
				bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, getShareParams().getImage().getImageUrl());
				break;
			case Http:
				bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, getShareParams().getImage().getImageUrl());
				break;
		}
		//		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName.getText().toString());
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		return bundle;
	}
}

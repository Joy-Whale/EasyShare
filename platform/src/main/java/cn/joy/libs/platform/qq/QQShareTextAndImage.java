package cn.joy.libs.platform.qq;

import android.os.Bundle;

import cn.joy.libs.platform.ShareParams;
import com.tencent.connect.share.QQShare;


class QQShareTextAndImage extends QQShareMessage {

	QQShareTextAndImage(ShareParams params) {
		super(params);
	}

	@Override
	protected Bundle createMessage() {
		Bundle bundle = new Bundle();
		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, getShareParams().getTitle());
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getShareParams().getTargetUrl());
		switch (getShareParams().getImage().getSource()) {
			case File:
				bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, getShareParams().getImage().getImageUrl());
				break;
			case Http:
				bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, getShareParams().getImage().getImageUrl());
				break;
		}
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, getShareParams().getContent());
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		//		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
		//		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
		return bundle;
	}
}

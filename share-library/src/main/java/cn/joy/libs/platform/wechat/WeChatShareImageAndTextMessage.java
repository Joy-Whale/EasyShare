package cn.joy.libs.platform.wechat;

import android.support.annotation.WorkerThread;

import cn.joy.libs.platform.ShareImageUtils;
import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;


/**
 * Created by Administrator on 2016/7/21 0021.
 */

class WeChatShareImageAndTextMessage extends WeChatShareMessage {

	WeChatShareImageAndTextMessage(ShareParams params, int scene) {
		super(params, scene);
	}

	@Override
	protected String buildTransaction() {
		return "img";
	}

	@Override
	@WorkerThread
	protected WXMediaMessage buildMediaMessage() {
		WXImageObject object = new WXImageObject();
		byte [] thumbByte = ShareImageUtils.getThumbData(getShareParams().getImage());
		switch (getShareParams().getImage().getSource()) {
			case File:
				object.imagePath = getShareParams().getImage().getImageUrl();
				break;
			case Http:
				object.imageData = thumbByte;
				break;
		}
		WXMediaMessage message = new WXMediaMessage();
		message.mediaObject = object;
		message.thumbData = thumbByte;
		message.description = getShareParams().getContent();
		return message;
	}
}

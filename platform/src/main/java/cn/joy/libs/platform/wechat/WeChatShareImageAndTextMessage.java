package cn.joy.libs.platform.wechat;


import cn.joy.libs.platform.ShareImageUtils;
import cn.joy.libs.platform.ShareParams;

import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;


/**
 * User: Administrator
 * Date: 2016/7/21 0021
 * Time: 9:47
 */

class WeChatShareImageAndTextMessage extends WeChatShareMessage {

	private static final int MAX_IMAGE_SIZE = 512 * 1024;
	private static final int MAX_THUMB_SIZE = 32 * 1024;

	WeChatShareImageAndTextMessage(ShareParams params, int scene) {
		super(params, scene);
	}

	@Override
	protected String buildTransaction() {
		return "img";
	}

	/**
	 * 工作线程
	 */
	@Override
	protected WXMediaMessage buildMediaMessage() {
		WXImageObject object = new WXImageObject();
		switch (getShareParams().getImage().getSource()) {
			case File:
				object.imagePath = getShareParams().getImage().getImageFile().getPath();
				break;
			case Http:
				object.imageData = ShareImageUtils.getCompressData(getShareParams().getImage(), MAX_IMAGE_SIZE);
				break;
		}
		WXMediaMessage message = new WXMediaMessage();
		message.mediaObject = object;
		message.thumbData = ShareImageUtils.getThumbCompressData(object.imageData);
		message.description = getShareParams().getContent();
		return message;
	}
}

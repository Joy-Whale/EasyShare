package cn.joy.libs.platform.sina;

import cn.joy.libs.platform.ShareImageUtils;
import cn.joy.libs.platform.ShareParams;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-07
 */

class SinaWeiboShareImageAndTextMessage extends SinaWeiboShareTextMessage {

	private static final int MAX_IMAGE_SIZE = 32 * 1024;

	SinaWeiboShareImageAndTextMessage(ShareParams params) {
		super(params);
	}

	@Override
	protected WeiboMultiMessage createMessage() {
		WeiboMultiMessage message = super.createMessage();
		final ImageObject object = new ImageObject();
		final ShareParams.ShareImage image = getShareParams().getImage();
		switch (image.getSource()) {
			case Bitmap:
				object.setImageObject(image.getImageBitmap());
				break;
			case File:
				object.imagePath = image.getImageFile().getAbsolutePath();
			case Http:
				object.imageData = ShareImageUtils.getCompressData(image);
				break;
		}
		message.imageObject = object;
		return message;
	}
}

package cn.joy.libs.platform.sina;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

import cn.joy.libs.platform.ShareParams;

/**
 * User: Joy
 * Date: 2016/12/6
 * Time: 11:52
 */

public class SinaWeiboShareWebPageMessage extends SinaWeiboShareImageAndTextMessage{

	SinaWeiboShareWebPageMessage(ShareParams params) {
		super(params);
	}

	@Override
	protected WeiboMultiMessage createMessage() {
		WeiboMultiMessage message = super.createMessage();
		WebpageObject object = new WebpageObject();
		object.actionUrl = getShareParams().getTargetUrl();
		object.defaultText = getShareParams().getContent();
		message.mediaObject = object;
		return message;
	}
}

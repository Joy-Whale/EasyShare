package cn.joy.libs.platform.sina;

import cn.joy.libs.platform.ShareParams;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-07
 *
 */

class SinaWeiboShareTextMessage extends SinaWeiboShareMessage {

	SinaWeiboShareTextMessage(ShareParams params) {
		super(params);
	}

	@Override
	protected WeiboMultiMessage createMessage() {
		WeiboMultiMessage message = new WeiboMultiMessage();
		TextObject object = new TextObject();
		object.text = getShareParams().getContent();
		message.textObject = object;
		return message;
	}
}

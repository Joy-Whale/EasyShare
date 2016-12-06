package cn.joy.libs.platform.wechat;

import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import cn.joy.libs.platform.ShareParams;

/**
 * User: Joy
 * Date: 2016/12/6
 * Time: 11:46
 */

public class WeChatShareWebPageMessage extends WeChatShareMessage {

	WeChatShareWebPageMessage(ShareParams params, int scene) {
		super(params, scene);
	}

	@Override
	protected String buildTransaction() {
		return "webpage";
	}

	@Override
	protected WXMediaMessage buildMediaMessage() {
		return null;
	}

	@Override
	protected WXMediaMessage.IMediaObject createMediaObject() {
		WXWebpageObject object = new WXWebpageObject();
		object.webpageUrl = getShareParams().getTargetUrl();
		return object;
	}
}

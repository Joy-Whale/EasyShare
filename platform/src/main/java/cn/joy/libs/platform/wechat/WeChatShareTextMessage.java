package cn.joy.libs.platform.wechat;

import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;


/**
 * Created by Administrator on 2016/7/21 0021.
 */

 class WeChatShareTextMessage extends WeChatShareMessage {

	public WeChatShareTextMessage(ShareParams params, int scene) {
		super(params, scene);
	}

	@Override
	protected String buildTransaction() {
		return "text";
	}

	@Override
	protected WXMediaMessage.IMediaObject createMediaObject() {
		WXTextObject object = new WXTextObject();
		object.text = getShareParams().getContent();
		return object;
	}
}

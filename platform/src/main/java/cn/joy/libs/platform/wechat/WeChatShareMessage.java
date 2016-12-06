package cn.joy.libs.platform.wechat;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;

import cn.joy.libs.platform.ShareImageUtils;
import cn.joy.libs.platform.ShareMessage;
import cn.joy.libs.platform.ShareParams;


/**
 * User: Administrator
 * Date: 2016/7/21 0021
 * Time: 9:48
 */

abstract class WeChatShareMessage extends ShareMessage<SendMessageToWX.Req> {

	private int scene;

	WeChatShareMessage(ShareParams params, int scene) {
		super(params);
		this.scene = scene;
	}

	protected abstract String buildTransaction();

	WXMediaMessage buildMediaMessage() {
		WXMediaMessage message = new WXMediaMessage();
		message.title = getShareParams().getTitle();
		message.description = getShareParams().getContent();
		message.mediaObject = createMediaObject();
		message.thumbData = createThumbData();
		return message;
	}

	protected abstract WXMediaMessage.IMediaObject createMediaObject();

	protected byte[] createThumbData() {
		return ShareImageUtils.getThumbCompressData(getShareParams().getImage());
	}

	@Override
	protected SendMessageToWX.Req createMessage() {
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction();
		req.message = buildMediaMessage();
		req.scene = scene;
		return req;
	}
}

package cn.joy.libs.platform.wechat;

import android.support.annotation.WorkerThread;

import cn.joy.libs.platform.ShareMessage;
import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;


/**
 * Created by Administrator on 2016/7/21 0021.
 */

abstract class WeChatShareMessage extends ShareMessage<SendMessageToWX.Req> {

	private int scene;

	WeChatShareMessage(ShareParams params, int scene) {
		super(params);
		this.scene = scene;
	}

	protected abstract String buildTransaction();

	protected abstract WXMediaMessage buildMediaMessage();

	@Override
	@WorkerThread
	protected SendMessageToWX.Req createMessage() {
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction();
		req.message = buildMediaMessage();
		req.scene = scene;
		return req;
	}
}

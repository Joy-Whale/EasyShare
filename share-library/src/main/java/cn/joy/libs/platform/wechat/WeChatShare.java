package cn.joy.libs.platform.wechat;

import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-04
 */

public class WeChatShare extends WechatShareBase {
	public WeChatShare(Wechat platform, ShareParams params) {
		super(platform, params);
	}

	public WeChatShare(Wechat platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	public int getScene() {
		return SendMessageToWX.Req.WXSceneSession;
	}
}

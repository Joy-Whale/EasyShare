package cn.joy.libs.platform.wechat;

import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 10-01
 *
 */

public class WeChatMomentsShare extends WechatShareBase {

	public WeChatMomentsShare(Wechat platform, ShareParams params) {
		super(platform, params);
	}

	public WeChatMomentsShare(Wechat platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	public int getScene() {
		return SendMessageToWX.Req.WXSceneTimeline;
	}
}

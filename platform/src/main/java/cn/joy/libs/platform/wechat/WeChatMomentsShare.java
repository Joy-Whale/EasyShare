package cn.joy.libs.platform.wechat;

import cn.joy.libs.platform.ShareParams;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 10-01
 *
 */

public class WeChatMomentsShare extends WeChatShareBase {

	public WeChatMomentsShare(WeChat platform, ShareParams params) {
		super(platform, params);
	}

	public WeChatMomentsShare(WeChat platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	public int getScene() {
		return SendMessageToWX.Req.WXSceneTimeline;
	}
}

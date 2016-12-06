package cn.joy.libs.platform.sina;

import cn.joy.libs.platform.ShareMessage;
import cn.joy.libs.platform.ShareParams;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-06
 *
 */

abstract class SinaWeiboShareMessage extends ShareMessage<WeiboMultiMessage> {


	SinaWeiboShareMessage(ShareParams params) {
		super(params);
	}
}

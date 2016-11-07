package cn.joy.libs.platform;


import cn.joy.libs.platform.qq.QQShare;
import cn.joy.libs.platform.qq.QZoneShare;
import cn.joy.libs.platform.sina.SinaWeiboShare;
import cn.joy.libs.platform.wechat.WeChatMomentsShare;
import cn.joy.libs.platform.wechat.WeChatShare;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 10-02
 */

class ShareFactory {

	static Share create(Share.Target item, ShareParams params, boolean shareByClient) {
		Share share = null;
		switch (item) {
			case Wechat:
				share = new WeChatShare(PlatformFactory.getWechat(), params, shareByClient);
				break;
			case WechatMoments:
				share = new WeChatMomentsShare(PlatformFactory.getWechat(), params, shareByClient);
				break;
			case QQ:
				share = new QQShare(PlatformFactory.getQQ(), params, shareByClient);
				break;
			case QQZone:
				share = new QZoneShare(PlatformFactory.getQQ(), params, shareByClient);
				break;
			case SinaWeibo:
				share = new SinaWeiboShare(PlatformFactory.getSina(), params, shareByClient);
				break;
		}
		return share;
	}
}

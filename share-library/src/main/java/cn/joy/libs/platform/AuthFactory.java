package cn.joy.libs.platform;


import cn.joy.libs.platform.qq.QQAuth;
import cn.joy.libs.platform.sina.SinaAuth;
import cn.joy.libs.platform.wechat.WechatAuth;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-05
 */

public class AuthFactory {
	
	static Auth create(Auth.Target target, boolean loginByClient) {
		Auth login = null;
		switch (target) {
			case QQ:
				login = new QQAuth(PlatformFactory.getQQ(), loginByClient);
				break;
			case Sina:
				login = new SinaAuth(PlatformFactory.getSina(), loginByClient);
				break;
			case Wechat:
				login = new WechatAuth(PlatformFactory.getWechat(), loginByClient);
				break;
		}
		return login;
	}
}

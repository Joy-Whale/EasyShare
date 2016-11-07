package cn.joy.libs.platform.qq;

import android.os.Bundle;

import cn.joy.libs.platform.ShareMessage;
import cn.joy.libs.platform.ShareParams;

/**
 * Created by Administrator on 2016/7/25 0025.
 */

 abstract class QQShareMessage extends ShareMessage<Bundle> {


	QQShareMessage(ShareParams params) {
		super(params);
	}

}

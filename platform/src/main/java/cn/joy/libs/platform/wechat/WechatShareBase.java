package cn.joy.libs.platform.wechat;

import android.os.AsyncTask;

import cn.joy.libs.platform.ErrorCode;
import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.ShareWithReceiver;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-22
 */

abstract class WeChatShareBase extends ShareWithReceiver<WeChat> {

	WeChatShareBase(WeChat platform, ShareParams params) {
		super(platform, params);
	}

	WeChatShareBase(WeChat platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	protected boolean checkInstall() {
		return getPlatform().checkInstall();
	}

	@Override
	protected boolean checkArgs() {
		return true;
	}

	@Override
	public boolean share() {
		if (!super.share())
			return false;
		new AsyncTask<Void, Void, SendMessageToWX.Req>() {
			@Override
			protected SendMessageToWX.Req doInBackground(Void... voids) {
				SendMessageToWX.Req req = null;
				switch (getShareParams().getShareType()) {
					case Text:
						req = new WeChatShareTextMessage(getShareParams(), getScene()).createMessage();
						break;
					case TextAndImage:
						req = new WeChatShareImageAndTextMessage(getShareParams(), getScene()).createMessage();
						break;
					case WebPage:
						req = new WeChatShareWebPageMessage(getShareParams(), getScene()).createMessage();
						break;
				}
				return req;
			}

			@Override
			protected void onPostExecute(SendMessageToWX.Req req) {
				super.onPostExecute(req);
				if (req == null) {
					onError(ErrorCode.ERROR_SHARE);
					return;
				}
				if (!getPlatform().getApi().sendReq(req)) {
					onError(ErrorCode.ERROR_SHARE);
				}

			}
		}.execute();
		return true;
	}

	public abstract int getScene();
}

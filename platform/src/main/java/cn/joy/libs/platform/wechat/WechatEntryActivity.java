package cn.joy.libs.platform.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import cn.joy.libs.platform.AuthCallbackReceiver;
import cn.joy.libs.platform.ErrorCode;
import cn.joy.libs.platform.Logs;
import cn.joy.libs.platform.Operate;
import cn.joy.libs.platform.PlatformAuthInfo;
import cn.joy.libs.platform.PlatformFactory;
import cn.joy.libs.platform.ShareCallbackReceiver;
import cn.joy.libs.platform.Utils;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 18-05
 */

public class WeChatEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WeChatEntryActivity";

	private static final String SCOPE_BASE = "snsapi_base";
	private static final String SCOPE_USER = "snsapi_userinfo";
	private static final String EXTRA_OPERATE = "EXTRA_OPERATE";
	//
	//	static void auth(Context context, boolean requestUserInfo) {
	//		context.startActivity(new Intent(context, WeChatEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	//				.putExtra(EXTRA_OPERATE, requestUserInfo ? Operate.AuthAndInfo : Operate.Auth));
	//	}

	private Operate operate = Operate.Share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() != null) {
			PlatformFactory.getWechat().getApi().handleIntent(getIntent(), this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent() != null) {
			PlatformFactory.getWechat().getApi().handleIntent(intent, this);
		}
	}

	@Override
	public void onReq(BaseReq baseReq) {
		Logs.d(TAG, baseReq.toString());
		finish();
	}

	@Override
	public void onResp(BaseResp baseResp) {
		// 请求auth点击返回键时返回的baseResp类型居然是SendMessageToWX.Resp而不是SendAuth.Resp???只能同时调用俩广播了
		if (baseResp instanceof SendMessageToWX.Resp) {
			SendMessageToWX.Resp resp = (SendMessageToWX.Resp) baseResp;
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					ShareCallbackReceiver.sendBroadcastComplete(this);
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					ShareCallbackReceiver.sendBroadcastCancel(this);
					AuthCallbackReceiver.sendBroadcastCancel(this);
					break;
				case BaseResp.ErrCode.ERR_SENT_FAILED:
				case BaseResp.ErrCode.ERR_UNSUPPORT:
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
				case BaseResp.ErrCode.ERR_BAN:
					ShareCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
					AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_AUTH);
					break;
			}
		} else if (baseResp instanceof SendAuth.Resp) {
			final SendAuth.Resp resp = (SendAuth.Resp) baseResp;
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					new Thread() {
						@Override
						public void run() {
							getToken(resp.code);
						}
					}.start();
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					AuthCallbackReceiver.sendBroadcastCancel(this);
					break;
				case BaseResp.ErrCode.ERR_SENT_FAILED:
				case BaseResp.ErrCode.ERR_UNSUPPORT:
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
				case BaseResp.ErrCode.ERR_BAN:
					AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
					break;
			}
		} else {
			finish();
		}
	}

	/**
	 * 获取用户token
	 * @param code code
	 */
	private void getToken(String code) {
		JSONObject object = Utils.requestGetJson(String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", PlatformFactory
				.getWechat()
				.getKey(), PlatformFactory.getWechat().getSecret(), code));
		if (object == null || object.has("errcode")) {
			AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
			return;
		}
		try {
			WeChatAuthInfo info = new WeChatAuthInfo();
			info.setAccessToken(object.getString("access_token"));
			info.setExpires(object.getLong("expires_in"));
			info.setRefreshToken(object.getString("refresh_token"));
			info.setOpenId(object.getString("openid"));
			info.setScope(object.getString("scope"));
			if (info.getScope().contains(SCOPE_USER) && !TextUtils.isEmpty(info.getAccessToken()) && !TextUtils.isEmpty(info.getOpenId())) {
				getUserInfo(info);
			} else {
				AuthCallbackReceiver.sendBroadcastComplete(this, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
		}
	}

	/**
	 * 获取用户详细资料
	 * @param info 用户info
	 */
	private void getUserInfo(WeChatAuthInfo info) {
		JSONObject object = Utils.requestGetJson(String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", info.getAccessToken(), info.getOpenId()));
		if (object == null || object.has("errcode")) {
			AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
			return;
		}
		try {
			info.setAvatar(object.getString("headimgurl"));
			info.setCity(object.getString("city"));
			info.setCountry(object.getString("country"));
			info.setNickname(object.getString("nickname"));
			info.setSex(object.getInt("sex") == 1 ? PlatformAuthInfo.Sex.Male : PlatformAuthInfo.Sex.Female);
			info.setUnionId(object.getString("unionid"));
			AuthCallbackReceiver.sendBroadcastComplete(this, info);
		} catch (JSONException e) {
			e.printStackTrace();
			AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
		}
	}
}

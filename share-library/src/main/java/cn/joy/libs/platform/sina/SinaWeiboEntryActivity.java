package cn.joy.libs.platform.sina;

import android.app.Activity;
import android.content.Context;
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
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

import org.json.JSONException;
import org.json.JSONObject;


public class SinaWeiboEntryActivity extends Activity implements IWeiboHandler.Response {

	private static final String TAG = "SinaWeiboEntryActivity";
	private static final String EXTRA_PARAMS = "EXTRA_PARAMS";
	private static final String EXTRA_OPERATE = "EXTRA_OPERATE";

	/**
	 * 分享
	 * @param context context
	 * @param args    参数
	 */
	static void share(Context context, WeiboMultiMessage args) {
		context.startActivity(new Intent(context, SinaWeiboEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_PARAMS, args.toBundle(new Bundle()))
				.putExtra(EXTRA_OPERATE, Operate.Share));
	}

	/**
	 * 鉴权
	 * @param context context
	 */
	static void auth(Context context, boolean requestUserInfo) {
		context.startActivity(new Intent(context, SinaWeiboEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_OPERATE, requestUserInfo ? Operate.AuthAndInfo : Operate.Auth));
	}

	static void deAuth(Context context) {
		context.startActivity(new Intent(context, SinaWeiboEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_OPERATE, Operate.DeAuth));
	}

	private IWeiboShareAPI mShareApi;
	private SsoHandler mSSOHandler;
	private Sina sina = PlatformFactory.getSina();
	private WeiboMultiMessage mWeiboMessage;
	private AuthInfo mAuthInfo;
	private Operate operate;
	private SinaAccessTokenKeeper mKeeper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!getIntent().hasExtra(EXTRA_OPERATE)) {
			finish();
			return;
		}
		mKeeper = new SinaAccessTokenKeeper(this);
		operate = (Operate) getIntent().getSerializableExtra(EXTRA_OPERATE);
		mAuthInfo = createAuthInfo();
		switch (operate) {
			case Auth:
			case AuthAndInfo:
				login();
				break;
			case DeAuth:
				logout();
				break;
			case Share:
				mWeiboMessage = new WeiboMultiMessage().toObject(getIntent().getBundleExtra(EXTRA_PARAMS));
				mShareApi = WeiboShareSDK.createWeiboAPI(this, sina.getKey());
				mShareApi.registerApp();
				if (savedInstanceState != null) {
					mShareApi.handleWeiboResponse(getIntent(), this);
				}
				share();
				break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//		if(intent.hasExtra(EXTRA_OPERATE)){
		//			operate = (Operate) intent.getSerializableExtra(EXTRA_OPERATE);
		//		}
		mShareApi.handleWeiboResponse(intent, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSSOHandler != null) {
			mSSOHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 取消授权
	 */
	private void logout() {
		Logs.d(TAG, "logout!!!");
		final String token = getToken();
		if (TextUtils.isEmpty(token)) {
			handlerSSO();
		} else {
			new Thread() {
				@Override
				public void run() {
					Oauth2AccessToken oauth2AccessToken = getLocalAccessToken();
					JSONObject object = Utils.requestGetJson(String.format("https://api.weibo.com/oauth2/revokeoauth2?access_token=%s", oauth2AccessToken.getToken()));
					if (object == null) {
						AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
						return;
					}
					try {
						if (object.getBoolean("result")) {
							AuthCallbackReceiver.sendBroadcastComplete(SinaWeiboEntryActivity.this, null);
							mKeeper.clear();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
					}
				}
			}.start();
		}
	}

	/**
	 * 授权登录
	 */
	private void login() {
		final String token = getToken();
		if (TextUtils.isEmpty(token)) {
			handlerSSO();
		} else {
			final Oauth2AccessToken oauth2AccessToken = getLocalAccessToken();
			final SinaAuthInfo info = new SinaAuthInfo();
			info.setAccessToken(oauth2AccessToken.getToken());
			info.setOpenId(oauth2AccessToken.getUid());
			info.setRefreshToken(oauth2AccessToken.getRefreshToken());
			info.setExpires(oauth2AccessToken.getExpiresTime());
			info.setPhoneNumber(oauth2AccessToken.getPhoneNum());
			if (operate.equals(Operate.Auth)) {
				AuthCallbackReceiver.sendBroadcastComplete(this, info);
				return;
			}
			new Thread() {
				@Override
				public void run() {
					JSONObject object = Utils.requestGetJson(String.format("https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s", oauth2AccessToken.getToken(), oauth2AccessToken
							.getUid()));
					if (object == null) {
						Logs.d(TAG, " 请求新浪微博用户信息失败！！！");
						AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
					} else {
						Logs.d(TAG, object.toString());
						try {
							info.setNickname(object.getString("screen_name"));
							info.setAvatar(object.getString("profile_image_url"));
							info.setSex(object.getString("gender").equals("m") ? PlatformAuthInfo.Sex.Male : PlatformAuthInfo.Sex.Female);
							AuthCallbackReceiver.sendBroadcastComplete(SinaWeiboEntryActivity.this, info);
						} catch (Exception e) {
							e.printStackTrace();
							AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
							Logs.d(TAG, " 请求新浪微博用户信息失败！！！");
						}
					}
				}
			}.start();
		}
	}

	private void share() {
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = mWeiboMessage;

		final String token = getToken();
		if (TextUtils.isEmpty(token)) {
			handlerSSO();
		} else {
			// 添加个空的WeiboAuthListener，若不添加，则在分享成功后，倒计时3s后微博分享页面不能正常关闭
			if (!mShareApi.sendRequest(this, request, mAuthInfo, token, new WeiboAuthListener() {
				@Override
				public void onComplete(Bundle bundle) {
					Logs.d(TAG, bundle.toString());
				}

				@Override
				public void onWeiboException(WeiboException e) {

				}

				@Override
				public void onCancel() {

				}
			})) {
				ShareCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
			}
		}
	}

	private String getToken() {
		Oauth2AccessToken mAccessToken = getLocalAccessToken();
		String token = null;
		if (mAccessToken != null) {
			token = mAccessToken.getToken();
		}
		return token;
	}

	private Oauth2AccessToken getLocalAccessToken() {
		return mKeeper.readAccessToken();
	}

	private void handlerSSO() {
		mSSOHandler = new SsoHandler(this, mAuthInfo);
		// 未安装微博情况下，需要调用web？
		mSSOHandler.authorize(mAuthListener);
	}

	private AuthInfo createAuthInfo() {
		return new AuthInfo(this, sina.getKey(), sina.getRedirectUrl(), sina.getScope());
	}

	@Override
	public void onResponse(BaseResponse baseResponse) {
		if (baseResponse == null) {
			ShareCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
			return;
		}
		// 分享成功后返回的是 ErrorCode.ERR_CANCEL？？？必须是倒计时结束后才能回调分享成功，提前关闭则分享取消！！！
		switch (baseResponse.errCode) {
			case WBConstants.ErrorCode.ERR_OK:
				ShareCallbackReceiver.sendBroadcastComplete(this);
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				ShareCallbackReceiver.sendBroadcastCancel(this);
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				ShareCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
				break;
			default:
				finish();
				break;
		}
	}

	private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

		@Override
		public void onComplete(Bundle bundle) {
			mSSOHandler = null;
			Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
			Logs.d(TAG, bundle.toString());
			if (newToken.isSessionValid()) {
				mKeeper.writeAccessToken(newToken);
				Logs.d(TAG, "operate is " + operate.toString());
				switch (operate) {
					case Auth:
					case AuthAndInfo:
						login();
						break;
					case DeAuth:
						logout();
						break;
					case Share:
						share();
						break;
				}
				return;
			}
			ShareCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_SHARE);
			AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			e.printStackTrace();
			ShareCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_SHARE);
			AuthCallbackReceiver.sendBroadcastError(SinaWeiboEntryActivity.this, ErrorCode.ERROR_AUTH);
		}

		@Override
		public void onCancel() {
			ShareCallbackReceiver.sendBroadcastCancel(SinaWeiboEntryActivity.this);
			AuthCallbackReceiver.sendBroadcastCancel(SinaWeiboEntryActivity.this);
		}
	};
}

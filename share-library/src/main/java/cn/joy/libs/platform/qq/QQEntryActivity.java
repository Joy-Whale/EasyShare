package cn.joy.libs.platform.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.joy.libs.platform.AuthCallbackReceiver;
import cn.joy.libs.platform.ErrorCode;
import cn.joy.libs.platform.Logs;
import cn.joy.libs.platform.Operate;
import cn.joy.libs.platform.Platform;
import cn.joy.libs.platform.PlatformAuthInfo;
import cn.joy.libs.platform.PlatformFactory;
import cn.joy.libs.platform.ShareCallbackReceiver;

public class QQEntryActivity extends Activity implements IUiListener {

	private static final String TAG = "QQEntryActivity";
	private static final String EXTRA_SHARE_PARAMS = "EXTRA_SHARE_PARAMS";
	private static final String EXTRA_SHARE_TARGET = "EXTRA_SHARE_TARGET";
	private static final String EXTRA_OPERATE = "EXTRA_OPERATE";

	/**
	 * 登录
	 */
	static void login(Context context) {
		context.startActivity(new Intent(context, QQEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_OPERATE, Platform.Operate.Login));
	}

	static void auth(Context context, boolean requestUserInfo) {
		context.startActivity(new Intent(context, QQEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_OPERATE, requestUserInfo ? Operate.AuthAndInfo : Operate.Auth));
	}

	static void deAuth(Context context) {
		context.startActivity(new Intent(context, QQEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(EXTRA_OPERATE, Operate.DeAuth));
	}

	/**
	 * 分享
	 * @param context context
	 * @param args    参数
	 * @param target  分享对象 QQ/QZone
	 */
	static void share(Context context, Bundle args, int target) {
		context.startActivity(new Intent(context, QQEntryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.putExtra(EXTRA_SHARE_PARAMS, args)
				.putExtra(EXTRA_SHARE_TARGET, target)
				.putExtra(EXTRA_OPERATE, Operate.Share));
	}

	private Operate operate;
	private QQ qq;
	private QQAccessTokenKeeper mKeeper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mKeeper = new QQAccessTokenKeeper(this);
		qq = PlatformFactory.getQQ();
		// 查看本地是否保存有QQ 各种key，然后尝试登陆
		QQAuthInfo authInfo = mKeeper.readAccessToken();
		if (authInfo != null) {
			qq.getApi().setOpenId(authInfo.getOpenId());
			qq.getApi().setAccessToken(authInfo.getAccessToken(), String.valueOf(authInfo.getExpires()));
		}
		operate = (Operate) getIntent().getSerializableExtra(EXTRA_OPERATE);
		switch (operate) {
			case Auth:
			case AuthAndInfo:
				startAuth();
				break;
			case DeAuth:
				startDeAuth();
				break;
			case Share:
				startShare(getIntent());
				break;
		}
	}

	/**
	 * 开始分享
	 * @param intent intent
	 */
	private void startShare(Intent intent) {
		Bundle bundle = intent.getBundleExtra(EXTRA_SHARE_PARAMS);
		int shareTarget = intent.getIntExtra(EXTRA_SHARE_TARGET, QQShareBase.SHARE_TARGET_QQ);
		if (bundle != null) {
			switch (shareTarget) {
				case QQShareBase.SHARE_TARGET_QQ:
					PlatformFactory.getQQ().getApi().shareToQQ(this, bundle, this);
					break;
				case QQShareBase.SHARE_TARGET_QZONE:
					PlatformFactory.getQQ().getApi().shareToQzone(this, bundle, this);
					break;
			}
		} else {
			onError(null);
		}
	}

	/**
	 * 开始登录
	 */
	private void startAuth() {
		PlatformFactory.getQQ().getApi().login(this, "all", this);
	}

	private void startDeAuth() {
		PlatformFactory.getQQ().getApi().logout(this);
		mKeeper.clear();
		AuthCallbackReceiver.sendBroadcastComplete(this, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Tencent.onActivityResultData(requestCode, resultCode, data, this);
		finish();
	}

	@Override
	public void onComplete(Object o) {
		switch (operate) {
			case Auth:
			case AuthAndInfo:
				try {
					final QQAuthInfo info = new QQAuthInfo();
					Logs.d(TAG, o.toString());
					final JSONObject object = (JSONObject) o;
					if (object.getInt("ret") != 0) {
						AuthCallbackReceiver.sendBroadcastError(QQEntryActivity.this, ErrorCode.ERROR_AUTH);
						return;
					}
					if (object.has("openid") && object.has("access_token") && !TextUtils.isEmpty(object.getString("openid"))) {
						info.setOpenId(object.getString("openid"));
						info.setAccessToken(object.getString("access_token"));
						info.setPayToken(object.getString("pay_token"));
						info.setExpires(object.getLong("expires_in"));
						info.setPf(object.getString("pf"));
						info.setPfKey(object.getString("pfkey"));
						qq.getApi().setOpenId(info.getOpenId());
						qq.getApi().setAccessToken(info.getAccessToken(), String.valueOf(info.getExpires()));
						// 将Token信息保存到本地
						mKeeper.writeAccessToken(info);
					}
					// 尝试获取授权信息并保存,分享时会有auth信息吗？？？
					if (operate.equals(Operate.Auth)) {
						AuthCallbackReceiver.sendBroadcastComplete(this, info);
						return;
					}
					UserInfo userInfo = new UserInfo(this, qq.getApi().getQQToken());
					userInfo.getUserInfo(new IUiListener() {
						@Override
						public void onComplete(Object o) {
							Logs.d(TAG, o.toString());
							JSONObject object2 = (JSONObject) o;
							try {
								if (object2.getInt("ret") != 0) {
									AuthCallbackReceiver.sendBroadcastError(QQEntryActivity.this, ErrorCode.ERROR_AUTH);
									return;
								}
								info.setNickname(object2.getString("nickname"));
								info.setCity(object2.getString("city"));
								info.setSex(object2.getString("gender").equals("男") ? PlatformAuthInfo.Sex.Male : PlatformAuthInfo.Sex.Female);
								info.setAvatar(object2.getString("figureurl_2"));
								info.setProvince(object2.getString("province"));
								AuthCallbackReceiver.sendBroadcastComplete(QQEntryActivity.this, info);
							} catch (JSONException e) {
								e.printStackTrace();
								AuthCallbackReceiver.sendBroadcastError(QQEntryActivity.this, ErrorCode.ERROR_AUTH);
							}
						}

						@Override
						public void onError(UiError uiError) {
							AuthCallbackReceiver.sendBroadcastError(QQEntryActivity.this, ErrorCode.ERROR_AUTH);
						}

						@Override
						public void onCancel() {
							AuthCallbackReceiver.sendBroadcastCancel(QQEntryActivity.this);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_AUTH);
				}
				break;
			case DeAuth:
				AuthCallbackReceiver.sendBroadcastComplete(this, null);
				break;
			case Share:
				ShareCallbackReceiver.sendBroadcastComplete(this);
				break;
		}
	}

	@Override
	public void onError(UiError uiError) {
		Logs.d(TAG, uiError.errorCode + " " + uiError.errorMessage + " " + uiError.errorDetail);
		switch (operate) {
			case Auth:
			case AuthAndInfo:
			case DeAuth:
				AuthCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_AUTH);
				break;
			case Share:
				ShareCallbackReceiver.sendBroadcastError(this, ErrorCode.ERROR_SHARE);
				break;
		}
	}

	@Override
	public void onCancel() {
		switch (operate) {
			case Auth:
			case AuthAndInfo:
			case DeAuth:
				AuthCallbackReceiver.sendBroadcastCancel(this);
				break;
			case Share:
				ShareCallbackReceiver.sendBroadcastCancel(this);
				break;
		}
	}
}

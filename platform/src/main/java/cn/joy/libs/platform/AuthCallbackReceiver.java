package cn.joy.libs.platform;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 17-15
 */

public class AuthCallbackReceiver extends BroadcastReceiver {

	private static final String TAG = "AUTHCallbackReceiver";
	private static final String ACTION_AUTH_CALLBACK = "cn.joy.libs.platforms.ACTION_AUTH_CALLBACK";
	private static final String EXTRA_CALLBACK_CODE = "EXTRA_CALLBACK_CODE";
	private static final int CALLBACK_CODE_COMPLETE = 1;
	private static final int CALLBACK_CODE_CANCEL = 2;
	private static final int CALLBACK_CODE_ERROR = 3;
	private static final String EXTRA_CALLBACK_ERROR_CODE = "EXTRA_CALLBACK_ERROR_CODE";
	private static final String EXTRA_CALLBACK_PLATFORM_USER_INFO = "EXTRA_CALLBACK_PLATFORM_USER_INFO";
	private Context context;
	private Auth auth;
	private boolean isRegister = false;

	public void register(Context context, Auth auth) {
		this.context = context;
		this.auth = auth;
		context.registerReceiver(this, new IntentFilter(ACTION_AUTH_CALLBACK));
		isRegister = true;
	}

	public void unregister() {
		if (isRegister)
			context.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (auth == null)
			return;
		if (intent.getAction().equals(ACTION_AUTH_CALLBACK)) {
			int result = intent.getIntExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_ERROR);
			Logs.d(TAG, result + "");
			switch (result) {
				case CALLBACK_CODE_CANCEL:
					auth.onCancel();
					break;
				case CALLBACK_CODE_ERROR:
					int errorCode = intent.getIntExtra(EXTRA_CALLBACK_ERROR_CODE, ErrorCode.ERROR_AUTH);
					auth.onError(errorCode);
					break;
				case CALLBACK_CODE_COMPLETE:
					auth.onComplete((PlatformAuthInfo) intent.getParcelableExtra(EXTRA_CALLBACK_PLATFORM_USER_INFO));
					break;
			}
		}
	}

	public static <T extends PlatformAuthInfo> void sendBroadcastComplete(Context context, T info) {
		Intent intent = new Intent(ACTION_AUTH_CALLBACK).putExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_COMPLETE);
		if (info != null) {
			intent.putExtra(EXTRA_CALLBACK_PLATFORM_USER_INFO, info);
		}
		Logs.d(TAG, "sendBroadcastComplete info ---> " + info);
		context.sendBroadcast(intent);
		finishTargetActivity(context);
	}

	public static void sendBroadcastCancel(Context context) {
		context.sendBroadcast(new Intent(ACTION_AUTH_CALLBACK).putExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_CANCEL));
		finishTargetActivity(context);
	}

	public static void sendBroadcastError(Context context, int errorCode) {
		context.sendBroadcast(new Intent(ACTION_AUTH_CALLBACK).putExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_ERROR)
				.putExtra(EXTRA_CALLBACK_ERROR_CODE, errorCode));
		finishTargetActivity(context);
	}


	private static void finishTargetActivity(Context context) {
		if (context instanceof Activity) {
			((Activity) context).finish();
		}
	}
}

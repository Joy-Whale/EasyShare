package cn.joy.libs.platform;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 17-47
 *
 */

public class ShareCallbackReceiver extends BroadcastReceiver {

	private static final String TAG = "ShareCallbackReceiver";
	private static final String ACTION_SHARE_CALLBACK = "cn.joy.libs.platforms.ACTION_SHARE_CALLBACK";
	private static final String EXTRA_CALLBACK_CODE = "EXTRA_CALLBACK_CODE";
	private static final int CALLBACK_CODE_COMPLETE = 1;
	private static final int CALLBACK_CODE_CANCEL = 2;
	private static final int CALLBACK_CODE_ERROR = 3;
	private static final String EXTRA_CALLBACK_ERROR_CODE = "EXTRA_CALLBACK_ERROR_CODE";

	private Context context;
	private Share share;
	private boolean isRegister = false;

	public void register(Context context, Share share) {
		this.context = context;
		this.share = share;
		context.registerReceiver(this, new IntentFilter(ACTION_SHARE_CALLBACK));
		isRegister = true;
	}

	void unregister() {
		if (isRegister)
			context.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (share == null)
			return;
		if (intent.getAction().equals(ACTION_SHARE_CALLBACK)) {
			Logs.d(TAG, intent.getIntExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_ERROR) + "");
			switch (intent.getIntExtra(EXTRA_CALLBACK_CODE, CALLBACK_CODE_ERROR)) {
				case CALLBACK_CODE_CANCEL:
					share.onCancel();
					break;
				case CALLBACK_CODE_ERROR:
					int errorCode = intent.getIntExtra(EXTRA_CALLBACK_ERROR_CODE, ErrorCode.ERROR_SHARE);
					share.onError(errorCode);
					break;
				case CALLBACK_CODE_COMPLETE:
					share.onComplete();
					break;
			}
		}
	}

	public static void sendBroadcastComplete(Context context) {
		context.sendBroadcast(new Intent(ShareCallbackReceiver.ACTION_SHARE_CALLBACK).putExtra(ShareCallbackReceiver.EXTRA_CALLBACK_CODE, ShareCallbackReceiver.CALLBACK_CODE_COMPLETE));
		finishTargetActivity(context);
	}

	public static void sendBroadcastCancel(Context context) {
		context.sendBroadcast(new Intent(ShareCallbackReceiver.ACTION_SHARE_CALLBACK).putExtra(ShareCallbackReceiver.EXTRA_CALLBACK_CODE, ShareCallbackReceiver.CALLBACK_CODE_CANCEL));
		finishTargetActivity(context);
	}

	public static void sendBroadcastError(Context context, int errorCode) {
		context.sendBroadcast(new Intent(ShareCallbackReceiver.ACTION_SHARE_CALLBACK).putExtra(ShareCallbackReceiver.EXTRA_CALLBACK_CODE, ShareCallbackReceiver.CALLBACK_CODE_ERROR)
				.putExtra(ShareCallbackReceiver.EXTRA_CALLBACK_ERROR_CODE, errorCode));
		finishTargetActivity(context);
	}

	private static void finishTargetActivity(Context context) {
		if (context instanceof Activity) {
			((Activity) context).finish();
		}
	}
}

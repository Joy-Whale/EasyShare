package cn.joy.libs.platform.qq;

import android.content.Context;

import com.tencent.tauth.Tencent;

import cn.joy.libs.platform.Logs;
import cn.joy.libs.platform.Platform;
import cn.joy.libs.platform.Utils;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 15-04
 */

public class QQ extends Platform<Tencent> {

	public static final String NAME = "QQ";
	private static final String TAG = "QQ";
	private Tencent mTencent;

	public QQ(Context context, String key, String secret) {
		super(context, key, secret);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean checkInstall() {
		return Utils.isQQInstall();
	}

	@Override
	public void register() {
		mTencent = Tencent.createInstance(getKey(), getContext().getApplicationContext());
		Logs.d(TAG, mTencent.isReady() + "");
	}

	@Override
	public Tencent getApi() {
		return mTencent;
	}
}

package cn.joy.libs.platform.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import cn.joy.libs.platform.Auth;
import cn.joy.libs.platform.AuthBuilder;
import cn.joy.libs.platform.Logs;
import cn.joy.libs.platform.PlatformActionListener;
import cn.joy.libs.platform.PlatformAuthInfo;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 09-43
 */

public class LoginActivity extends Activity implements PlatformActionListener<PlatformAuthInfo> {

	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn1)
	void loginWeichat() {
		new AuthBuilder().authTo(Auth.Target.Wechat).listener(this).auth();
	}

	@OnClick(R.id.btn2)
	void loginQQ() {
		new AuthBuilder().authTo(Auth.Target.QQ).listener(this).auth();
	}

	@OnClick(R.id.btn3)
	void loginSina() {
		new AuthBuilder().authTo(Auth.Target.Sina).listener(this).auth();
	}

	@Override
	public void onComplete(PlatformAuthInfo info) {
		Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
		if (info != null)
			Logs.d(TAG, info.toString());
	}

	@Override
	public void onCancel() {
		Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(int code) {
		Toast.makeText(this, "error " + code, Toast.LENGTH_SHORT).show();
	}
}

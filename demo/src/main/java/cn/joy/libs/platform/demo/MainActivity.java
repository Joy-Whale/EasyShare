package cn.joy.libs.platform.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn1)
	void share() {
		startActivity(new Intent(this, ShareActivity.class));
	}

	@OnClick(R.id.btn2)
	void login() {
		startActivity(new Intent(this, LoginActivity.class));
	}

	@OnClick(R.id.btn3)
	void logout() {
		startActivity(new Intent(this, LogoutActivity.class));
	}
}

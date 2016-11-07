package cn.joy.libs.platform.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.widget.Toast;

import java.io.File;

import cn.joy.libs.platform.PlatformActionListener;
import cn.joy.libs.platform.Share;
import cn.joy.libs.platform.ShareBuilder;
import cn.joy.libs.platform.ShareParams;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 09-43
 */

public class ShareActivity extends Activity {

	private static final String TAG = "ShareActivity";
	private static final String TARGET_URL = "http://www.baidu.com";

	private String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/image.jpg";
	private PlatformActionListener listener = new PlatformActionListener() {
		@Override
		public void onComplete(Object o) {
			Toast.makeText(ShareActivity.this, "success", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(int errorCode) {
			Toast.makeText(ShareActivity.this, "error:" + errorCode, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(ShareActivity.this, "cancel", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.main_share_text)
	void shareText() {
		new ShareDialog(this).targetUrl(TARGET_URL).title("1111111").content("2222222").type(ShareParams.ShareType.Text).show();
	}

	@OnClick(R.id.main_share_image)
	void shareImage() {
		new ShareDialog(this).targetUrl(TARGET_URL).title("1111111").content("2222222").image(imagePath).type(ShareParams.ShareType.Image).show();
	}

	@OnClick(R.id.main_share_text_image)
	void shareTextImage() {
		new ShareDialog(this).targetUrl(TARGET_URL)
				.title("1111111")
				.content("2222222")
				.image(imagePath)
				.type(ShareParams.ShareType.TextAndImage)
				.show();
	}

	 class ShareDialog extends BottomSheetDialog {

		private ShareParams params;

		ShareDialog(@NonNull Context context) {
			super(context);
			setContentView(R.layout.dialog_share);
			ButterKnife.bind(this);
			params = new ShareParams();
			params.setShareActivity(ShareActivity.this);
		}

		 ShareDialog title(String title) {
			this.params.setTitle(title);
			return this;
		}

		ShareDialog targetUrl(String targetUrl) {
			this.params.setTargetUrl(targetUrl);
			return this;
		}

		ShareDialog content(String content) {
			this.params.setContent(content);
			return this;
		}

		ShareDialog type(ShareParams.ShareType type) {
			this.params.setShareType(type);
			return this;
		}

		ShareDialog image(String url) {
			this.params.setImage(new ShareParams.ShareImage(new File(url)));
			return this;
		}

		@OnClick(R.id.share_qqzone)
		void shareQQZone() {
			new ShareBuilder().params(params).listener(listener).shareTo(Share.Target.QQ).share();
			dismiss();
		}

		@OnClick(R.id.share_weibo)
		void shareWeibo() {
			new ShareBuilder().params(params).listener(listener).shareTo(Share.Target.SinaWeibo).share();
			dismiss();
		}

		@OnClick(R.id.share_wechat)
		void shareWechat() {
			new ShareBuilder().params(params).listener(listener).shareTo(Share.Target.Wechat).share();
			dismiss();
		}

		@OnClick(R.id.share_moments)
		void shareWechatMoments() {
			new ShareBuilder().params(params).listener(listener).shareTo(Share.Target.WechatMoments).share();
			dismiss();
		}
	}
}

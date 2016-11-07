package cn.joy.libs.platform;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * ShareBuilder为分享建造类,通过设置不同参数分享到不同平台
 */

public class ShareBuilder {

	private ShareParams params;
	private Share.Target target;
	private PlatformActionListener listener;
	private Activity activity;
	private boolean shareByClient = true;

	/**
	 * 设置分享的宿主Activity
	 * @param activity activity
	 */
	public ShareBuilder from(@NonNull Activity activity) {
		this.activity = activity;
		getShareParams().setShareActivity(activity);
		return this;
	}

	/**
	 * 分享结果监听
	 * @param listener 监听接口
	 */
	public ShareBuilder listener(PlatformActionListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * 分享的平台
	 * @param target 平台类型
	 */
	public ShareBuilder shareTo(Share.Target target) {
		this.target = target;
		return this;
	}

	/**
	 * 分享参数object
	 * @param params 参数封装object
	 */
	public ShareBuilder params(ShareParams params) {
		this.params = params;
		return this;
	}

	/**
	 * 分享类型
	 * @param type 类型值 详见{@link ShareParams.ShareType}
	 */
	public ShareBuilder type(ShareParams.ShareType type) {
		getShareParams().setShareType(type);
		return this;
	}

	/**
	 * 分享标题
	 * @param title 标题
	 */
	public ShareBuilder title(String title) {
		getShareParams().setTitle(title);
		return this;
	}

	/**
	 * 分享描述内容
	 * @param content 描述内容
	 */
	public ShareBuilder content(String content) {
		getShareParams().setContent(content);
		return this;
	}

	/**
	 * 点击分享内容后跳转的链接
	 * @param url 需要跳转的链接
	 */
	public ShareBuilder targetUrl(String url) {
		getShareParams().setTargetUrl(url);
		return this;
	}

	/**
	 * 需要分享的图片
	 * @param image 图片Object
	 */
	public ShareBuilder image(ShareParams.ShareImage image) {
		getShareParams().setImage(image);
		return this;
	}

	public ShareBuilder byClient(boolean shareByClient) {
		this.shareByClient = shareByClient;
		return this;
	}

	private ShareParams getShareParams() {
		if (params == null) {
			params = new ShareParams();
		}
		if (params.getShareActivity() == null && activity != null) {
			params.setShareActivity(activity);
		}
		return params;
	}

	/**
	 * 分享
	 */
	public void share() {
		if (target == null) {
			onError(ErrorCode.ERROR_NULL_SHARE_TARGET);
			return;
		}
		if (params == null) {
			onError(ErrorCode.ERROR_NULL_SHARE_PARAMS);
			return;
		}
		Share share = ShareFactory.create(target, params, shareByClient);
		if(share != null){
			share.listener(listener).share();
		}
	}

	private void onError(int code) {
		if (listener != null) {
			listener.onError(code);
		}
	}
}

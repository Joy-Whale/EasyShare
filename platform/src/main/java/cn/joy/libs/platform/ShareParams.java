package cn.joy.libs.platform;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 11-41
 */

public class ShareParams {

	/** 分享类型 */
	public enum ShareType {
		Text, Image, TextAndImage, WebPage, Music, Video
	}
	//	public static final int SHARE_TYPE_TEXT = 1;  //  文字
	//	public static final int SHARE_TYPE_IMAGE_ONLY = 2; //  纯图
	//	public static final int SHARE_TYPE_IMAGE_WITH_TEXT = 3;  //  图片加文字
	//	public static final int SHARE_TYPE_WEBPAGE = 4;  // 网页
	//	public static final int SHARE_TYPE_MUSIC = 5;   // 音乐
	//	public static final int SHARE_TYPE_VIDEO = 6;   // 视频
	//	/** 下面仅微信支持 */
	//	public static final int SHARE_TYPE_APPS = 7;
	//	public static final int SHARE_TYPE_FILE = 8;
	//	public static final int SHARE_TYPE_EMOJI = 9;

	/** 下面为分享的参数 */
	//  宿主activity
	private static final String PARAM_SHARE_ACTIVITY = "share_activity";
	//  类型
	private static final String PARAM_SHARE_TYPE = "share_type";
	//  标题
	private static final String PARAM_TITLE = "title";
	//  标题链接
	private static final String PARAM_TARGET_URI = "title_uri";
	//  内容
	private static final String PARAM_CONTENT = "content";
	//  本地图片
	private static final String PARAM_IMAGE_FILE = "image_file";
	//  网络图片
	private static final String PARAM_IMAGE = "image_url";

	/**
	 * 图片包装类
	 */
	public static class ShareImage implements Parcelable {

		/**
		 * 图片源
		 */
		public enum Source {
			Http, File, Bitmap
			//			, Resource
		}

		private Source source;
		private String url;
		private File file;
		private Bitmap bitmap;

		public ShareImage(String url) {
			this.source = Source.Http;
			this.url = url;
		}

		public ShareImage(Bitmap bitmap) {
			this.source = Source.Bitmap;
			this.bitmap = bitmap;
		}

		public ShareImage(File file) {
			this.source = Source.File;
			this.file = file;
		}

		public Source getSource() {
			return source;
		}

		public String getImageUrl() {
			return url;
		}


		public Bitmap getImageBitmap() {
			return bitmap;
		}

		public File getImageFile() {
			return file;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.source == null ? -1 : this.source.ordinal());
			dest.writeString(this.url);
			dest.writeSerializable(this.file);
			dest.writeParcelable(this.bitmap, flags);
		}

		protected ShareImage(Parcel in) {
			int tmpSource = in.readInt();
			this.source = tmpSource == -1 ? null : Source.values()[tmpSource];
			this.url = in.readString();
			this.file = (File) in.readSerializable();
			this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
		}

		public static final Creator<ShareImage> CREATOR = new Creator<ShareImage>() {
			@Override
			public ShareImage createFromParcel(Parcel source) {
				return new ShareImage(source);
			}

			@Override
			public ShareImage[] newArray(int size) {
				return new ShareImage[size];
			}
		};
	}

	protected Map<String, Object> params = new HashMap<>();

	/**
	 * 添加 key-value 内容键值对
	 * @param key 关键字
	 * @param obj 内容
	 */
	private ShareParams put(String key, Object obj) {
		params.put(key, obj);
		return this;
	}

	/**
	 * 加载值
	 * @param key          关键字
	 * @param clazz        值类型
	 * @param defaultValue 默认值,如果不存在
	 * @param <T>          值泛型
	 * @return 若存在，则返回；若默认值存在，返回默认值，否则返回null
	 */
	private <T> T get(String key, Class<T> clazz, T defaultValue) {
		Object obj = params.get(key);
		return obj == null ? defaultValue : clazz.cast(obj);
	}

	/**
	 * 加载值
	 * @param key   关键字
	 * @param clazz 值类型
	 * @param <T>   值泛型
	 * @return 若存在，则返回；否则返回null
	 */
	private <T> T get(String key, Class<T> clazz) {
		Object obj = params.get(key);
		return obj == null ? null : clazz.cast(obj);
	}

	public Activity getShareActivity() {
		return get(PARAM_SHARE_ACTIVITY, Activity.class);
	}

	public void setShareActivity(Activity activity) {
		put(PARAM_SHARE_ACTIVITY, activity);
	}

	public ShareType getShareType() {
		return get(PARAM_SHARE_TYPE, ShareType.class, ShareType.Text);
	}

	public void setShareType(ShareType shareType) {
		put(PARAM_SHARE_TYPE, shareType);
	}

	public String getTitle() {
		return get(PARAM_TITLE, String.class);
	}

	public void setTitle(String title) {
		put(PARAM_TITLE, title);
	}

	public String getTargetUrl() {
		return get(PARAM_TARGET_URI, String.class);
	}

	public void setTargetUrl(String targetUrl) {
		put(PARAM_TARGET_URI, targetUrl);
	}

	public String getContent() {
		return get(PARAM_CONTENT, String.class);
	}

	public void setContent(String content) {
		put(PARAM_CONTENT, content);
	}

	public void setImage(ShareImage image) {
		put(PARAM_IMAGE, image);
	}

	public ShareImage getImage() {
		return get(PARAM_IMAGE, ShareImage.class);
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		params.clear();
		params = null;
	}
}

package cn.joy.libs.platform.wechat;

import android.os.Parcel;
import android.os.Parcelable;

import cn.joy.libs.platform.Platform;
import cn.joy.libs.platform.PlatformAuthInfo;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 10-45
 */

public class WeChatAuthInfo extends PlatformAuthInfo {

	private long expires;
	private String refreshToken;
	private String scope;
	private String unionId;

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	@Override
	public final Platform.Target getTarget() {
		return Platform.Target.Wechat;
	}

	public WeChatAuthInfo() {
	}

	@Override
	public String toString() {
		return "WeChatAuthInfo{" +
				", expires=" + expires +
				", refreshToken='" + refreshToken + '\'' +
				", scope='" + scope + '\'' +
				", unionId='" + unionId + '\'' +
				"} " + super.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(this.expires);
		dest.writeString(this.refreshToken);
		dest.writeString(this.scope);
		dest.writeString(this.unionId);
	}

	protected WeChatAuthInfo(Parcel in) {
		super(in);
		this.expires = in.readLong();
		this.refreshToken = in.readString();
		this.scope = in.readString();
		this.unionId = in.readString();
	}

	public static final Parcelable.Creator<WeChatAuthInfo> CREATOR = new Parcelable.Creator<WeChatAuthInfo>() {
		@Override
		public WeChatAuthInfo createFromParcel(Parcel source) {
			return new WeChatAuthInfo(source);
		}

		@Override
		public WeChatAuthInfo[] newArray(int size) {
			return new WeChatAuthInfo[size];
		}
	};
}

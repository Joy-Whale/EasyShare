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

public class WechatAuthInfo extends PlatformAuthInfo {

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

	public WechatAuthInfo() {
	}

	@Override
	public String toString() {
		return "WechatAuthInfo{" +
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

	protected WechatAuthInfo(Parcel in) {
		super(in);
		this.expires = in.readLong();
		this.refreshToken = in.readString();
		this.scope = in.readString();
		this.unionId = in.readString();
	}

	public static final Parcelable.Creator<WechatAuthInfo> CREATOR = new Parcelable.Creator<WechatAuthInfo>() {
		@Override
		public WechatAuthInfo createFromParcel(Parcel source) {
			return new WechatAuthInfo(source);
		}

		@Override
		public WechatAuthInfo[] newArray(int size) {
			return new WechatAuthInfo[size];
		}
	};
}

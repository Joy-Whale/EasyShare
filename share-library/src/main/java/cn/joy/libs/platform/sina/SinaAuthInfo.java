package cn.joy.libs.platform.sina;

import android.os.Parcel;
import android.os.Parcelable;

import cn.joy.libs.platform.Platform;
import cn.joy.libs.platform.PlatformAuthInfo;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 14-14
 */

public class SinaAuthInfo extends PlatformAuthInfo {

	private long expires;
	private String refreshToken;
	private String phoneNumber;

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

	public SinaAuthInfo() {
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public final Platform.Target getTarget() {
		return Platform.Target.Sina;
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
		dest.writeString(this.phoneNumber);
	}

	protected SinaAuthInfo(Parcel in) {
		super(in);
		this.expires = in.readLong();
		this.refreshToken = in.readString();
		this.phoneNumber = in.readString();
	}

	public static final Parcelable.Creator<SinaAuthInfo> CREATOR = new Parcelable.Creator<SinaAuthInfo>() {
		@Override
		public SinaAuthInfo createFromParcel(Parcel source) {
			return new SinaAuthInfo(source);
		}

		@Override
		public SinaAuthInfo[] newArray(int size) {
			return new SinaAuthInfo[size];
		}
	};
}

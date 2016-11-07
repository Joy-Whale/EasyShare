package cn.joy.libs.platform.qq;

import android.os.Parcel;
import android.os.Parcelable;

import cn.joy.libs.platform.Platform;
import cn.joy.libs.platform.PlatformAuthInfo;

/**
 * User: JiYu
 * Date: 2016-07-27
 * Time: 12-48
 */

public class QQAuthInfo extends PlatformAuthInfo {

	private long expires;
	private String payToken;
	private String pf;
	private String pfKey;
	private int queryAuthorityCost, authorityCost;

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getPayToken() {
		return payToken;
	}

	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPfKey() {
		return pfKey;
	}

	public void setPfKey(String pfKey) {
		this.pfKey = pfKey;
	}

	public int getQueryAuthorityCost() {
		return queryAuthorityCost;
	}

	public void setQueryAuthorityCost(int queryAuthorityCost) {
		this.queryAuthorityCost = queryAuthorityCost;
	}

	public int getAuthorityCost() {
		return authorityCost;
	}

	public void setAuthorityCost(int authorityCost) {
		this.authorityCost = authorityCost;
	}

	@Override
	public final Platform.Target getTarget() {
		return Platform.Target.QQ;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(this.expires);
		dest.writeString(this.payToken);
		dest.writeString(this.pf);
		dest.writeString(this.pfKey);
		dest.writeInt(this.queryAuthorityCost);
		dest.writeInt(this.authorityCost);
	}

	public QQAuthInfo() {
	}

	protected QQAuthInfo(Parcel in) {
		super(in);
		this.expires = in.readLong();
		this.payToken = in.readString();
		this.pf = in.readString();
		this.pfKey = in.readString();
		this.queryAuthorityCost = in.readInt();
		this.authorityCost = in.readInt();
	}

	public static final Parcelable.Creator<QQAuthInfo> CREATOR = new Parcelable.Creator<QQAuthInfo>() {
		@Override
		public QQAuthInfo createFromParcel(Parcel source) {
			return new QQAuthInfo(source);
		}

		@Override
		public QQAuthInfo[] newArray(int size) {
			return new QQAuthInfo[size];
		}
	};

	@Override
	public String toString() {
		return "QQAuthInfo{" +
				"expires=" + expires +
				", payToken='" + payToken + '\'' +
				", pf='" + pf + '\'' +
				", pfKey='" + pfKey + '\'' +
				", queryAuthorityCost=" + queryAuthorityCost +
				", authorityCost=" + authorityCost +
				"} " + super.toString();
	}
}

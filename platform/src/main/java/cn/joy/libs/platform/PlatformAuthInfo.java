package cn.joy.libs.platform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 18-11
 */

public class PlatformAuthInfo implements Parcelable {

	public enum Sex {
		Male, Female
	}

	private String nickname;
	private String avatar;
	private String city;
	private String country;
	private String province;
	private Sex sex;

	private String openId;

	private String accessToken;

	public PlatformAuthInfo() {

	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Platform.Target getTarget() {
		return null;
	}

	@Override
	public String toString() {
		return "PlatformAuthInfo{" +
				"nickname='" + nickname + '\'' +
				", avatar='" + avatar + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", province='" + province + '\'' +
				", sex=" + sex +
				", openId='" + openId + '\'' +
				", accessToken='" + accessToken + '\'' +
				'}';
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.nickname);
		dest.writeString(this.avatar);
		dest.writeString(this.city);
		dest.writeString(this.country);
		dest.writeString(this.province);
		dest.writeInt(this.sex == null ? -1 : this.sex.ordinal());
		dest.writeString(this.openId);
		dest.writeString(this.accessToken);
	}

	protected PlatformAuthInfo(Parcel in) {
		this.nickname = in.readString();
		this.avatar = in.readString();
		this.city = in.readString();
		this.country = in.readString();
		this.province = in.readString();
		int tmpSex = in.readInt();
		this.sex = tmpSex == -1 ? null : Sex.values()[tmpSex];
		this.openId = in.readString();
		this.accessToken = in.readString();
	}

}

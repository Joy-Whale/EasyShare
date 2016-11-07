package cn.joy.libs.platform;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 16-49
 */

public interface PlatformActionListener<T> {

	void onComplete(T t);

	void onError(int errorCode);

	void onCancel();
}

package cn.joy.libs.platform;

import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 15-04
 */

public class Utils {

	private static final String QQ_PACKAGE = "com.tencent.mm";
	private static final String QZONE_PACKAGE = "com.qzone";

	public static boolean isAppInstall(String packageName) {
		try {
			PlatformManager.getInstance().getContext().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public static boolean isQQInstall() {
		return isAppInstall(QQ_PACKAGE);
	}

	public static boolean isQZoneInstall() {
		return isAppInstall(QZONE_PACKAGE);
	}

	public static JSONObject requestGetJson(String urlString) {
		String str = requestGet(urlString);
		try {
			return TextUtils.isEmpty(str) ? null : new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String requestGet(String urlString) {
		String str = null;
		HttpURLConnection conn;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET"); // 设置请求方法为post
			conn.setReadTimeout(5000); // 设置读取超时为5秒
			conn.setConnectTimeout(10000); // 设置连接网络超时为10秒
			if (conn.getResponseCode() == 200) {
				str = getStringFromInputStream(conn.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private static String getStringFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 模板代码 必须熟练
		byte[] buffer = new byte[1024];
		int len = -1;
		// 一定要写len=is.read(buffer)
		// 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
		os.close();
		return state;
	}
}

package cn.joy.libs.platform;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * User: JiYu
 * Date: 2016-07-26
 * Time: 15-04
 */

public class PlatformManager {

	private static PlatformManager sharePlatform;

	public static PlatformManager init(Context context) {
		return sharePlatform = new PlatformManager(context);
	}

	public static PlatformManager getInstance() {
		if (sharePlatform == null) {
			throw new NullPointerException("PlatformManager not init yet");
		}
		return sharePlatform;
	}

	private Context context;
	private Map<String, Platform> platformMap = new HashMap<>();

	private PlatformManager(Context context) {
		this.context = context.getApplicationContext();
	}

	public PlatformManager register(Platform... platform) {
		for (Platform aPlatform : platform) {
			aPlatform.register();
			platformMap.put(aPlatform.getName(), aPlatform);
		}
		return this;
	}

	Platform getPlatform(String name) {
		return platformMap.get(name);
	}

	public Context getContext() {
		return context;
	}
}

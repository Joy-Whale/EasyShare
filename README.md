# EasyShare
只集成QQ、微信、新浪微博平台，可以用来授权、分享、登录的轻量级库，简单易用，易于维护
鉴于当前某些第三方分享功能实在是难用，并且项目中也需要一个易于自己维护的分享Library（需要随时升级某个第三方平台，QQ、微信、新浪的sdk），然后就写出这个Libaray来

目前呢这个Library只支持微信、微博和QQ平台的登录鉴权以及分享功能，后续可能会加一些其他平台或其他功能.

##添加依赖
`compile 'cn.joy.libs:platform:1.0.4'`

##使用方法
1.在主package下添加微信必要的回调Activity,**需继承自cn.joy.libs.platform.wechat.WXEntryActivity**

  _若不使用微信授权和分享可跳过此步骤_

2.在AndroidManifest.xml中添加QQ和微信配置

  _若不使用微信、QQ授权和分享可跳过此步骤_

<!-- 微信以及QQ平台activity -->
        <!-- 微信以及QQ平台activity -->
        <!-- 微信以及QQ平台activity -->
        <activity
            android:name=".wxapi.WXEntryActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:exported="true"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"/>

        <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true"
            android:screenOrientation="portrait">
            <intent-filter>
                <action android:name="android.intent.action.VIEW"/>

                <category android:name="android.intent.category.DEFAULT"/>
                <category android:name="android.intent.category.BROWSABLE"/>

                <data android:scheme="tencent${QQ_KEY}"/>
            </intent-filter>
        </activity>

3.在Application onCreate()中添加Library初始化

    PlatformManager.init(this)
 	  .register(PlatformFactory.createWechat("你的微信 key", "你的微信 secret key"))
 	  .register(PlatformFactory.createQQ("你的QQ key", "你的QQ secret key"))
 	  .register(PlatformFactory.createSina("你的微博 key", "你的微博 secret key", "你的微博跳转 target url"));

4.授权登录

    PlatformActionListener<WechatAuthInfo> listener = new PlatformActionListener<WechatAuthInfo>() {
        /**
         * 授权成功
         * @param wechatAuthInfo 微信授权用户信息
         */
        @Override
        public void onComplete(WechatAuthInfo wechatAuthInfo) {
    
        }
    
        /**
         * 授权出错
         * @param errorCode 错误码{@link cn.joy.libs.platform.ErrorCode}
         */
        @Override
        public void onError(int errorCode) {
    
        }
    
        /**
         * 用户取消
         */
        @Override
        public void onCancel() {
    
        }
    };
		
    new AuthBuilder().authTo(Auth.Target.Wechat).listener(listener).auth();

5.分享

    private PlatformActionListener listener = new PlatformActionListener() {
	    /**
    	 * 分享成功
    	 * @param wechatAuthInfo 微信授权用户信息
    	 */
		@Override
		public void onComplete(Object o) {
	
		}

        /**
    	 * 分享失败
    	 * @param errorCode 错误码{@link cn.joy.libs.platform.ErrorCode}
    	 */
		@Override
		public void onError(int errorCode) {
			
		}
		
		/**
		 * 用户取消
		 */
		@Override
		public void onCancel() {
			
		}
	};
	
    new ShareBuilder()
            // activity
            .from(this)
            // 是否需要安装目标平台客户端
            .byClient(true)
            // 监听
            .listener(this)
            // 分享的标题
            .title("")
            // 分享的内容
            .content("")
            // 分享的跳转链接
            .targetUrl("")
            // 分享的图片。有三种格式，url、file、bitmap
            .image(new ShareParams.ShareImage(""))
            // 分享的类型。目前实现了纯文本和图文混合
            .type(ShareParams.ShareType.TextAndImage)
            // 分享的目标平台
            .shareTo(Share.Target.QQ)
            .share();


##更新日志
###1.0.4  2016-12-1
1.修复微信返回不能回调onCancel bug
2.优化大图分享

###1.0.3  2016-11-29
1.修复`java.lang.RuntimeException: Unable to start activity ComponentInfo{com.sina.weibo.sdk.demo/com.sina.weibo.sdk.component.WeiboSdkBrowser}: java.lang.IllegalArgumentException: Empty file name` bug

###1.0.2  2016-11-8
1.删除无用的依赖

###1.0.1  2016-11-7
1.首版功能完善

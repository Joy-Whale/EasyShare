package cn.joy.libs.platform;

/**
 * User: Joy
 * Date: 2016/11/2
 * Time: 17:16
 */

public interface ErrorCode {

	//  平台未注册
	int ERROR_NOT_INIT = 1001;
	//  应用未安装
	int ERROR_NOT_INSTALL = 2001;
	//  无效参数
	int ERROR_ARGS = 3001;
	//  分享对象为空
	int ERROR_NULL_SHARE_TARGET = 4001;
	//  分享参数为空
	int ERROR_NULL_SHARE_PARAMS = 5001;
	//  平台分享失败
	int ERROR_SHARE = 6001;
	//  平台分享被拒绝
	int ERROR_SHARE_DENIED = 6004;
	//  平台分享不支持
	int ERROR_SHARE_UNSUPPORT = 6005;
	//  平台分享被禁止
	int ERROR_SHARE_BAN = 6006;
	//  授权失败
	int ERROR_AUTH = 7001;
}

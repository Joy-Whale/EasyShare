package cn.joy.libs.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

/**
 * User: JiYu
 * Date: 2016/7/25 0025
 * Time: 11:03
 */

public class ShareImageUtils {

	// 图片压缩宽高比
	private static final float COMPRESS_CROP_SCALE_DEFAULT = 1.5f;

	// 压缩图片的常量值
	// 宽高最大限制
	private static final int COMPRESS_IMAGE_SIDE_DEFAULT = Integer.MAX_VALUE;
	// 字节大小最大限制（单位beta）
	private static final int COMPRESS_IMAGE_SIZE_DEFAULT = 512 * 1024;

	// 缩略图的常量值
	// 宽高最大限制
	private static final int COMPRESS_THUMB_IMAGE_SIDE_DEFAULT = 500;
	// 字节大小最大限制（单位beta）
	private static final int COMPRESS_THUMB_IMAGE_SIZE_DEFAULT = 32 * 1024;

	// 图片的默认最大宽高
	@Deprecated
	private static final int THUMB_RESOLUTION_SIZE = 1080;
	@Deprecated
	private static final int THUMB_MAX_SIZE = 32 * 1024;
	@Deprecated
	private static final int BITMAP_SAVE_THRESHOLD = 32 * 1024;


	/**
	 * 根据原图数据获取缩略图资源
	 * @param source 原图数据
	 */
	public static byte[] getThumbCompressData(byte[] source) {
		return getThumbCompressData(source, COMPRESS_THUMB_IMAGE_SIZE_DEFAULT);
	}

	public static Bitmap getThumbCompressBitmap(byte[] source){
		byte[] bytes = getThumbCompressData(source);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 根据原图数据获取缩略图资源
	 * @param source 原图数据
	 */
	public static byte[] getThumbCompressData(byte[] source, int maxSize) {
		return getBitmapCompressData(BitmapFactory.decodeByteArray(source, 0, source.length), maxSize, COMPRESS_THUMB_IMAGE_SIDE_DEFAULT, COMPRESS_THUMB_IMAGE_SIDE_DEFAULT, COMPRESS_CROP_SCALE_DEFAULT, false);
	}

	/**
	 * 获取缩略图压缩图片
	 * @param image image
	 */
	public static byte[] getThumbCompressData(ShareParams.ShareImage image) {
		return getThumbCompressData(image, COMPRESS_THUMB_IMAGE_SIZE_DEFAULT);
	}

	/**
	 * 获取缩略图压缩图片
	 * @param image   image
	 * @param maxSize 最大尺寸
	 */
	public static byte[] getThumbCompressData(ShareParams.ShareImage image, int maxSize) {
		return getCompressData(image, maxSize, COMPRESS_THUMB_IMAGE_SIDE_DEFAULT, COMPRESS_THUMB_IMAGE_SIDE_DEFAULT, COMPRESS_CROP_SCALE_DEFAULT, false);
	}

	/**
	 * 获取正常的压缩数据
	 * @param image 图片资源
	 */
	public static byte[] getCompressData(ShareParams.ShareImage image) {
		return getCompressData(image, COMPRESS_IMAGE_SIZE_DEFAULT);
	}

	/**
	 * 获取正常的压缩数据
	 * @param image   图片资源
	 * @param maxSize 图片最大尺寸
	 */
	public static byte[] getCompressData(ShareParams.ShareImage image, int maxSize) {
		return getCompressData(image, maxSize, COMPRESS_IMAGE_SIDE_DEFAULT, COMPRESS_IMAGE_SIDE_DEFAULT, 0, false);
	}

	/**
	 * 将图片资源转换为byte数组
	 * @param image     图片
	 * @param maxSize   最大限制
	 * @param widthMax  最大宽度
	 * @param heightMax 最大高度
	 * @param cropScale 图片裁剪宽高比，0为不裁剪
	 * @param isFixSize 是否修复大小
	 */
	public static byte[] getCompressData(ShareParams.ShareImage image, int maxSize, int widthMax, int heightMax, float cropScale, boolean isFixSize) {
		return getBitmapCompressData(decodeBitmap(image), maxSize, widthMax, heightMax, cropScale, isFixSize);
	}

	/**
	 * 将bitmap转换为byte数组
	 * @param bmp       bitmap
	 * @param maxSize   最大限制
	 * @param widthMax  最大宽度
	 * @param heightMax 最大高度
	 * @param cropScale 图片裁剪宽高比，0为不裁剪
	 * @param isFixSize 是否修复大小
	 */
	private static byte[] getBitmapCompressData(Bitmap bmp, int maxSize, int widthMax, int heightMax, float cropScale, boolean isFixSize) {
		if (bmp == null)
			return new byte[0];
		if (!bmp.isRecycled()) {
			if (!isFixSize) {
				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();
				// 如果当前设置的最大宽高大于图片本身的宽高，则以图片本身的宽高来加载图片
				if (widthMax > bmpWidth) {
					widthMax = bmpWidth;
				}
				if (heightMax > bmpHeight) {
					heightMax = bmpHeight;
				}
				double scale = getScale(widthMax, heightMax, bmpWidth, bmpHeight);
				widthMax = (int) (bmpWidth / scale);
				heightMax = (int) (bmpHeight / scale);
			}
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, widthMax, heightMax, true);
			if (thumbBmp != bmp) {
				bmp.recycle();
			}
			if (cropScale > 0) {
				boolean changed = false;
				if (widthMax >= heightMax && (float) widthMax / heightMax > cropScale) {
					widthMax = (int) (heightMax * cropScale);
					changed = true;
				} else if (heightMax >= widthMax && (float) heightMax / widthMax > cropScale) {
					heightMax = (int) (widthMax * cropScale);
					changed = true;
				}
				if (changed) {
					Bitmap tmp = Bitmap.createBitmap(thumbBmp, 0, 0, widthMax, heightMax);
					if (thumbBmp != tmp) {
						thumbBmp.recycle();
						thumbBmp = tmp;
					}
				}
			}
			byte[] tempArr = compressBitmapToByteArray(thumbBmp, maxSize, true);
			return tempArr == null ? new byte[0] : tempArr;
		}
		return new byte[0];
	}

	/**
	 * 获取bitmap压缩数据
	 * @param bmp       bmp
	 * @param maxSize   maxSize
	 * @param widthMax  widthMax
	 * @param heightMax heightMax
	 * @param cropScale cropScale
	 * @param isFixSize isFixSize
	 */
	public static Bitmap getCompressBitmap(Bitmap bmp, int maxSize, int widthMax, int heightMax, float cropScale, boolean isFixSize) {
		byte[] bytes = getBitmapCompressData(bmp, maxSize, widthMax, heightMax, cropScale, isFixSize);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 获取bitmap压缩缩略Bitmap
	 * @param bmp 原bitmap
	 */
	public static Bitmap getThumbCompressBitmap(Bitmap bmp) {
		return getCompressBitmap(bmp, THUMB_MAX_SIZE, COMPRESS_IMAGE_SIDE_DEFAULT, COMPRESS_IMAGE_SIDE_DEFAULT, 0, false);
	}

	public static Bitmap getThumbCompressBitmap(String filePath) {
		return getThumbCompressBitmap(BitmapFactory.decodeFile(filePath));
	}

	/**
	 * 根据图片url等转化为bitmap
	 * @param image 图片bean
	 */
	private static Bitmap decodeBitmap(final ShareParams.ShareImage image) {
		Bitmap bmp = null;
		switch (image.getSource()) {
			case File:
				bmp = BitmapFactory.decodeFile(image.getImageFile().getAbsolutePath());
				//	bmp = decodeFile(image.getImageFile().getAbsolutePath(), THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE);
				break;
			case Http:
				bmp = decodeUrl(image.getImageUrl());
				break;
		}
		return bmp;
	}

	/**
	 * 将bitmap压缩然后转换为字节码
	 * @param bmp         bitmap
	 * @param maxSize     字节最大限制
	 * @param needRecycle 压缩完后是否需要回收
	 * @return 压缩然后的bitmap字节码
	 */
	private static byte[] compressBitmapToByteArray(final Bitmap bmp, int maxSize, final boolean needRecycle) {
		if (bmp == null) {
			return null;
		}
		final ByteArrayOutputStream bao = new ByteArrayOutputStream();
		int options = 100;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, bao);
		int outputLength = bao.size();
		while (outputLength > maxSize && options >= 0) {
			if (outputLength > 10 * maxSize) {
				options -= 30;
			} else if (outputLength > 5 * maxSize) {
				options -= 20;
			} else {
				options -= 10;
			}
			if (options < 0) {
				options = 0;
			}
			bao.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, options, bao);
			outputLength = bao.size();
			if (options == 0) {
				break;
			}
		}

		if (needRecycle) {
			bmp.recycle();
		}

		final byte[] result = bao.toByteArray();
		closeIo(bao);

		return result;
	}

	public static Bitmap decodeUrl(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl)) {
			return null;
		}

		InputStream is = null;
		try {
			is = new URL(imageUrl).openStream();
			if (is.available() < 50 * 1024) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// 设置显示效果为rbg565
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				// 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
				opts.inPurgeable = true;
				return BitmapFactory.decodeStream(is, null, opts);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIo(is);
		}
		return null;
	}

	public static Bitmap decodeFile(String path, float width, float height) {
		int inSampleSize = getInSampleSize(path, width, height);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = inSampleSize;
		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(path, newOpts);
	}

	private static int getInSampleSize(String path, float width, float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, newOpts);
		int outWidth = newOpts.outWidth;
		int outHeight = newOpts.outHeight;
		return (int) getScale(width, height, outWidth, outHeight);
	}

	public static double getScale(float targetWidth, float targetHeight, float bmpWidth, float bmpHeight) {
		double be;
		if (bmpWidth >= bmpHeight) {
			float widthScale = bmpWidth / targetHeight;
			float heightScale = bmpHeight / targetWidth;
			if (widthScale >= heightScale) {
				be = Math.rint(widthScale);
			} else {
				be = Math.rint(heightScale);
			}
		} else {
			float widthScale = bmpWidth / targetWidth;
			float heightScale = bmpHeight / targetHeight;
			if (widthScale >= heightScale) {
				be = widthScale;
			} else {
				be = heightScale;
			}
		}
		if (be <= 0) {
			return 1.0;
		}

		return be;
	}

	public static File saveBitmapToExternal(Bitmap bitmap, String targetFileDirPath) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		File targetFileDir = new File(targetFileDirPath);
		if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
			return null;
		}

		File targetFile = new File(targetFileDir, UUID.randomUUID().toString());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			baos.writeTo(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return targetFile;
	}

	private static void closeIo(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void closeIo(OutputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

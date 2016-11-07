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

	// 图片的默认最大宽高
	private static final int THUMB_RESOLUTION_SIZE = 1080;
	private static final int THUMB_MAX_SIZE = 30 * 1024;
	private static final int BITMAP_SAVE_THRESHOLD = 32 * 1024;

	/**
	 * 缩略图。32kb限制。
	 * 注意：在工作线程调用。
	 * @param image image
	 */
	public static byte[] getThumbData(final ShareParams.ShareImage image) {
		return getThumbData(image, THUMB_MAX_SIZE, THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE, false);
	}

	/**
	 * 缩略图
	 * 注意：在工作线程调用。
	 * @param image   image
	 * @param maxSize 图片最大限制
	 */
	public static byte[] getThumbData(ShareParams.ShareImage image, int maxSize) {
		return getThumbData(image, maxSize, THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE, false);
	}

	public static byte[] getThumbData(final ShareParams.ShareImage image, int maxSize, int widthMax, int heightMax, boolean isFixSize) {
		if (image == null) {
			return new byte[0];
		}

		boolean isRecycleSrcBitmap = true;
		Bitmap bmp = null;

		switch (image.getSource()) {
			case File:
				bmp = decodeFile(image.getImageFile().getAbsolutePath(), THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE);
				break;
			//			case Resource:
			//				break;
			case Http:
				bmp = decodeUrl(image.getImageUrl());
				break;
		}

		if (bmp != null && !bmp.isRecycled()) {
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

			final Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, widthMax, heightMax, true);
			if (thumbBmp != bmp) {
				bmp.recycle();
			}
			byte[] tempArr = bmpToByteArray(thumbBmp, maxSize, true);
			return tempArr == null ? new byte[0] : tempArr;
		}

		return new byte[0];
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, int maxSize, final boolean needRecycle) {
		if (bmp == null) {
			return null;
		}

		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		int options = 100;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, output);
		int outputLength = output.size();
		while (outputLength > maxSize) {
			if (outputLength > 10 * maxSize) {
				options -= 30;
			} else if (outputLength > 5 * maxSize) {
				options -= 20;
			} else {
				options -= 10;
			}

			output.reset();
			bmp.compress(Bitmap.CompressFormat.JPEG, options, output);
			outputLength = output.size();
		}

		if (needRecycle) {
			bmp.recycle();
		}

		final byte[] result = output.toByteArray();
		closeIo(output);

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
				return BitmapFactory.decodeStream(is);
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

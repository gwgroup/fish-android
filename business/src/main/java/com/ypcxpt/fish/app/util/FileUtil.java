package com.ypcxpt.fish.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.ypcxpt.fish.App;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SdCardPath")
public class FileUtil {

	public static File saveBitmap(Context context,String picName, Bitmap bm) {
		File f = new File("/sdcard/DCIM/Camera/", picName);
		if (f.exists()) {
			f.delete();
		} else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,	Uri.fromFile(new File(f.getPath()))));
	    
		return f;
	}
	
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Uri uri,Context context) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = ((Activity) context).managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
//	public static String uploadFile(File imageFile,final Context context) {
//		final String URL[]={null};
//		RequestParams reParams = new RequestParams();
//		reParams.addBodyParameter("file", imageFile, "image/JPEG");
//		new APPRestClient().getHttpClient().send(HttpMethod.POST, RequestUrlConsts.UPLOAD_IMG, reParams,
//				new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//					}
//
//					@Override
//					public void onLoading(long total, long current,
//							boolean isUploading) {
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						if (arg0.result.contains("err_code")) {
//							JsonObject jsonObject = new JsonParser().parse(
//									arg0.result).getAsJsonObject();
//
//							if (jsonObject.has("err_code")) {
//								JsonElement errorCodeJsonElement = jsonObject
//										.get("err_code");
//								if (errorCodeJsonElement.getAsString().equals(
//										"0")) {
//								}
//							}
//							if (jsonObject.has("err_msg")) {
//								String uploadFinishUrl = jsonObject.get("err_msg")
//										.toString();
//								URL[0]=uploadFinishUrl;
//								ToastTools.showShort(context, "上传成功");
//							}
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//				});
//		return URL[0];
//	}




	// 处理图片 大图片压缩上传 服务端
	public static File compressImage(String filePath) {
		Bitmap bitmap = getSmallBitmap(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		byte[] b = baos.toByteArray();
		return getFileFromBytes(b, filePath);
	}

	/**
	 * 把字节数组保存为一个文件
	 *
	 * @param b
	 * @param urlpath
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String urlpath) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(urlpath);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);//这个值应该动态配的 嫌麻烦我写死的

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(App.getInstance(), contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 时间转换
	 * @param time
	 * @return
	 */
	public static String exchangeTime(String time) {
	    String updateTime = "";
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            Date date = format.parse(time);
            updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
            return updateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return updateTime;
	}

	// 两次点击按钮之间的点击间隔不能少于1000毫秒
	private static final int MIN_CLICK_DELAY_TIME = 1000;
	private static long lastClickTime;
	public static boolean isFastClick() {
		boolean flag = false;
		long curClickTime = System.currentTimeMillis();
		if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
			flag = true;
		}
		lastClickTime = curClickTime;
		return flag;
	}
}

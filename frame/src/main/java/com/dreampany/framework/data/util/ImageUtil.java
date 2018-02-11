package com.dreampany.framework.data.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.common.util.UriUtil;

import java.io.File;
import java.io.FileOutputStream;

import cn.trinea.android.common.util.ImageUtils;

/**
 * Created by nuc on 1/1/2017.
 */

public final class ImageUtil {
    private ImageUtil() {
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            return icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getAppIconByUri(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(uri, 0);
        if (pi == null) {
            return null;
        }
        ApplicationInfo appInfo = pi.applicationInfo;

        appInfo.sourceDir = uri;
        appInfo.publicSourceDir = uri;

        return appInfo.loadIcon(pm);
    }


    public static Bitmap getImageThumbnail(String path, int mMaxWidth, int mMaxHeight) {
        Bitmap.Config mDecodeConfig = Bitmap.Config.RGB_565;
        ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_CROP;

        File bitmapFile = new File(path);
        Bitmap bitmap = null;

        if (!bitmapFile.exists() || !bitmapFile.isFile()) {
            return bitmap;
        }

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inInputShareable = true;
        decodeOptions.inPurgeable = true;
        decodeOptions.inPreferredConfig = mDecodeConfig;
        if (mMaxWidth == 0 && mMaxHeight == 0) {

            bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), decodeOptions);
        } else {
            // If we have to resize this image, first get the natural bounds.
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            // Then compute the dimensions we would ideally like to decode to.
            int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                    actualWidth, actualHeight, mScaleType);
            int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                    actualHeight, actualWidth, mScaleType);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), decodeOptions);
            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
                        desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }

        }
        return bitmap;
    }

    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    public static Drawable getVideoIcon(Context context, String thumbUri) {
        /*try {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            return icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }*/

        return null;
    }

    public static Uri getUri(String path, int defaultResource) {
        Uri.Builder builder = new Uri.Builder();

        if (path.startsWith(UriUtil.LOCAL_FILE_SCHEME)) {
            builder.scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(path);
        } else if (path.startsWith(UriUtil.HTTP_SCHEME)) {
            builder.scheme(UriUtil.HTTP_SCHEME)
                    .path(path);
        } else if (path.startsWith(UriUtil.HTTPS_SCHEME)) {
            builder.scheme(UriUtil.HTTPS_SCHEME)
                    .path(path);
        } else {
            builder.scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(defaultResource));
        }
        return builder.build();
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                           int actualSecondary, ImageView.ScaleType scaleType) {
        // If no dominant value at all, just return the actual.
        if ((maxPrimary == 0) && (maxSecondary == 0)) {
            return actualPrimary;
        }
        // If ScaleType.FIT_XY fill the whole rectangle, ignore ratio.
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            if (maxPrimary == 0) {
                return actualPrimary;
            }
            return maxPrimary;
        }
        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }
        if (maxSecondary == 0) {
            return maxPrimary;
        }
        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        // If ScaleType.CENTER_CROP fill the whole rectangle, preserve aspect ratio.
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if ((resized * ratio) < maxSecondary) {
                resized = (int) (maxSecondary / ratio);
            }
            return resized;
        }
        if ((resized * ratio) > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

/*    public static String saveImage(String directory, String fileName, Bitmap bitmap) {
        File file = new File(directory, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            LogKit.error("save image error: " + e.toString());
        }
        return null;
    }*/
}

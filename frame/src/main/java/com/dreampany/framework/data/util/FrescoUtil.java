package com.dreampany.framework.data.util;

import android.net.Uri;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoUtil {

    public static void loadResource(SimpleDraweeView view, int resourceId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(resourceId))
                .build();
        view.setImageURI(uri);
    }

    public static void loadImage(SimpleDraweeView view, String imageUri, int size) {
        loadImage(view, imageUri, size, size);
    }

    public static void loadImage(SimpleDraweeView view, String imageUri, int width, int height) {
        if (imageUri == null) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        view.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(view.getController())
                        .setImageRequest(request)
                        .build());
    }

    public static void loadImage(SimpleDraweeView view, String imageUri) {
        if (imageUri == null) {
            return;
        }
        view.setImageURI(imageUri);
    }

   /* public static final File CACHE_DIR = BaseApp.getContext().getExternalCacheDir();

    public static final String IMAGE_CACHE_DIR = CACHE_DIR != null ? CACHE_DIR.getAbsolutePath() : App.getContext().getCacheDir().getAbsolutePath();

    public static void savePicture(String picUrl, Context context, String fileName) {
        File picDir = new File(IMAGE_CACHE_DIR);
        if (!picDir.exists()) {
            picDir.mkdir();
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getContext().getEncodedCacheKey(ImageRequest.fromUri(picUrl),context);
        File cacheFile = getCacheImageOnDisk(cacheKey);
        if (cacheFile == null) {
            downloadImage(Uri.parse(picUrl), fileName, context);
            Log.d("frescoUtil","download successful");
        } else {
            copyTo(cacheFile, picDir, fileName);
            Log.d("frescoUtil","copy successful");
        }
    }

    public static File getCacheImageOnDisk(CacheKey cacheKey) {
        Logger.d(IMAGE_CACHE_DIR);
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getContext().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getContext().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getContext().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getContext().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    public static boolean copyTo(File src, File dir, String fileName) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();
            File dst = new File(dir, fileName + ".jpg");
            fo = new FileOutputStream(dst);
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void downloadImage(Uri uri, final String fileName, Context context) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    Log.d("FrescoUtil", "save image failed");
                }
                File appDir = new File(IMAGE_CACHE_DIR);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                File file = new File(appDir, fileName + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        }, CallerThreadExecutor.getContext());
    }*/


}
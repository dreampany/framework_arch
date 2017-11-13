package com.dreampany.framework.data.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dreampany.framework.data.manager.PermissionManager;
import com.dreampany.framework.data.model.Task;
import com.google.common.hash.Hashing;
import com.jaredrummler.android.device.DeviceName;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class AndroidUtil {
    private static final String TAG = AndroidUtil.class.getSimpleName();
    private static boolean debug = true;

    private AndroidUtil() {
    }

    public static void setDebug(Context context) {
        AndroidUtil.debug = isDebug(context);
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return PermissionManager.isPermissionGranted(context, permission);
    }

    public static void requestPermission(Activity activity, String permission, PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(permissionListener)
                .check();
    }

    public static boolean isDebug() {
        return debug;
        //return false;
    }

    public static boolean isDebug(Context context) {
        boolean debuggable = false;

        Context appContext = context.getApplicationContext();
        PackageManager pm = appContext.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(appContext.getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /*debuggable variable will remain false*/
        }

        return debuggable;
    }


    public static boolean needThread(Thread thread) {
        return (thread == null || !thread.isAlive());
    }

    public static Thread createThread(Runnable runnable) {
        return new Thread(runnable);
    }

    public static Thread createThread(Runnable runnable, String name) {
        Thread thread = new Thread(runnable);
        thread.setName(name);
        thread.setDaemon(true);
        return thread;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

/*    public static String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }*/

    public static long getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);//InstanceID.getInstance(context).getId();
        long deviceId = Hashing.sha256().newHasher().putUnencodedChars(androidId).hash().asLong();

        return Math.abs(deviceId);
    }

    public static long getPackageId(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        return DataUtil.getSha256(packageName);
    }

    public static String getDeviceName() {
        return DeviceName.getDeviceName();
    }

    public static boolean isAndroid() {
        return System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik");
    }


    public static void log(String unit, String message) {
        if (isDebug()) {
            if (isAndroid()) {
                Log.d(unit, message);
            } else {
                System.out.println(unit + ": " + message);
            }
        }
    }

    public static void logE(String unit, String message) {
        if (isAndroid()) {
            Log.d(unit, "FATAL ERROR: " + message);
        } else {
            System.out.println(unit + ": FATAL ERROR: " + message);
        }

        throw new RuntimeException(unit + ": FATAL ERROR: " + message);
    }

    public static byte byteOfInt(int value, int which) {
        int shift = which * 8;
        return (byte) (value >> shift);
    }

    public static InetAddress intToInet(int value) {
        byte[] bytes = new byte[4];

        for (int e = 0; e < 4; ++e) {
            bytes[e] = byteOfInt(value, e);
        }

        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException var3) {
            return null;
        }
    }

    public static Object invokeQuietly(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var4) {
            Log.e("WM", "", var4);
            return null;
        }
    }

    public static <T extends Activity> void openActivity(Activity activity, Class<T> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    public static <T extends Activity> void openDestroyActivity(Activity activity, Class<T> clazz) {
        activity.startActivity(new Intent(activity, clazz));
        activity.finish();
    }

    public static <T extends Activity> void openActivity(Fragment fragment, Class<T> clazz) {
        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), clazz));
    }

    public static <T extends Activity> void openDestroyActivity(Fragment fragment, Class<T> clazz) {
        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), clazz));
        fragment.getActivity().finish();
    }

    public static <T extends Activity> void openActivity(Activity activity, Class<T> clazz, Task task) {
        Intent intent = new Intent(activity, clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        activity.startActivity(intent);
    }

    public static <T extends Activity> void openActivityForResult(Activity activity, Class<T> clazz, int requestCode) {
        openActivityForResult(activity, clazz, requestCode, null);
    }

    public static <T extends Activity> void openActivityForResult(Activity activity, Class<T> clazz, int requestCode, Task task) {
        Intent intent = new Intent(activity, clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static <T extends Activity> void openActivityForResult(Activity activity, Fragment fragment, Class<T> clazz, int requestCode) {
       openActivityForResult(activity, fragment, clazz, requestCode, null);
    }

    public static <T extends Activity> void openActivityForResult(Activity activity, Fragment fragment, Class<T> clazz, int requestCode, Task task) {
        Intent intent = new Intent(activity, clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        fragment.startActivityForResult(intent, requestCode);
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package verbose
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }
    //Build information
/*    public static boolean isDebug(BuildConfig buildConfig) {
        return BuildConfig.DEBUG;
    }*/

    public static float dp2px(final Context context, final float dpValue) {
        return dpValue * context.getResources().getDisplayMetrics().density;
    }

    public static void hideKeyboard(Activity activity) {
       /* ((InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);*/

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Is the screen of the device on.
     *
     * @param context the getContext
     * @return true when (at least one) screen is on
     */
    public static boolean isScreenOn(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }

    public static String getApplicationId(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.packageName;
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return null;
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException nameException) {
            return null;
        }
    }

    // Android System Queue
    private static Handler handler;
    private static Handler nonUiHandler = new Handler(Looper.myLooper());

    public static Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    private static final long defaultDelay = 250L;

    public static void removeRunnable(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static void post(Runnable runnable, long delay) {
        getHandler().postDelayed(runnable, delay);
    }

    public static void postDelay(Runnable runnable) {
        post(runnable, defaultDelay);
    }

    public static void post(Runnable runnable) {
        getHandler().post(runnable);
    }

    public static void postNonUi(Runnable runnable, long timeout) {
        nonUiHandler.postDelayed(runnable, timeout);
    }

    // Checking and Getting
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNull(View view) {
        return view == null;
    }

    public static boolean isNull(Context context) {
        return context == null;
    }

    public static View getViewById(View parentView, int viewId) {
        if (!isNull(parentView)) {
            return parentView.findViewById(viewId);
        }
        return null;
    }

    public static ViewPager getViewPager(View parentView, int viewPagerId) {
        View viewPager = getViewById(parentView, viewPagerId);
        if (ViewPager.class.isInstance(viewPager)) {
            return (ViewPager) viewPager;
        }
        return null;
    }

    public static TabLayout getTabLayout(View parentView, int tabLayoutId) {
        View tabLayout = getViewById(parentView, tabLayoutId);
        if (TabLayout.class.isInstance(tabLayout)) {
            return (TabLayout) tabLayout;
        }
        return null;
    }

    private static TextToSpeech textToSpeech;

    public static void initTextToSpeech(Context context) {
        if (textToSpeech == null) {
            try {
                textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.ENGLISH);
                        }
                    }
                });
            } catch (IllegalArgumentException e) {
                LogKit.error("Error in TTS " + e.toString());
            }
        }
    }

    public static void speak(String text) {
        if (textToSpeech != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }


    public static void stopTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }


    public static void openVideo(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setDataAndType(Uri.parse(uri), "video/mp4");
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean startService(Context context, Class<?> serviceClass) {
        if (AndroidUtil.isServiceRunning(context, serviceClass)) {
            return false;
        }

        Intent intent = new Intent(context, serviceClass);
        context.startService(intent);
        return true;
    }

    public static boolean stopService(Context context, Class<?> serviceClass) {
        return context.stopService(new Intent(context, serviceClass));
    }

    public static boolean bindService(Context context, Class<?> serviceClass, ServiceConnection serviceConnection) {
        return context.bindService(new Intent(context, serviceClass), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public static boolean unbindService(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        return true;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasFeature(Context context, String feature) {
        return context.getApplicationContext().getPackageManager().hasSystemFeature(feature);
    }

    public static BluetoothManager getBluetoothManager(Context context) {
        return (BluetoothManager) context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private static HandlerThread handlerThread;
    private static Handler backgroundHandler;

    public static Handler getBackgroundHandler() {
        if (handlerThread == null) {
            handlerThread = new HandlerThread("background-thread");
            handlerThread.start();
            backgroundHandler = new Handler(handlerThread.getLooper());
        }

        return backgroundHandler;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static int getTargetSDKVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException nnf) {
            return -1;
        }
    }

/*    public static boolean isShareServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (SHAREthemService.class.getCanonicalName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasOverlayPermission(Context context) {
        return Settings.canDrawOverlays(context.getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasWriteSettingsPermission(Context context) {
        return Settings.System.canWrite(context.getApplicationContext());
    }

    public static boolean checkOverlayPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!hasOverlayPermission(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkWriteSettingsPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!hasWriteSettingsPermission(activity.getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static List<String> getStrings(Context context, int resourceId) {
        Resources resources = context.getResources();
        String[] items = resources.getStringArray(resourceId);
        return Arrays.asList(items);
    }

    public static int getScreenWidthInDp(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.widthPixels / dm.density);
    }

    public static int getScreenWidthInPx(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}

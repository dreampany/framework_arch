package com.dreampany.framework.data.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.content.res.TypedArray;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dreampany.framework.R;
import com.dreampany.framework.data.manager.PermissionManager;
import com.dreampany.framework.data.model.BaseParcel;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.ui.activity.BaseActivity;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import eu.davidea.flexibleadapter.utils.FlexibleUtils;

public final class AndroidUtil {
    private static final String TAG = AndroidUtil.class.getSimpleName();
    private static boolean debug = true;
    public static final String DATE_TIME = "dd MMM yyyy HH:mm:ss z";
    private static int colorPrimary = -1;
    private static int colorPrimaryDark = -1;
    private static int colorAccent = -1;

    private AndroidUtil() {
    }

    public static void setDebug(Context context) {
        AndroidUtil.debug = isDebug(context);
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
        String androidId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);//InstanceID.getContext(context).getId();
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

    public static String getUuid() {
        return UUID.randomUUID().toString();
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

    public static void openActivity(Activity activity, Class<?> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    public static void openDestroyActivity(Activity activity, Class<?> clazz) {
        activity.startActivity(new Intent(activity, clazz));
        activity.finish();
    }

    public static void openActivity(Fragment fragment, Class<?> clazz) {
        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), clazz));
    }

    public static void openDestroyActivity(Fragment fragment, Class<?> clazz) {
        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), clazz));
        fragment.getActivity().finish();
    }

    public static void openActivity(Fragment fragment, Class<?> clazz, Task task) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        fragment.startActivity(intent);
    }

    public static void openActivity(Activity activity, Class<?> clazz, Task task) {
        Intent intent = new Intent(activity, clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        activity.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity, Class<?> clazz, int requestCode) {
        openActivityForResult(activity, clazz, requestCode, null);
    }

    public static void openActivityForResult(Activity activity, Class<?> clazz, int requestCode, Task task) {
        Intent intent = new Intent(activity, clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openActivityForResult(Fragment fragment, Class<?> clazz, int requestCode) {
        openActivityForResult(fragment, clazz, requestCode, null);
    }

    public static void openActivityForResult(Fragment fragment, Class<?> clazz, int requestCode, Task task) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        if (task != null) {
            intent.putExtra(Task.class.getName(), task);
        }
        fragment.startActivityForResult(intent, requestCode);
    }


/*    public static String printKeyHash(Activity context) {
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
                MessageDigest md = MessageDigest.getContext("SHA");
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
    }*/

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
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        Activity activity = fragment.getActivity();
        hideKeyboard(activity);
    }

    public static void showKeyboard(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
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

    private static TextToSpeech textToSpeech;

    public static void initTextToSpeech(Context context) {
        if (textToSpeech == null) {
            try {
                textToSpeech = new TextToSpeech(context.getApplicationContext(), status -> {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    }
                });
            } catch (IllegalArgumentException e) {
                LogKit.error("Error in TTS " + e.toString());
            }
        }
    }

    public static void speak(String text) {
        if (textToSpeech != null && text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public static void silentSpeak() {
        if (textToSpeech != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null);
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

    public static void share(Fragment fragment, String subject, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        fragment.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static void share(Activity activity, String subject, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static Point getScreenDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        Point point = new Point();
        point.set(dm.widthPixels, dm.heightPixels);
        return point;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int dpToPx(Context context, float dp) {
        return Math.round(dp * getDisplayMetrics(context).density);
    }

    /**
     * dd MMM yyyy HH:mm:ss z
     *
     * @return The date formatted.
     */
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * API 11
     *
     * @see VERSION_CODES#HONEYCOMB
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * API 14
     *
     * @see VERSION_CODES#ICE_CREAM_SANDWICH
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * API 16
     *
     * @see VERSION_CODES#JELLY_BEAN
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * API 19
     *
     * @see VERSION_CODES#KITKAT
     */
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * API 21
     *
     * @see VERSION_CODES#LOLLIPOP
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * API 23
     *
     * @see VERSION_CODES#M
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * API 24
     *
     * @see VERSION_CODES#N
     */
    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

/*    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "v" + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return context.getString(android.R.string.unknownName);
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }*/

    /**
     * Adjusts the alpha of a color.
     *
     * @param color the color
     * @param alpha the alpha value we want to set 0-255
     * @return the adjusted color
     */
    public static int adjustAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return (alpha << 24) | (color & 0x00ffffff);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorPrimary(Context context) {
        if (colorPrimary < 0) {
            int primaryAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorPrimary : R.attr.colorPrimary;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{primaryAttr});
            colorPrimary = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorPrimary;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorPrimaryDark(Context context) {
        if (colorPrimaryDark < 0) {
            int primaryDarkAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorPrimaryDark : R.attr.colorPrimaryDark;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{primaryDarkAttr});
            colorPrimaryDark = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorPrimaryDark;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getColorAccent(Context context) {
        if (colorAccent < 0) {
            int accentAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorAccent : R.attr.colorAccent;
            TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{accentAttr});
            colorAccent = androidAttr.getColor(0, 0xFFFFFFFF); //Default: material_deep_teal_500
            androidAttr.recycle();
        }
        return colorAccent;
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtmlCompat(String text) {
        if (hasNougat()) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressWarnings("deprecation")
    public static void textAppearanceCompat(TextView textView, int resId) {
        if (hasMarshmallow()) {
            textView.setTextAppearance(resId);
        } else {
            textView.setTextAppearance(textView.getContext(), resId);
        }
    }

    /**
     * Show Soft Keyboard with new Thread
     *
     * @param activity
     */
    public static void hideSoftInput(final Activity activity) {
        if (activity.getCurrentFocus() != null) {
            new Runnable() {
                public void run() {
                    InputMethodManager imm =
                            (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }.run();
        }
    }

    /**
     * Hide Soft Keyboard from Dialogs with new Thread
     *
     * @param context
     * @param view
     */
    public static void hideSoftInputFrom(final Context context, final View view) {
        new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }.run();
    }

    /**
     * Show Soft Keyboard with new Thread
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(final Context context, final View view) {
        new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }.run();
    }

    /**
     * Create the reveal effect animation
     *
     * @param view the View to reveal
     * @param cx   coordinate X
     * @param cy   coordinate Y
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void reveal(final View view, int cx, int cy) {
        if (!hasLollipop()) {
            view.setVisibility(View.VISIBLE);
            return;
        }

        //Get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        //Create the animator for this view (the start radius is zero)
        Animator animator =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        //Make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        animator.start();
    }

    /**
     * Create the un-reveal effect animation
     *
     * @param view the View to hide
     * @param cx   coordinate X
     * @param cy   coordinate Y
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void unReveal(final View view, int cx, int cy) {
        if (!hasLollipop()) {
            view.setVisibility(View.GONE);
            return;
        }

        //Get the initial radius for the clipping circle
        int initialRadius = view.getWidth();

        //Create the animation (the final radius is zero)
        Animator animator =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        //Make the view invisible when the animation is done
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });

        //Start the animation
        animator.start();
    }


    public static void rateUs(Activity activity) {
        String id = getApplicationId(activity);
        Uri uri = Uri.parse("market://details?id=" + id);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(goToMarket);
    }

}

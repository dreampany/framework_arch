package com.dreampany.framework.data.manager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.dreampany.framework.data.util.AndroidUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nuc on 12/10/2016.
 */

public final class PermissionManager {

    private PermissionManager() {
    }

    private static final List<String> profilePermissions = Arrays.asList(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
    );
    private static final int MAX_PERMISSION_LABEL_LENGTH = 20;

    static List<String> getProfilePermissions(Context context) {
        return profilePermissions;
    }

    private static List<PermissionInfo> getPermissions(Context context) {

        List<PermissionInfo> permissionInfoList = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        for (String permission : getProfilePermissions(context)) {
            PermissionInfo pinfo = null;
            try {
                pinfo = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            permissionInfoList.add(pinfo);
        }
        return permissionInfoList;
    }

    private static CharSequence[] getPermissionNames(Context context) {
        PackageManager pm = context.getPackageManager();
        CharSequence[] names = new CharSequence[getPermissions(context).size()];
        int i = 0;
        for (PermissionInfo permissionInfo : getPermissions(context)) {
            CharSequence label = permissionInfo.loadLabel(pm);
            if (label.length() > MAX_PERMISSION_LABEL_LENGTH) {
                label = label.subSequence(0, MAX_PERMISSION_LABEL_LENGTH);
            }
            names[i] = label;
            i++;
        }
        return names;
    }

    private static boolean[] getPermissionsState(Context context) {
        boolean[] states = new boolean[getProfilePermissions(context).size()];
        int i = 0;
        for (String permission : getProfilePermissions(context)) {
            states[i] = isPermissionGranted(context, permission);
            i++;
        }
        return states;
    }


    public static void showContactPermission(final Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMultiChoiceItems(getPermissionNames(context),
                getPermissionsState(context),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        ActivityCompat.requestPermissions(scanForActivity(context),
                                new String[]{getProfilePermissions(context).get(which)}, 23);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasLocationPermissionGranted(Context context) {
        return PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean areExplicitPermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void showContactPermission(final Context context) {
        showContactPermission(context, null);
    }

    public static boolean isPermissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isRecordAudioPermission(String permission) {
        return Manifest.permission.RECORD_AUDIO.equals(permission);
    }

    public static boolean isCameraPermission(String permission) {
        return Manifest.permission.CAMERA.equals(permission);
    }

    public static boolean isPermitted(Context context, String... permissions) {
        for (String permission : permissions) {
            if (AndroidUtil.hasMarshmallow()) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            if (!hasPermissionInManifest(context, permission)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasPermissionInManifest(
            @NonNull Context context, @NonNull String permissionName) {
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equalsIgnoreCase(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

}

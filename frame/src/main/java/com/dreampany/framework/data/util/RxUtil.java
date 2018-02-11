package com.dreampany.framework.data.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by air on 9/12/17.
 */

public class RxUtil {
    public static void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }
}

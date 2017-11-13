package com.dreampany.framework.data.manager;

/**
 * Created by air on 10/23/17.
 */

public final class NotifyManager extends EventManager {




    public NotifyManager() {

    }

    @Override
    protected boolean looping() throws InterruptedException {
        return false;
    }
}

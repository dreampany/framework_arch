package com.dreampany.framework.data.manager;

/**
 * Created by nuc on 5/25/2017.
 */

public final class FavouriteManager {
    private static FavouriteManager manager;

    private FavouriteManager() {

    }

    public static synchronized FavouriteManager onManager() {
        if (manager == null) {
            manager = new FavouriteManager();
        }
        return manager;
    }
}

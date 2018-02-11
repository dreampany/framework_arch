package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.api.network.data.manager.NetworkManager;
import com.dreampany.framework.data.model.Quote;
import com.dreampany.framework.data.model.StoreTask;
import com.dreampany.framework.data.provider.room.QuoteDatabase;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by air on 11/17/17.
 */

public class QuoteManager extends Manager<StoreTask<?>> {

    private static final long idleDelay = TimeUtil.secondToMilli(20);

    private static final String QUOTES = "quotes";
    private static final String TIME = "time";

    private static QuoteManager instance;
    private final Context context;
    private final Forismatic forismatic;
    private final Executor executor;

    private volatile boolean quoting;

    private QuoteManager(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        forismatic = new Forismatic();
        executor = Executors.newSingleThreadExecutor();
    }

    synchronized public static QuoteManager onInstance(Context context) {
        if (instance == null) {
            instance = new QuoteManager(context);
        }
        return instance;
    }

    @Override
    protected boolean looping() throws InterruptedException {
        //LogKit.verbose("NewsManager Items: " + count());
        StoreTask<?> task = pollTask();
        if (task == null) {
            waitRunner(wait);
            wait += pureWait;
            return true;
        }
        wait = defaultWait;

        long fireTime = FirestoreManager.onInstance().getLastTime();
        if (!isExpired(fireTime, idleDelay)) {
            putTaskUniquely(task);
            long delay = TimeUtil.getDelayTime(fireTime);
            long wait = idleDelay - delay;
            if (wait > 0L) {
                waitRunner(wait);
            }
            return true;
        }

        if (!FirebaseManager.onManager().resolveAuth()) {
            putTaskUniquely(task);
            return true;
        }
        FirestoreManager.onInstance().addMultiple(task.getCollection(), task.getData());
        return true;
    }

    public Quote getLatestQuote() {
        return QuoteDatabase.onInstance(context).quoteDao().getLatest();
    }

    public Quote getQuote(NetworkManager network) {
        long time = TimeUtil.startOfHour();
        Quote quote = QuoteDatabase.onInstance(context).quoteDao().getQuote(time);

        if (quote == null && network.hasInternet()) {
            quote = getFromFirestore(time);
            if (quote != null) {
                QuoteDatabase.onInstance(context).quoteDao().insert(quote);
            }
        }

        if (quote == null && network.hasInternet()) {
            quote = forismatic.get();
            if (quote != null) {
                quote.setTime(time);
                storeInFirestore(quote);
                QuoteDatabase.onInstance(context).quoteDao().insert(quote);
            }
        }

        return quote;
    }


    private Quote getFromFirestore(long time) {
        if (!FirebaseManager.onManager().resolveAuth()) {
            return null;
        }
        return FirestoreManager.onInstance().getSingle(QUOTES, TIME, time, Quote.class);
    }

    private void storeInFirestore(Quote quote) {
        String collection = QUOTES;
        Map<String, Object> data = Maps.newHashMap();
        data.put(quote.getId(), quote);
        StoreTask<?> task = new StoreTask<>();
        task.setCollection(collection);
        task.setData(data);
        putTaskUniquely(task);
    }
}

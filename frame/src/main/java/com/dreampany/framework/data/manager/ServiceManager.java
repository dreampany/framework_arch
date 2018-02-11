package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.util.LogKit;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.oasisfeng.condom.CondomContext;

/**
 * Created by air on 3/1/18.
 */

public class ServiceManager {

    private static ServiceManager instance;

    private ServiceManager() {
    }

    synchronized public static ServiceManager onInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public <T extends JobService> void scheduleService(CondomContext context, Class<T> clazz, int period) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        String tag = clazz.getSimpleName();

        Job job = dispatcher.newJobBuilder()
                .setService(clazz)
                .setTag(tag)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(period - 1, period))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK, Constraint.DEVICE_IDLE)
                .build();
        try {
            dispatcher.mustSchedule(job);
        } catch (FirebaseJobDispatcher.ScheduleFailedException e) {
            LogKit.verbose(tag + " error: " + e.toString());
        }
    }

    public <T extends JobService> void schedulePowerService(CondomContext context, Class<T> clazz, int period) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        String tag = clazz.getSimpleName();

        Job job = dispatcher.newJobBuilder()
                .setService(clazz)
                .setTag(tag)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, period))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        try {
            dispatcher.mustSchedule(job);
        } catch (FirebaseJobDispatcher.ScheduleFailedException e) {
            LogKit.verbose(tag + " error: " + e.toString());
        }
    }
}

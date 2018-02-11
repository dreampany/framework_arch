package com.dreampany.framework.data.manager;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by air on 11/14/17.
 */

public final class Scheduler {

    private static Scheduler instance;

    private Scheduler() {
    }

    synchronized public static Scheduler onInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    public static Job createJob(FirebaseJobDispatcher dispatcher, String identifier, Class<? extends JobService> serviceClass, int intervalInSeconds, int periodInSeconds) {
        Job job = dispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setService(serviceClass)
                .setTag(identifier)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(intervalInSeconds, intervalInSeconds + periodInSeconds))
                .setConstraints(Constraint.DEVICE_IDLE)
                .build();
        return job;
    }

/*    public static Job updateJob(FirebaseJobDispatcher dispatcher) {
        Job newJob = dispatcher.newJobBuilder()
                //update if any task with the given tag exists.
                .setReplaceCurrent(true)
                .setService(ScheduledJobService.class)
                .setTag("OneTimeJob")
                // Run between 60 - 120 seconds from now.
                .setTrigger(Trigger.executionWindow(60, 120))
                .build();
        return newJob;
    }*/

}

package com.dreampany.framework.data.manager;

import android.support.annotation.NonNull;

import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.LogKit;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by air on 11/17/17.
 */

public final class FirestoreManager {

    private static FirestoreManager instance;

    private final Object sync = new Object();

    private FirebaseFirestore firestore;

    private volatile boolean result;
    private volatile List<DocumentSnapshot> snaps;
    private volatile boolean waiting;

    private volatile long lastTime;
    private String currentPath;

    private FirestoreManager() {
        firestore = FirebaseFirestore.getInstance();
    }

    synchronized public static FirestoreManager onInstance() {
        if (instance == null) {
            instance = new FirestoreManager();
        }
        return instance;
    }

    synchronized public long getLastTime() {
        return lastTime;
    }

    synchronized public <T> boolean addSingle(String collection, String id, T item) {
        DocumentReference doc = firestore.collection(collection).document(id);
        Task<Void> task = doc.set(item, SetOptions.merge());
        currentPath = doc.getPath();
        task.addOnCompleteListener(writeCompleteListener).addOnFailureListener(writeFailureListener);
        waiting();
        return result;
    }

    synchronized public boolean addMap(String collection, String document, Map<String, Object> data) {
        DocumentReference doc = firestore.collection(collection).document(document);
        Task<Void> task = doc.set(data, SetOptions.merge());
        currentPath = doc.getPath();
        task.addOnCompleteListener(writeCompleteListener).addOnFailureListener(writeFailureListener);
        waiting();
        return result;
    }

    /**
     * @param collection a path such as: news (collection), news (collection)/sources (document)/sources (collection)
     * @param items
     * @param <T>
     * @return
     */
    synchronized public <T> boolean addMultiple(String collection, Map<String, T> items) {
        WriteBatch batch = firestore.batch();
        DocumentReference doc = null;
        for (Map.Entry<String, T> entry : items.entrySet()) {
            doc = firestore.collection(collection).document(entry.getKey());
            batch.set(doc, entry.getValue());
        }
        Task<Void> task = batch.commit();
        currentPath = doc.getPath();
        task.addOnCompleteListener(writeCompleteListener).addOnFailureListener(writeFailureListener);
        waiting();
        return result;
    }

    synchronized public <T> T getSingle(String collection, String upperKey, Object upperValue, Class<T> outputClass) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(1);
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        Task<QuerySnapshot> task = query.get();
        currentPath = collRef.getPath();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (snaps != null && snaps.size() > 0) {
            DocumentSnapshot snap = snaps.get(0);
            if (snap != null) {
                return snap.toObject(outputClass);
            }
        }
        return null;
    }


    synchronized public <T> T getSingle(String collection, String where, Object whereArg, String upperKey, Object upperValue, Class<T> outputClass) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(1);
        if (where != null && whereArg != null) {
            query = query.whereEqualTo(where, whereArg);
        }
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        Task<QuerySnapshot> task = query.get();
        currentPath = collRef.getPath() + "/" + whereArg;
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (snaps != null && snaps.size() > 0) {
            DocumentSnapshot snap = snaps.get(0);
            if (snap != null) {
                return snap.toObject(outputClass);
            }
        }
        return null;
    }

    synchronized public <T> T getSingle(String collection, String[] where, Object[] whereArg, Class<T> outputClass) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(1);
        if (where != null && whereArg != null) {
            for (int index = 0; index < where.length; index++) {
                if (where[index] != null && whereArg[index] != null) {
                    query = query.whereEqualTo(where[index], whereArg[index]);
                }
            }
        }
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (snaps != null && snaps.size() > 0) {
            DocumentSnapshot snap = snaps.get(0);
            if (snap != null) {
                return snap.toObject(outputClass);
            }
        }
        return null;
    }

    synchronized public <T> T getSingle(String collection, String[] where, Object[] whereArg, String upperKey, Object upperValue, Class<T> outputClass) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(1);
        if (where != null && whereArg != null) {
            for (int index = 0; index < where.length; index++) {
                if (where[index] != null && whereArg[index] != null) {
                    query = query.whereEqualTo(where[index], whereArg[index]);
                }
            }
        }
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        currentPath = collRef.getPath();
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (snaps != null && snaps.size() > 0) {
            DocumentSnapshot snap = snaps.get(0);
            if (snap != null) {
                return snap.toObject(outputClass);
            }
        }
        return null;
    }

    synchronized public <T> List<T> getMultiple(String collection, String[] where, Object[] whereArg, Class<T> outputClass, long limit) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (where != null && whereArg != null) {
            for (int index = 0; index < where.length; index++) {
                if (where[index] != null && whereArg[index] != null) {
                    query = query.whereEqualTo(where[index], whereArg[index]);
                }
            }
        }
        currentPath = collRef.getPath();
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (!DataUtil.isEmpty(snaps)) {
            List<T> items = new ArrayList<>(snaps.size());
            for (DocumentSnapshot snap : snaps) {
                items.add(snap.toObject(outputClass));
            }
            return items;
        }
        return null;
    }


    synchronized public <T> List<T> getMultiple(String collection, String upperKey, Object upperValue, Class<T> outputClass, long limit) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        currentPath = collRef.getPath();
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (!DataUtil.isEmpty(snaps)) {
            List<T> items = new ArrayList<>(snaps.size());
            for (DocumentSnapshot snap : snaps) {
                items.add(snap.toObject(outputClass));
            }
            return items;
        }
        return null;
    }

    synchronized public <T> List<T> getMultiple(String collection, String[] where, Object[] whereArg, String upperKey, Object upperValue, Class<T> outputClass, long limit) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (where != null && whereArg != null) {
            for (int index = 0; index < where.length; index++) {
                if (where[index] != null && whereArg[index] != null) {
                    query = query.whereEqualTo(where[index], whereArg[index]);
                }
            }
        }
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        currentPath = collRef.getPath();
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (!DataUtil.isEmpty(snaps)) {
            List<T> items = new ArrayList<>(snaps.size());
            for (DocumentSnapshot snap : snaps) {
                items.add(snap.toObject(outputClass));
            }
            return items;
        }
        return null;
    }

/*    synchronized public <T> List<T> getMultiple(String collection, String where, Object whereArg, String upperKey, Object upperValue, Class<T> outputClass, long limit) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (where != null && whereArg != null) {
            query = query.whereEqualTo(where, whereArg);
        }
        if (upperKey != null && upperValue != null) {
            query = query.whereGreaterThanOrEqualTo(upperKey, upperValue);
        }
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (!DataUtil.isEmpty(snaps)) {
            List<T> items = new ArrayList<>(snaps.size());
            for (DocumentSnapshot snap : snaps) {
                items.add(snap.toObject(outputClass));
            }
            return items;
        }
        return null;
    }*/

/*    synchronized public <T> List<T> getMultiples(String collection, String[] where, Object[] whereArg, String timeKey, long time, Class<T> outputClass, long limit) {
        lastTime = TimeUtil.currentTime();
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (where != null && whereArg != null) {
            for (int index = 0; index < where.length; index++) {
                if (where[index] != null && whereArg[index] != null) {
                    query = query.whereEqualTo(where[index], whereArg[index]);
                }
            }
        }
        if (timeKey != null && time > 0) {
            query = query.whereGreaterThanOrEqualTo(timeKey, time);
        }
        Task<QuerySnapshot> task = query.get();
        task.addOnCompleteListener(readCompleteListener);
        snaps = null;
        waiting();
        if (!DataUtil.isEmpty(snaps)) {
            List<T> items = new ArrayList<>(snaps.size());
            for (DocumentSnapshot snap : snaps) {
                items.add(snap.toObject(outputClass));
            }
            return items;
        }
        return null;
    }*/

    synchronized public boolean isReady() {
        return !waiting;
    }

    private void waiting() {
        waiting = true;
        synchronized (sync) {
            try {
                sync.wait(TimeUtil.secondToMilli(10));
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void notifyWaiting() {
        synchronized (sync) {
            sync.notify();
        }
        waiting = false;
    }


    //anonymous callback
    private final OnCompleteListener<Void> writeCompleteListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            result = task.isSuccessful();
            LogKit.verbose("firestore write completed: " + result + " - " + currentPath);
            notifyWaiting();
        }
    };

    private final OnFailureListener writeFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            LogKit.verbose("firestore write error: " + e.toString());
            result = false;
            notifyWaiting();
        }
    };

    private final OnCompleteListener<QuerySnapshot> readCompleteListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                LogKit.verbose("firestore read completed - " + currentPath);

                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    snaps = snapshot.getDocuments();
                }
            }
            notifyWaiting();
        }
    };
}

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

public final class FirestoreManagerV2 {

    private static FirestoreManagerV2 instance;

    private final Object sync = new Object();

    private FirebaseFirestore firestore;

    private volatile boolean result;
    private volatile List<DocumentSnapshot> snaps;
    private volatile boolean waiting;

    private FirestoreManagerV2() {
        firestore = FirebaseFirestore.getInstance();
    }

    synchronized public static FirestoreManagerV2 onInstance() {
        if (instance == null) {
            instance = new FirestoreManagerV2();
        }
        return instance;
    }

    synchronized public <T> boolean addSingle(String collection, String id, T item) {
        DocumentReference doc = firestore.collection(collection).document(id);
        Task<Void> task = doc.set(item, SetOptions.merge());
        task.addOnCompleteListener(writeCompleteListener).addOnFailureListener(writeFailureListener);
        waiting();
        return result;
    }

    synchronized public boolean addMap(String collection, String document, Map<String, Object> data) {
        DocumentReference doc = firestore.collection(collection).document(document);
        Task<Void> task = doc.set(data, SetOptions.merge());
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
        DocumentReference doc;
        for (Map.Entry<String, T> entry : items.entrySet()) {
            doc = firestore.collection(collection).document(entry.getKey());
            batch.set(doc, entry.getValue(), SetOptions.merge());
        }
        Task<Void> task = batch.commit();
        task.addOnCompleteListener(writeCompleteListener).addOnFailureListener(writeFailureListener);
        waiting();
        return result;
    }


    synchronized public <T> T getSingle(String collection, String where, Object whereArg, String timeKey, long time, Class<T> outputClass) {
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(1);
        if (where != null && whereArg != null) {
            query = query.whereEqualTo(where, whereArg);
        }
        if (timeKey != null && time > 0) {
            query = query.whereGreaterThanOrEqualTo(timeKey, time);
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

    synchronized public <T> T getSingle(String collection, String[] where, Object[] whereArg, Class<T> outputClass) {
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.whereEqualTo(where[0], whereArg[0]);
        for (int index = 1; index < where.length; index++) {
            query.whereEqualTo(where[index], whereArg[index]);
        }
        query = query.limit(1);
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

    synchronized public <T> List<T> getMultiple(String collection, String where, Object whereArg, String timeKey, long time, Class<T> outputClass, long limit) {
        CollectionReference collRef = firestore.collection(collection);
        Query query = collRef.limit(limit);
        if (where != null && whereArg != null) {
            query = query.whereEqualTo(where, whereArg);
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
    }

    synchronized public <T> List<T> getMultiples(String collection, String[] where, Object[] whereArg, String timeKey, long time, Class<T> outputClass, long limit) {
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
    }

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
            LogKit.verbose("firestore completed: " + task.toString());
            result = task.isSuccessful();
            notifyWaiting();
        }
    };

    private final OnFailureListener writeFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            LogKit.verbose("firestore error: " + e.toString());
            result = false;
            notifyWaiting();
        }
    };

    private final OnCompleteListener<QuerySnapshot> readCompleteListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    snaps = snapshot.getDocuments();
                }
            }
            notifyWaiting();
        }
    };
}

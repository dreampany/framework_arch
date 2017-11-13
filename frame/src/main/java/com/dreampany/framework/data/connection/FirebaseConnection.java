package com.dreampany.framework.data.connection;

import com.dreampany.framework.data.model.FirebaseLock;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dreampany.framework.data.util.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 6/10/2016.
 */
public class FirebaseConnection<T> implements DatabaseReference.CompletionListener, ValueEventListener, Connection {

    private final FirebaseLock<T, DatabaseError, FirebaseAuth> firebaseFirebaseLock = new FirebaseLock<>();

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        synchronized (firebaseFirebaseLock) {
            LogKit.logInfo("Waiting completed");
            firebaseFirebaseLock.error = databaseError;
            firebaseFirebaseLock.notify();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<T> items = null;

        LogKit.logInfo("DataSnapshot " + dataSnapshot.toString());

        if (dataSnapshot.getChildrenCount() > 0) {
            items = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                T t = snapshot.getValue(firebaseFirebaseLock.tClass);
                items.add(t);
            }
        }

        synchronized (firebaseFirebaseLock) {
            firebaseFirebaseLock.result = items;
            firebaseFirebaseLock.notify();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        synchronized (firebaseFirebaseLock) {
            firebaseFirebaseLock.error = databaseError;
            firebaseFirebaseLock.notify();
        }
    }

    private DatabaseReference toDatabase(String relativePath) {
        return FirebaseDatabase.getInstance().getReference(relativePath);
    }

    public T addToFirebase(T t, String parentPath, String childPath) {

        DatabaseReference dbRef = toDatabase(parentPath);
        DatabaseReference childRef = dbRef.child(childPath);
        childRef.setValue(t, this);

        synchronized (firebaseFirebaseLock) {
            try {
                LogKit.logInfo("Waiting for Clouds");
                firebaseFirebaseLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogKit.logInfo("Waiting End for Clouds");
        }

        if (firebaseFirebaseLock.error == null) {
            return t;
        }

        return null;
    }


    public List<T> takeFromFirebase(Class<T> tClass, String parentPath, long atTimestamp, int howMuch) {
        DatabaseReference dbRef = toDatabase(parentPath);

        Query queryRef = dbRef.orderByChild("timestamp").limitToFirst(howMuch).startAt(atTimestamp);
        LogKit.logInfo("Query Started");
        queryRef.addListenerForSingleValueEvent(this);

        synchronized (firebaseFirebaseLock) {
            try {
                firebaseFirebaseLock.tClass = tClass;
                firebaseFirebaseLock.result = null;
                firebaseFirebaseLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return firebaseFirebaseLock.result;
    }

    public List<T> takeFromFirebase(Class<T> tClass, String parentPath, String orderBy, long equalTo) {
        DatabaseReference dbRef = toDatabase(parentPath);

        Query queryRef = dbRef.orderByChild(orderBy).equalTo(equalTo);
        LogKit.logInfo("Query Started");
        queryRef.addListenerForSingleValueEvent(this);

        synchronized (firebaseFirebaseLock) {
            try {
                firebaseFirebaseLock.tClass = tClass;
                firebaseFirebaseLock.result = null;
                firebaseFirebaseLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return firebaseFirebaseLock.result;
    }

    public void deleteNode() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

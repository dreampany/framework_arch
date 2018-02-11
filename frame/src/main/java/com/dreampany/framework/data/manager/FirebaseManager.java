package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.connection.FirebaseConnection;
import com.dreampany.framework.data.model.FirebaseLock;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nuc on 4/7/2016.
 */
public final class FirebaseManager {

    private static final long AUTH_TIMEOUT = TimeUtil.secondToMilli(10);
    private final FirebaseLock<?, DatabaseError, FirebaseAuth> firebaseLock = new FirebaseLock<>();

    private static FirebaseManager firebaseManager;
    private FirebaseConnection firebaseConnection;

    private long serverTime;



    private FirebaseManager() {
        firebaseConnection = new FirebaseConnection<>();
    }

    synchronized public static FirebaseManager onManager() {
        if (firebaseManager == null) {
            firebaseManager = new FirebaseManager();
        }
        return firebaseManager;
    }


    public FirebaseUser toUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String toUid() {
        FirebaseUser user = toUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public boolean isAuthenticated() {
        return toUser() != null;
    }

    public String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getUid();
        }
        return null;
    }

    public boolean signInAnonymously() {
        if (isAuthenticated()) return true;

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(task -> notifyWaiting());
        waiting(TimeUtil.secondToMilli(15));
        return isAuthenticated();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void waiting(long timeout) {
        synchronized (firebaseLock) {
            try {
                firebaseLock.wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyWaiting() {
        synchronized (firebaseLock) {
            firebaseLock.notify();
        }
    }

    interface Key {
        String data = "data";
        String all = "all";
    }

    private <T> String toName(T t) {
        return toName(t.getClass());
    }

    private <T> String toName(Class<T> tClass) {
        return tClass.getSimpleName().toLowerCase(Locale.getDefault());
    }

    private <T> Long toHash(T t) {
        try {
            final Method getHashMethod = t.getClass().getMethod("getHash");
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    getHashMethod.setAccessible(true);
                    return null; // nothing to return
                }
            });
            Long hash = (Long) getHashMethod.invoke(t);
            return hash;
        } catch (SecurityException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T write(T t, String childPath) {

        if (t == null) return null;

        String parentPath = Key.data + "/" + toName(t) + "/" + Key.all;
        if (!DataUtil.isEmpty(childPath)) {
            parentPath += "/" + childPath;
        }

        String hash = String.valueOf(toHash(t));

        T result = (T) firebaseConnection.addToFirebase(t, parentPath, hash);

        return result;
    }

    public <T> T write(T t) {
        if (t == null) return null;
        String parentPath = Key.data + "/" + toName(t);
        String hash = String.valueOf(toHash(t));
        T result = (T) firebaseConnection.addToFirebase(t, parentPath, hash);
        return result;
    }

    public <T> List<T> write(List<T> ts) {

        if (ts == null || ts.isEmpty()) return null;

        List<T> results = new ArrayList<>();

        for (T t : ts) {
            String parentPath = Key.data + "/" + toName(t) + "/" + Key.all;
            String childPath = String.valueOf(toHash(t));
            T result = (T) firebaseConnection.addToFirebase(t, parentPath, childPath);
            results.add(result);
        }

        return results;
    }

    public <T> List<T> read(Class<T> tClass, String childPath, long atTimestamp, int howMuch) {
        String parentPath = Key.data + "/" + toName(tClass) + "/" + Key.all + "/" + childPath;
        return firebaseConnection.takeFromFirebase(tClass, parentPath, atTimestamp, howMuch);
    }

    public <T> List<T> read(Class<T> tClass, String childPath, String orderBy, long equalTo) {
        String parentPath = Key.data + "/" + toName(tClass) + "/" + Key.all;
        if (!DataUtil.isEmpty(childPath)) {
            parentPath += "/" + childPath;
        }
        return firebaseConnection.takeFromFirebase(tClass, parentPath, orderBy, equalTo);
    }

    public <T> List<T> read(Class<T> tClass, String orderBy, long equalTo) {
        String parentPath = Key.data + "/" + toName(tClass);
        return firebaseConnection.takeFromFirebase(tClass, parentPath, orderBy, equalTo);
    }

    public boolean resolveAuth() {
        if (!isAuthenticated()) {
            signInAnonymously();
        }
        return isAuthenticated();
    }

    public long getServerTime() {
        //FirebaseDatabase.getInstance().goOffline();
        //FirebaseDatabase.getInstance().goOnline();
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null) {
                    double offset = snapshot.getValue(Double.class);
                    serverTime = (long) (System.currentTimeMillis() + offset);
                }
                notifyWaiting();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        waiting(AUTH_TIMEOUT);
        return serverTime;
    }
}

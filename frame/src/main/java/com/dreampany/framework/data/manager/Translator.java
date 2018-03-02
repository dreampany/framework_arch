package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.model.Translate;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by air on 11/22/17.
 */

public final class Translator {

    private static final String TRANSLATES = "translates";
    private static final String KEY_HASH = "hash";

    private static Translator instance;
    private final FirestoreManager firestore;
    private Yandex yandex;
    private Map<Integer, Translate> translates;

    private Translator() {
        firestore = FirestoreManager.onInstance();
        yandex = new Yandex();
        translates = Maps.newConcurrentMap();
    }

    synchronized public static Translator onInstance() {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    public Translate getTranslate(Context context, String source, String target, String sourceText) {
        int hash = Objects.hashCode(source, target, sourceText);

        Translate translate = translates.get(hash);

        if (translate == null) {
            translate = FrameManager.onInstance(context).getTranslate(source, target, sourceText);
        }

        if (translate == null) {
            translate = getFromFirestore(hash);
            if (translate != null) {
                FrameManager.onInstance(context).translateDao().insert(translate);
            }
        }

        if (translate == null) {
            translate = yandex.getTranslate(source, target, sourceText);
            if (translate != null) {
                storeInFirestore(translate);
                FrameManager.onInstance(context).translateDao().insert(translate);
            }
        }

        if (translate != null) {
            translates.put(hash, translate);
        }

        return translate;
    }

    private Translate getFromFirestore(int hash) {
        if (FirebaseManager.onManager().resolveAuth()) {
            return firestore.getSingle(TRANSLATES, KEY_HASH, hash, null, 0L, Translate.class);
        }
        return null;
    }

    private void storeInFirestore(Translate translate) {
        if (FirebaseManager.onManager().resolveAuth()) {
            firestore.addSingle(TRANSLATES, String.valueOf(translate.hashCode()), translate);
        }
    }
}

package com.dreampany.framework.data.manager;

import android.content.Context;
import android.view.ViewGroup;

import com.dreampany.framework.data.util.ColorUtil;
import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 6/13/2017.
 */

public class ParticleManager extends Manager {

    private static ParticleManager instance;
    private final List<ConfettiManager> activeConfettiManagers = new ArrayList<>();

    private ParticleManager() {
    }

    synchronized public static ParticleManager onInstance() {
        if (instance == null) {
            instance = new ParticleManager();
        }
        return instance;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        for (ConfettiManager confettiManager : activeConfettiManagers) {
            confettiManager.terminate();
        }
        activeConfettiManagers.clear();
    }

    @Override
    protected boolean looping() throws InterruptedException {
        return false;
    }

    public void onShot(ViewGroup container) {
        Context context = container.getContext();
        ConfettiManager confettiManager = CommonConfetti.rainingConfetti(container, ColorUtil.getParticleColors(context)).oneShot();
        activeConfettiManagers.add(confettiManager);
    }

}

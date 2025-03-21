/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.config.monitor;

import com.ss.topt.conf.js.ConfJsTopt;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 *
 * @author marina
 */
@Service
public class ServiceSecurityRequest {

    private static int ARRAY_ERROR_SIZE = ConfJsTopt.getInstance().getApp().getErrorsSize() + 1;
    private static int ERROR_INTERVAL = ConfJsTopt.getInstance().getApp().getErrorsInterval();
    private static int ERROR_SLEEP_INTERVAL = ConfJsTopt.getInstance().getApp().getErrorsSleepInterval();
    private final Instant[] errorTimestamps = new Instant[ARRAY_ERROR_SIZE];
    private int currentIndex = 0;
    private Instant muteUntil = null; // time until we mute notification

    public boolean isNeedError() {
        Instant now = Instant.now();
        errorTimestamps[currentIndex] = now;
        Instant previousEl = errorTimestamps[currentIndex];

        currentIndex = (currentIndex + 1) % ARRAY_ERROR_SIZE;//increment index

        if (errorTimestamps[currentIndex] == null) {
            return false;
        }

        if (muteUntil != null && now.isBefore(muteUntil)) {
            return false; // if now in time between mute time, log error with level WARN
        }

        if ((now.toEpochMilli() - previousEl.toEpochMilli()) / 1_000 < ERROR_INTERVAL) {
            muteUntil = now.plusMillis(ERROR_SLEEP_INTERVAL * 1_000);
            return true;
        }
        muteUntil = null;
        return false;
    }

    public void updateUsingConfig() {
        ARRAY_ERROR_SIZE = ConfJsTopt.getInstance().getApp().getErrorsSize();
        ERROR_INTERVAL = ConfJsTopt.getInstance().getApp().getErrorsInterval();
        ERROR_SLEEP_INTERVAL = ConfJsTopt.getInstance().getApp().getErrorsInterval();
    }
}

package com.example.jamesho.dinero.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jamesho on 2018-10-13.
 */

public class CallServerIntentService extends IntentService {

    public CallServerIntentService() {super("CallServerIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        //TODO: add authcode
        CallServerTasks.executeTask(this, action, "");
    }
}

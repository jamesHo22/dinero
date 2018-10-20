package com.example.jamesho.dinero.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jamesho on 2018-10-19.
 */

/**
 * Define a service that returns a IBinder for the sync adapter class, allowing the sync adapter framework to call onPerformSync().
 */
public class SyncService extends Service {
    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdatperLock = new Object();
    public SyncService() {
        super();
    }
    /**
     * Instantiate the sync adapter object using the singleton pattern
     */
    @Override
    public void onCreate() {
        super.onCreate();

            if (sSyncAdapter == null) {
                sSyncAdapter = new  SyncAdapter(getApplicationContext(), true);
            }
            Log.v("SyncService", "SyncService created");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}

package com.example.jamesho.dinero.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by jamesho on 2018-10-19.
 */
/**
 * This class handles the data transfer between a server and app using the Android sync adapter framework
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    /**
     * This method handles the actual data transfer.
     * @param account is an account object associated with the event that triggered the sync adapter. If the server does not use accounts, this information will not be used.
     * @param extras are a Bundle containing flags sent by the event that triggered the sync adapter.
     * @param authority is the authority of the content provider in the system.
     * @param provider is a lightweight public interface to a content provider. Has the same functionality as a content resolver.
     * @param syncResult object that you iuse tot send information to the sync adapter framework.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        //TODO: Call the pending intent that accesses the database
    }
}

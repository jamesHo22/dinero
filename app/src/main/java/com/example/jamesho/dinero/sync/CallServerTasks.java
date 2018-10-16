package com.example.jamesho.dinero.sync;

import android.content.Context;
import android.util.Log;

import com.example.jamesho.dinero.Database.AppDatabase;
import com.example.jamesho.dinero.Database.ItemEntry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jamesho on 2018-10-13.
 */

public class CallServerTasks {
    private final static String TAG = CallServerTasks.class.getName();
    public final static String ACTION_CALL_SERVER = "query-server";
    public final static String ACTION_INSERT_ROOM = "insert-room";
    public final static String ACTION_SYNC_DATABASE = "sync-database";
    // Add Authentication parameters

    /**
     * A method that handles what Task to launch when called by an intentService
     * @param context the context
     * @param action a String that identifies which task to execute
     * @param extras holds any extra information. For example, an authentication token.
     */
    public static void executeTask(Context context, String action, String extras) {

        if (ACTION_CALL_SERVER.equals(action)) {
            AppDatabase mDb = AppDatabase.getInstance(context);
            // Declare null variables
            URL builtUri = null;
            HttpURLConnection urlConnection = null;
            // Assign variables to stuff
            builtUri = NetworkUtils.buildUrl(1);
            //FIXME: the builtURI is built every time the async task is called. Only build it if its a new URI
            try {
                urlConnection = (HttpURLConnection) builtUri.openConnection();
                String response = NetworkUtils.getResponseFromHTTPUrl(urlConnection);
                Log.v("Background ", response);
                // Make a new ItemEntry and put it in database
                ItemEntry testResponse = new ItemEntry(1,1,"Test", response, "Test", "Test");
                mDb.ItemDao().insertItem(testResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ACTION_INSERT_ROOM.equals(action)) {
            // TODO: mode the database operation in here
        }

        if (ACTION_SYNC_DATABASE.equals(action)) {
            // TODO: Add stuff here that syncs the local database with the one on the server. It should sync every time the app is opened and when the location changes.
            // TODO: Look into sync adapters
        }
    }
}

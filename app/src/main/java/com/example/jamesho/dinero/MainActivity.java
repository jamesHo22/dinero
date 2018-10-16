package com.example.jamesho.dinero;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jamesho.dinero.Database.AppDatabase;
import com.example.jamesho.dinero.Database.ItemEntry;
import com.example.jamesho.dinero.sync.CallServerIntentService;
import com.example.jamesho.dinero.sync.CallServerTasks;
import com.example.jamesho.dinero.sync.NetworkUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler {

    // Recycler View Things
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppDatabase mDb;
    private Toast mToast;

    DrawerLayout mDrawerLayout;

    //TODO: (Completed) put a recycler view in here that loads cardViews.
    //TODO: (Completed) make a side panel where a user can access the sign out option
    //TODO: (Completed) Read from a local database and populate the card views
    //TODO: (Completed) Add click and swipe features to the card views
    //TODO: Make an IntentService for database operations, JSON Parsing, Network Calling
    //TODO: Make Location Manager thing to trigger syncing service when phone moves


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "Please sign out to return to previous page", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String accountName = getIntent().getStringExtra("accountName");
        TextView textView = findViewById(R.id.signed_in_as);
        textView.setText("You are signed in as: " + accountName);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Handles actions when sign-out is clicked
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.sign_out:
                        // sign out
                        signOut();
                        break;
                    case R.id.add_fake_data:
                        // TODO: make the call method in Database directory to make and populate data
                        ItemEntry itemEntry = new ItemEntry(0, 0, "Main Course",
                                "The Big Mac consists of two 1.6 oz (45.4 g) beef patties, special sauce (a variant of Thousand Island dressing), iceberg lettuce, American cheese, pickles, and onions, served in a three-part sesame seed bun.",
                                "Big Mac",
                                "$3.99");
                        Log.v("Database", "test item added");
                        mDb.ItemDao().insertItem(itemEntry);
                        // Live data wrapper around the data should automatically update the UI
                }
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        // Get reference to the ROOM Database by creating a db instance
        mDb = AppDatabase.getInstance(getApplicationContext());
        //Get a reference to the recycler view on the main activity layout
        mRecyclerView = findViewById(R.id.rv__show_menu_items);
        // Initialize the layout manager for the item recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new ItemAdapter(this);

        retrieveData();
        mRecyclerView.setAdapter(mAdapter);

        //Add the swiping cards functionality using the simple callback
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
                // do not allow moving
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Do something when the item is swiped
                // Right now it will show a toast saying which direction you swiped.
                String mDirection;
                if (direction==ItemTouchHelper.RIGHT) {
                    mDirection = "right";
                    int position = viewHolder.getAdapterPosition();
                    List<ItemEntry> items = mAdapter.getItems();
                    mDb.ItemDao().deleteItem(items.get(position));
                    mAdapter.notifyItemRemoved(position);
                } else {
                    mDirection = "left";
                }
                Toast.makeText(getApplicationContext(), "You swiped " + mDirection, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        // Get reference to the ROOM Database by creating a db instance
        mDb = AppDatabase.getInstance(getApplicationContext());


    }

    @Override
    public void onClick(String testItem) {
        Context context = this;
        if (mToast != null) {
            mToast.cancel();
        }
        final Intent callServerIntent = new Intent(this, CallServerIntentService.class);
        callServerIntent.setAction(CallServerTasks.ACTION_CALL_SERVER);
        startService(callServerIntent);
        mToast = Toast.makeText(context, testItem, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * This method retrieves a live data object from the local database.
     * An observer is set on it to monitor changes and update the recycler view.
     */
    public void retrieveData() {
        // retrieving items from database
        Log.v("LiveData", "Actively retrieving data from database");
        final LiveData<List<ItemEntry>> items = mDb.ItemDao().loadAllItems();
        items.observe(this, new Observer<List<ItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntry> itemEntries) {
                if (itemEntries.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    Log.v("Database", "The database is empty");
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.setItems(itemEntries);
                }
            }
        });
    }

    // A method that instantiates a googleSignIn Object to allow the user to sign out
    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient;
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }

    /**
     * A temporary class for calling server in background
     * TODO: put this into a service
     */
    private class callServerTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            // Declare null variables
            URL builtUri = null;
            HttpURLConnection urlConnection = null;
            // Assign variables to stuff
            builtUri = NetworkUtils.buildUrl(integers[0]);
            //FIXME: the builtURI is built everytime the asynctask is called. Only build it if its a new URI
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
            return null;
        }
    }
}
